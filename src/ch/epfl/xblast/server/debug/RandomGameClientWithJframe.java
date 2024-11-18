package ch.epfl.xblast.server.debug;

import java.awt.event.KeyEvent;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Ticks;
import ch.epfl.xblast.server.graphics.BoardPainter;

@SuppressWarnings("javadoc")
public class RandomGameClientWithJframe {

    private static GameState game = Level.DEFAULT_LEVEL.gameState();
    private static BoardPainter BP = Level.DEFAULT_LEVEL.boardPainter();
    private static RandomEventGenerator REG = new RandomEventGenerator(2016, 30,
            100);
    static PlayerAction P1Action = null;
    public static void main(String[] args) {

        XBlastComponent xbc = new XBlastComponent();
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        
        // … autres correspondances touches / actions
        Consumer<PlayerAction> c = (x)->P1Action=x;
        xbc.addKeyListener(new KeyboardEventHandler(kb, c));
        xbc.setSize(xbc.getPreferredSize());
        xbc.setFocusable(true);
        xbc.requestFocusInWindow();
        JFrame jfr = new JFrame();
        jfr.setTitle("Test Xblast GUI : Random Game");
        jfr.setResizable(true);
        jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfr.add(xbc);
        jfr.pack();
        jfr.setVisible(true);
        for(PlayerID p:PlayerID.values())
            xbc.setGameState(GameStateDeserializer.deserializeGameState(
                    GameStateSerializer.serialize(BP, game)), p);
        
        
        
        
        while (!game.isGameOver()) {
            long before = System.currentTimeMillis();
            Map<PlayerID,Optional<Direction>> dirOpt = new HashMap<>(REG.randomSpeedChangeEvents());
            Set<PlayerID> bombs = new HashSet<>(REG.randomBombDropEvents());
            dirOpt.remove(PlayerID.PLAYER_1);
            bombs.remove(PlayerID.PLAYER_1);
            if(P1Action !=null)
            switch(P1Action){
            case DROP_BOMB:
                bombs.add(PlayerID.PLAYER_1);
                break;
            case JOIN_GAME:
                break;
            case MOVE_E:
                dirOpt.put(PlayerID.PLAYER_1, Optional.of(Direction.E));
                break;
            case MOVE_N:
                dirOpt.put(PlayerID.PLAYER_1, Optional.of(Direction.N));
                break;
            case MOVE_S:
                dirOpt.put(PlayerID.PLAYER_1, Optional.of(Direction.S));
                break;
            case MOVE_W:
                dirOpt.put(PlayerID.PLAYER_1, Optional.of(Direction.W));
                break;
            case STOP:
                dirOpt.put(PlayerID.PLAYER_1, Optional.empty());
                break;
            default:
                break;
            
            }
            P1Action = null;
            game = game.next(dirOpt, bombs);
                xbc.setGameState(GameStateDeserializer.deserializeGameState(
                        GameStateSerializer.serialize(BP, game)), PlayerID.PLAYER_1);
            long after = System.currentTimeMillis();
            System.out.println(after-before);
            if((after-before>1000/Ticks.TICKS_PER_SECOND))
                System.out.println("TOO FAST");
            try {
                Thread.sleep((after-before>1000/Ticks.TICKS_PER_SECOND)?0:1000/Ticks.TICKS_PER_SECOND-(after-before));
            }
            
            catch (InterruptedException e) {
                
                e.printStackTrace();
            }

        }
        //game is over
       
        
        
        try {
           
                Thread.sleep(8000);
                
            
        } 
        
        catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        jfr.dispose();
    }
    

}
