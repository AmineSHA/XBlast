package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    private static int MAXIMUM_SERIALISED_BYTE = 409;
    static private Map<SocketAddress, PlayerID> addressID = new HashMap<>();
    static private BoardPainter bp = Level.DEFAULT_LEVEL.boardPainter();
    static private GameState gs = Level.DEFAULT_LEVEL.gameState();

    /**
     * main method, useful for the program's correct functioning
     * @param args
     *      numbers of players
     * @throws InterruptedException
     *      when interruptions occur
     */
    public static void main(String args[]) throws InterruptedException {

        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();

        List<Byte> dfBytes = GameStateSerializer.serialize(bp, gs);

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
                    senderAddress = channel.receive(buffer);
                    addressID.put(senderAddress,
                            PlayerID.values()[addressID.size()]);

                }
                System.out.println("after if");

            }
            System.out.println(addressID);
            
            
            // phase 2 part giving gameState
            long beginning = System.nanoTime();
            long nextTick = beginning;
            channel.configureBlocking(false);
            ByteBuffer b = ByteBuffer.allocate(MAXIMUM_SERIALISED_BYTE);
            
            while (!gs.isGameOver()) {

                System.out.println("game not over");
                for (SocketAddress s : addressID.keySet()) {
                    b.clear();
                
                    b.put((byte) (addressID.get(s).ordinal()+1));//we add 1 to avoid having 0 in buffer.get(0)
                    
                    for (Byte e : dfBytes) 
                        b.put(e);
                    
                    
                    b.flip();
                    channel.send(b, s);
                    System.out.println("stuff sent");
                }

                // phase 2 part time
                nextTick += Ticks.TICK_NANOSECOND_DURATION;

                if (nextTick < System.nanoTime())
                    Thread.sleep((nextTick - System.nanoTime()) / Time.US_PER_S,
                            (int) (nextTick - System.nanoTime())
                                    % Time.US_PER_S);

             // phase 2 part receive infos
                for (SocketAddress s : addressID.keySet()) {
                    channel.receive(buffer);
                    buffer.flip();
                    whatToDo.put(addressID.get(s),
                            PlayerAction.values()[buffer.get(0)]);
                    
                }

                // phase 2 part Compute nextGamestate
                for (PlayerID ID : whatToDo.keySet()) {
                    if (whatToDo.get(ID).isMovementInput())
                        speedChangeEvents.put(ID, Optional.ofNullable(
                                whatToDo.get(ID).equivalentDirection()));

                    else if (whatToDo.get(ID).equals(PlayerAction.DROP_BOMB)) {
                        bombDropEvents.add(ID);
                    }
                }
                gs = gs.next(speedChangeEvents, bombDropEvents);
                dfBytes = GameStateSerializer.serialize(bp, gs);

            }
            System.out.println(gs.winner().isPresent()?"winner is player: " + gs.winner():"Draw");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}