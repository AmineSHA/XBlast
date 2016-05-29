package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.graphics.BoardPainter;

import java.util.List;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class Main {

    private static int NECES_PLAYERS = 4;
    private static int MAXIMUM_SERIALISED_BYTE = 420;
    static private Map<SocketAddress, PlayerID> addressID = new HashMap<>();
    static private BoardPainter bp = Level.DEFAULT_LEVEL.boardPainter();
    static private GameState gs = Level.DEFAULT_LEVEL.gameState();

    /**
     * main method, useful for the program's correct functioning
     * 
     * @param args
     *            numbers of players
     * @throws InterruptedException
     *             when interruptions occur
     */
    public static void main(String args[]) throws InterruptedException {

        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();

        

        // 1st Phase;
        Map<PlayerID, PlayerAction> whatToDo = new HashMap<>();
        
        if (args.length != 0)
            NECES_PLAYERS = Integer.parseInt(args[0]);

        try {

            DatagramChannel channel = DatagramChannel
                    .open(StandardProtocolFamily.INET);
            channel.bind(new InetSocketAddress(2016));

            ByteBuffer buffer = ByteBuffer.allocate(1);
            SocketAddress senderAddress;

            while (addressID.size() < NECES_PLAYERS) {
                System.out.println("while");
                buffer.clear();
                senderAddress = channel.receive(buffer);
                System.out.println("received");
                buffer.flip();
                if (!(addressID.containsKey(senderAddress))
                        && buffer.get(0) == 0) {

                    System.out.println("if");
                    addressID.put(senderAddress,
                            PlayerID.values()[addressID.size()]);

                }
                System.out.println("after if");

            }
            System.out.println(addressID);

            // phase 2 part giving gameState
            List<Byte> dfBytes = new ArrayList<>();
            dfBytes.addAll(GameStateSerializer.serialize(bp, gs));
            
            
            channel.configureBlocking(false);
            ByteBuffer b = ByteBuffer.allocate(MAXIMUM_SERIALISED_BYTE);

            while (!gs.isGameOver()) {

                System.out.println("game not over");
                for (SocketAddress s : addressID.keySet()) {
                    b.clear();

                    
                    dfBytes.add(0,(byte)(addressID.get(s).ordinal()));
                    System.out.println(dfBytes);
                    /*
                     * we add 1 to avoid having 0 in buffer.get(0)
                     */

                    for (Byte e : dfBytes)
                        b.put(e);

                    b.flip();
                    channel.send(b, s);
                    System.out.println("stuff sent");
                    b.rewind();
                }

                long beginning = System.nanoTime();
                long nextTick = beginning;
                // phase 2 part time
                nextTick += Ticks.TICK_NANOSECOND_DURATION;

                if (nextTick > System.nanoTime())
                    Thread.sleep((nextTick - System.nanoTime()) / Time.US_PER_S,
                            (int) (nextTick - System.nanoTime())
                                    % Time.US_PER_S);

                // phase 2 part receive infos
                SocketAddress s=null;
                while (Objects.nonNull(s=channel.receive(buffer))) {

                    
                    
                    PlayerAction a =PlayerAction.values()[buffer.get(0)];
                    PlayerID pp=addressID.get(s);
                    System.out.printf("playerID %s action %s\n",pp,a);
                    whatToDo.put(pp,
                            a);
                    buffer.clear();
                }

                // phase 2 part Compute nextGamestate
                for (PlayerID ID : whatToDo.keySet()) {
                    if (whatToDo.get(ID).isMovementInput())
                        speedChangeEvents.put(ID, Optional.ofNullable(
                                whatToDo.get(ID).equivalentDirection()));

                    else if (whatToDo.get(ID).equals(PlayerAction.DROP_BOMB)) {
                        bombDropEvents.add(ID);
                    }
                    //and else do nothing
                }
                gs = gs.next(speedChangeEvents, bombDropEvents);
                dfBytes = GameStateSerializer.serialize(bp, gs);
                whatToDo.clear();
                bombDropEvents.clear();

            }
            System.out.println(gs.winner().isPresent()
                    ? "winner is player: " + gs.winner() : "Draw");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}