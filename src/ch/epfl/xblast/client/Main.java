package ch.epfl.xblast.client;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class Main {

    private static DatagramChannel CHANNEL;
    private static SocketAddress ADDRESS;
    private static final int HIGHEST_SERIALISED_BYTE = 420;
    private static final int PORT = 2016;

    private static PlayerAction thisPlayerAction = null;

    /**
     * Used to display
     * 
     * @param xbc
     *            XBlastComponent
     */
    public static void createUI(XBlastComponent xbc) {

        Map<Integer, PlayerAction> keyboardKeys = new HashMap<>();
        keyboardKeys.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        keyboardKeys.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        keyboardKeys.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        keyboardKeys.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        keyboardKeys.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        keyboardKeys.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);

        Consumer<PlayerAction> thisPlayerActionCons = x -> {

            thisPlayerAction = x;

            if (!Objects.isNull(thisPlayerAction)) {
                ByteBuffer actionBuffer = ByteBuffer.allocate(1);
                actionBuffer.put((byte) thisPlayerAction.ordinal());
                actionBuffer.flip();

                try {
                    CHANNEL.send(actionBuffer, ADDRESS);

                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                thisPlayerAction = null;
            }

        };
        xbc.addKeyListener(
                new KeyboardEventHandler(keyboardKeys, thisPlayerActionCons));

        xbc.setSize(xbc.getPreferredSize());
        xbc.setFocusable(true);
        xbc.requestFocusInWindow();
        JFrame jfr = new JFrame();
        jfr.setTitle("Xblast");
        jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfr.setResizable(true);
        jfr.add(xbc);
        jfr.pack();
        jfr.setVisible(true);

    }

    /**
     * main method, useful for the program's correct functioning
     * 
     * @param args
     *            the server IP (or nothing if server is localHost)
     * @throws InterruptedException
     *             an exception
     * @throws IOException
     *             an exception
     * @throws InvocationTargetException
     *             an exception
     */
    public static void main(String args[]) throws InterruptedException,
            IOException, InvocationTargetException {

        XBlastComponent xbc = new XBlastComponent();
        SwingUtilities.invokeAndWait(() -> createUI(xbc));
        GameState gs;

        CHANNEL = DatagramChannel.open(StandardProtocolFamily.INET);
        ADDRESS = new InetSocketAddress(
                args.length == 0 ? "localhost" : args[0], PORT);
        ByteBuffer sendBuffer = ByteBuffer.allocate(1);
        ByteBuffer receiveBuffer = ByteBuffer.allocate(HIGHEST_SERIALISED_BYTE);

        CHANNEL.configureBlocking(false);

        while (Objects.isNull(CHANNEL.receive(receiveBuffer))) {

            sendBuffer.clear();
            sendBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
            sendBuffer.flip();
            CHANNEL.send(sendBuffer, ADDRESS);
            Thread.sleep(Time.CLIENT_WAIT_TIME);
        }


        receiveBuffer.flip();

        List<Byte> gsList = new ArrayList<>();

        while (receiveBuffer.hasRemaining())
            gsList.add(receiveBuffer.get());

        PlayerID myid = PlayerID.values()[gsList.get(0)];

        System.out.println(gsList);
        gs = GameStateDeserializer
                .deserializeGameState(gsList.subList(1, gsList.size()));
        xbc.setGameState(gs, myid);
        gsList.clear();

        CHANNEL.configureBlocking(true);
        while (true) {

            receiveBuffer.clear();

            CHANNEL.receive(receiveBuffer);

            receiveBuffer.flip();

            while (receiveBuffer.hasRemaining())
                gsList.add(receiveBuffer.get());

            gs = GameStateDeserializer
                    .deserializeGameState(gsList.subList(1, gsList.size()));
            xbc.setGameState(gs, myid);
            gsList.clear();

        }

    }

}
