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
import java.util.List;
 

import ch.epfl.xblast.server.graphics.BoardPainter;


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
    private static final int HIGHEST_SERIALISED_BYTE = 420;
    static private Map<SocketAddress, PlayerID> addressID = new HashMap<>();
    static private final BoardPainter bp = Level.DEFAULT_LEVEL.boardPainter();
    static private GameState gs = Level.DEFAULT_LEVEL.gameState();
    private static final int PORT = 2016;

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
            channel.bind(new InetSocketAddress(PORT));

            ByteBuffer buffer = ByteBuffer.allocate(1);
            SocketAddress senderAddress;

            while (addressID.size() < NECES_PLAYERS) {

                buffer.clear();
                senderAddress = channel.receive(buffer);

                buffer.flip();
                if (!(addressID.containsKey(senderAddress))
                        && buffer.get(0) == 0) {

                    addressID.put(senderAddress,
                            PlayerID.values()[addressID.size()]);

                }

            }
            System.out.println(addressID);

            // phase 2 part giving gameState
            List<Byte> dfBytes = new ArrayList<>();

            channel.configureBlocking(false);
            ByteBuffer gsBuffer = ByteBuffer.allocate(HIGHEST_SERIALISED_BYTE);
            SocketAddress sendAddress = null;

            while (!gs.isGameOver()) {

                dfBytes.add((byte) -1);
                dfBytes.addAll(GameStateSerializer.serialize(bp, gs));

                for (SocketAddress s : addressID.keySet()) {
                    gsBuffer.clear();

                    dfBytes.set(0, (byte) (addressID.get(s).ordinal()));
                    System.out.println(dfBytes);

                    for (Byte e : dfBytes)
                        gsBuffer.put(e);

                    gsBuffer.flip();
                    channel.send(gsBuffer, s);

                }

                long beginning = System.nanoTime();
                long nextTick = beginning + Ticks.TICK_NANOSECOND_DURATION;
                // phase 2 part time

                if (nextTick > System.nanoTime())
                    Thread.sleep((nextTick - System.nanoTime()) / Time.US_PER_S,
                            (int) (nextTick - System.nanoTime())
                                    % Time.US_PER_S);

                // phase 2 part receive infos

                while (Objects.nonNull(sendAddress = channel.receive(buffer))) {

                    PlayerAction action = PlayerAction.values()[buffer.get(0)];
                    PlayerID corespID = addressID.get(sendAddress);
                    whatToDo.put(corespID, action);
                    buffer.clear();
                }

                // phase 2 part Compute nextGamestate
                for (PlayerID ID : whatToDo.keySet()) {
                    if (whatToDo.get(ID).isMovementInput())
                        speedChangeEvents.put(ID, Optional.ofNullable(
                                whatToDo.get(ID).equivalentDirection()));

                    else if (whatToDo.get(ID).equals(PlayerAction.DROP_BOMB))
                        bombDropEvents.add(ID);

                    // and else do nothing
                }
                gs = gs.next(speedChangeEvents, bombDropEvents);
                dfBytes.clear();
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