package ch.epfl.xblast.client;
import java.awt.event.*;
import java.util.*;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class KeyboardEventHandler extends KeyAdapter {

    
     final private Map<Integer,PlayerAction> keyMap;
     final private Consumer<PlayerAction> consumer;
    
    /**
     * KeyboardEventHandler constructor
     * @param keyMap
     * @param e
     */
    public KeyboardEventHandler(Map<Integer,PlayerAction> keyMap,Consumer<PlayerAction> e){
    this.keyMap=new HashMap<Integer, PlayerAction>(keyMap);
    this.consumer=e; 
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        if(keyMap.containsKey(e.getKeyCode()))
            consumer.accept(keyMap.get(e.getKeyCode()));
    }

}