package ch.epfl.xblast.client;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

public class GameStateDeserializer {

    private GameStateDeserializer(){}
    
    public static GameState deserializeGameState(List<Byte> encoded){
        
        
        
        
        
    }
    
     private static  List<Image> deserializeBoard(List<Byte> encodedBoard){
         List<Image> board = new ArrayList<>();
         
         
     }
     
     private static List<Image> deserializeExplosions(List<Byte> encodedExplosionsBombs){
         
     }
     

//   methode testee
     private static List<Player> deserializePlayers(List<Byte> encodedPlayers){
         
         if(encodedPlayers.size()!=PlayerID.values().length*4)
             throw new IllegalArgumentException();
         
         List<Byte> temp = new ArrayList<>(encodedPlayers);
         
         
         ImageCollection playerCollection=new ImageCollection("player");
         List<Player> playerList=new ArrayList<>();
         for (int i=0;i<4;i++) {
            playerList.add(new Player(PlayerID.values()[i],Byte.toUnsignedInt(temp.get(0)),new SubCell(Byte.toUnsignedInt(temp.get(1)), Byte.toUnsignedInt(temp.get(2))),playerCollection.image(temp.get(3))));
            temp=temp.subList(4, temp.size());
         
         }
         return playerList;
     }
    
}