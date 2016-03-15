package ch.epfl.xblast;

import ch.epfl.cs108.*;
import ch.epfl.xblast.server.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
public class GameState {
   
	
	
	private final int ticks;
	private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
	
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs,
			List<Sq<Sq<Cell>>> explosions, List<Sq<Cell>> blasts) {
          Objects.requireNonNull(board);
          Objects.requireNonNull(players);
          Objects.requireNonNull(bombs);
          Objects.requireNonNull(explosions);
          Objects.requireNonNull(blasts);
          ArgumentChecker.requireNonNegative(ticks);
          if(ticks!=4){
        	  throw new IllegalArgumentException();
          }
          this.ticks=ticks;
          this.board=board;
          this.players=players;
          this.bombs=bombs;
          this.explosions=explosions;
          this.blasts=blasts;
	}
	public GameState(Board board, List<Player> players){
		this(0,board,players,new ArrayList<Bomb>(0),new ArrayList<Sq<Sq<Cell>>>(0),new ArrayList<Sq<Cell>>(0));
	}
	
	public int ticks(){
		return ticks;
	}
	 public boolean isGameOver(){
		return(alivePlayers().isEmpty() || Ticks.TOTAL_TICKS == 2400);
			
		
	}
	public Optional<PlayerID> winner(){
		if(Ticks.TOTAL_TICKS == 0 || (Ticks.TOTAL_TICKS == 2400 && alivePlayers().size()>1)) {
			return Optional.empty();
		}
		
		return Optional.of((alivePlayers().get(0).id()));
		
	}
	
	public Board board(){
		return board;
	}
	public List<Player> players(){
		return players;
	}
	public List<Player> alivePlayers(){
	 List<Player> alivePlayers = new ArrayList<>();
	 for(Player r : players){
		 if(r.isAlive()){
			 alivePlayers.add(r);
		 } 
		 }
	 return alivePlayers;
		 
		 
		 
	 }
	}
	

