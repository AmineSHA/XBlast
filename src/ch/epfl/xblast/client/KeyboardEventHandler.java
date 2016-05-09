package ch.epfl.xblast.client;
import java.awt.event.*;
import ch.epfl.xblast.server.PlayerAction;
import java.util.*;
import java.util.function.Consumer;

public class KeyboardEventHandler extends KeyAdapter {

    
    Map<Integer,PlayerAction> keyMap;
    Consumer<PlayerAction> consumer;
    
    public KeyboardEventHandler(Map<Integer,PlayerAction> keyMap,Consumer<PlayerAction> e){
    this.keyMap=keyMap;
    this.consumer=consumer; 
    }
    
    public void keyPressed(KeyEvent e){
        if(keyMap.containsKey(e.getKeyCode()))
            consumer.accept(keyMap.get(e.getKeyCode()));
    }

}