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

public class Main {

    private static int NECES_PLAYERS;
    static private Map<SocketAddress, PlayerID> addressID = new HashMap<>();
    static private BoardPainter bp =Level.DEFAULT_LEVEL.boardPainter();
    Map<PlayerID, Optional<Direction>> speedChangeEvents=new HashMap<>();
    Set<PlayerID> bombDropEvents=new HashSet<>();
    

    public static void main(String args[]) throws InterruptedException {
        
        Map<PlayerID, Optional<Direction>> speedChangeEvents=new HashMap<>();
        Set<PlayerID> bombDropEvents=new HashSet<>();
        
        GameState gs = Level.DEFAULT_LEVEL.gameState();
        List<Byte> dfBytes = GameStateSerializer.serialize(
                bp, gs);
        
        // 1st Phase;
        Map<PlayerID, PlayerAction>  whatToDo= new HashMap<>();
        if (args.length == 0)
            NECES_PLAYERS = 4;
        else
            NECES_PLAYERS = Integer.parseInt(args.toString());

        try {
            DatagramChannel channel = DatagramChannel
                    .open(StandardProtocolFamily.INET);
            channel.bind(new InetSocketAddress(2016));

            ByteBuffer buffer = ByteBuffer.allocate(1);
            SocketAddress senderAddress = channel.receive(buffer);

            
            while (addressID.size() < NECES_PLAYERS) {
                if (!(addressID.containsKey(senderAddress))
                        && buffer.get(0) == 0) {

                    buffer.flip();
                    senderAddress = channel.receive(buffer);
                    addressID.put(senderAddress,
                            PlayerID.values()[addressID.size()]);
                    Thread.sleep(25);
                }
            
            }
            long beginning = System.nanoTime();
            long nextTick = beginning;
            channel.configureBlocking(false);
            
            
            
            
            
            
            
            while (!gs.isGameOver()) {
                
               
                for (SocketAddress s : addressID.keySet()) {
                    ByteBuffer b = ByteBuffer.allocate(dfBytes.size());
                    b.put((byte) (addressID.get(s).ordinal()));
                    for (Byte e : dfBytes)
                        b.put(e);

                    channel.send(b, s);

                }

             nextTick +=  Ticks.TICK_NANOSECOND_DURATION;
             
                if ((nextTick - System.nanoTime()) > 0) {
                    
                    Thread.sleep(nextTick - System.nanoTime());
                    for(SocketAddress s : addressID.keySet())
                     {
                         buffer.flip();
                         channel.receive(buffer);
                         whatToDo.put(addressID.get(s), PlayerAction.values()[buffer.get(0)]);
                         buffer.clear();
                         
                     }

                }
                
                
                
                
                //Compute nextGamestate
                for (PlayerID ID : whatToDo.keySet()) {
                    if(whatToDo.get(ID).isMovementInput())
                    speedChangeEvents.put(ID, Optional.ofNullable(whatToDo.get(ID).equivalentDirection()));//ofnullabe make an empty optional if null
                    
                    else if(whatToDo.get(ID).equals(PlayerAction.DROP_BOMB)){
                        bombDropEvents.add(ID);
                    }
                }
                
                gs=gs.next(speedChangeEvents, bombDropEvents);
                

                
            }
            //here the game is finished
            
            

        } catch (IOException e) {
            // TODO Auto-generated catch block wolololo
            e.printStackTrace();
        }


    }

}