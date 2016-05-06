package ch.epfl.xblast.client;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

public class GameStateDeserializer {

    private GameStateDeserializer(){}
    
    public static GameState deserializeGameState(List<Byte> encoded){
        
        
        
        
        
    }
    
     public static  List<Image> deserializeBoardAndRowMajoredIt(List<Byte> encodedBoard){
         
         ImageCollection BoardCollection=new ImageCollection("block");
         Image board[] = new Image[Cell.COUNT];
         
         for (Cell c : Cell.SPIRAL_ORDER) 
            board[c.rowMajorIndex()]=BoardCollection.image(encodedBoard.get(Cell.SPIRAL_ORDER.indexOf(c)));
         
         
         return new ArrayList<Image>(Arrays.asList(board));
         
     }
     
     private static List<Image> deserializeExplosions(List<Byte> encodedExplosionsBombs){
         

         ImageCollection explosionsBombsCollection=new ImageCollection("explosion");
         List<Image> explosionBomb=new ArrayList<>();
         
         for (Byte b : encodedExplosionsBombs) 
            explosionBomb.add(explosionsBombsCollection.imageOrNull(b));
        
         return explosionBomb;
         
     }
     

//   methode testee(pour la re tester il faut la passer en private)
     private static List<Player> deserializePlayers(List<Byte> encodedPlayers){
         
         //TODO la partie dessous est surtout utile pour les tests
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