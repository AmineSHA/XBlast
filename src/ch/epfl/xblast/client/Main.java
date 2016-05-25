package ch.epfl.xblast.client;

import java.util.List;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.server.graphics.BoardPainter;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class Main {

    private static DatagramChannel CHANNEL;
    private static SocketAddress   ADDRESS;
    private static int MAXIMUM_SERIALISED_BYTE = 409;
    private PlayerID MYID;

    
    private static BoardPainter bp = Level.DEFAULT_LEVEL.boardPainter();
    private static PlayerAction thisPlayerAction = null;
    
    
    /**
     * Used to display
     * @param xbc
     *      xblastcomponent
     */
    public static void createUI(XBlastComponent xbc){
        
        
        
        Map<Integer, PlayerAction> keyboardKeys =new HashMap<>();
        keyboardKeys.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        keyboardKeys.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        keyboardKeys.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        keyboardKeys.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        keyboardKeys.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        keyboardKeys.put(KeyEvent.VK_ALT, PlayerAction.STOP);
        Consumer<PlayerAction> thisPlayerActionCons = x->thisPlayerAction=x;
       xbc.addKeyListener(new KeyboardEventHandler(keyboardKeys,thisPlayerActionCons));
       
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
     * @param args
     *         the server IP
     * @throws InterruptedException
     *          an exception
     * @throws IOException
     *          
     * @throws InvocationTargetException
     */
    public static void main(String args[]) throws InterruptedException,
        IOException, InvocationTargetException {

        XBlastComponent xbc = new XBlastComponent();
        SwingUtilities.invokeAndWait(()-> createUI(xbc));
        GameState gs = null;

        CHANNEL = DatagramChannel
                .open(StandardProtocolFamily.INET);
        ADDRESS= new InetSocketAddress(args.length == 0 ? "localhost" : args[0], 2016);
        ByteBuffer sendBuffer = ByteBuffer.allocate(1);
        ByteBuffer receiveBuffer = ByteBuffer.allocate(MAXIMUM_SERIALISED_BYTE);
    
        System.out.println(receiveBuffer.get(MAXIMUM_SERIALISED_BYTE));
        
        List<Byte> gsList = new ArrayList<>();
        CHANNEL.configureBlocking(false);
        do  {
            
            CHANNEL.receive(receiveBuffer);
            System.out.println(receiveBuffer.toString()+"203");
            sendBuffer.clear(); 
            sendBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
            sendBuffer.flip();
            CHANNEL.send(sendBuffer, ADDRESS);
            Thread.sleep(Time.CLIENT_WAIT_TIME);
        }while(Objects.isNull(receiveBuffer.get(0)));
        
        while(true){
            
            receiveBuffer = ByteBuffer.allocate(MAXIMUM_SERIALISED_BYTE);
            receiveBuffer.clear();
            CHANNEL.receive(receiveBuffer);
            System.out.println(receiveBuffer.toString()+"25");
            receiveBuffer.flip();
            System.out.println(receiveBuffer.toString()+"4205");
             PlayerID MYID = PlayerID.values()[receiveBuffer.get()];
            while (receiveBuffer.hasRemaining()) {
                gsList.add(receiveBuffer.get());
            }
            gs = GameStateDeserializer.deserializeGameState(gsList);
            xbc.setGameState(gs,MYID);
            gsList.clear();
            
            
        }
    
    }

}
