package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.ArgumentChecker;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
final public class Player {

	private final PlayerID id;
	private final Sq<LifeState> lifeStates;
	private final Sq<DirectedPosition> directedPos;
	private final int maxBombs;
	private final int bombRange;

	
	@Override
    public String toString() {
        return "( " + id + ", " + lifeStates.head() + " ,"+   directedPos.head()   + " ,"+  maxBombs   + " ,"+    bombRange      +")";
    }
	
	/**
     * Principal Player constructor
     * @param id
     *      the player's ID
     * @param lifeStates
     *      The player's LifeState Sequence (Sq)
     * @param directedPos
     *      The player's DiectedPos Sequence (Sq)
     * @param maxBombs
     *      How many bombs the player can have at the same time
     * @param bombRange
     *      The player's bomb range maximum
     */
	public Player(PlayerID id, Sq<LifeState> lifeStates,
			Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) {

		this.id = Objects.requireNonNull(id);
		this.lifeStates = Objects.requireNonNull(lifeStates);
		this.directedPos = Objects.requireNonNull(directedPos);
		this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
		this.bombRange = ArgumentChecker.requireNonNegative(bombRange);

	}

	/**
     * Secondary Player constructor
     * @param id
     *      the player's ID 
     * @param lives
     *      The player's life number
     * @param position
     *      The Cell occupied by the player
     * @param maxBombs
     *      How many bombs the player can have at the same time
     * @param bombRange
     *      The player's bomb range maximum
     */
	public Player(PlayerID id, int lives, Cell position, int maxBombs,
			int bombRange) {
	    
		this(id, methodForConstructor(lives),
				Sq.constant(new DirectedPosition(SubCell
						.centralSubCellOf(position), Direction.S)), maxBombs,
				bombRange);

	}
	
	private static Sq<LifeState> methodForConstructor(int lives){
	    if(lives==0)
	        return Sq.constant(new LifeState(lives, State.DEAD));
	    
	    return Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
                new LifeState(lives, LifeState.State.INVULNERABLE)).concat(
                Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE)));
	 
	}

	/**
     * PlayerID getter
     * @return the player's PlayerID
     */
	public PlayerID id() {
		return id;
	}

	/**
     * Player's lifestate sequence getter
     * @return  Sq<LifeState> lifeStates
     */
	public Sq<LifeState> lifeStates() {
		return lifeStates;
	}

	/**
     * Player's actual lifestate getter
     * @return  the first lifestate of the lifestates sequence
     */
	public LifeState lifeState() {
		return lifeStates().head();
	}
/**
 * Basically is the method statesForNextLife I just thought it would be clearer to put this piece of code in another method
 * 
 * @param lives
 * @returns sequence of LifeStates.
 */
	private Sq<LifeState> constructLifeStateSequence(int lives) {   
		Sq<LifeState> dyingBasisSequence = Sq.repeat(Ticks.PLAYER_DYING_TICKS,
				new LifeState(lives, LifeState.State.DYING));

		if (lives()<=1) {
			return dyingBasisSequence.concat(Sq.constant(new LifeState(0,
					LifeState.State.DEAD)));
		} else {
			return dyingBasisSequence.concat(Sq.repeat(
					Ticks.PLAYER_INVULNERABLE_TICKS,
					new LifeState(lives - 1, LifeState.State.INVULNERABLE))
					.concat(Sq.constant(new LifeState(lives - 1,
							LifeState.State.VULNERABLE))));
		}
	}

	/**
     * Construct a new lifeState sequence for players who've just been hit
     * @return Sq<LifeState> the state for the next life
     */
	public Sq<LifeState> statesForNextLife() {

		return constructLifeStateSequence(lifeStates.head().lives());

	}

	/**
     * Player's life number getter
     * @return Current lives total
     */
	public int lives() {
		return lifeStates.head().lives();
	}

	/**
     * Check if the player is alive
     * @return true if player is alive
     */
	public boolean isAlive() {
		return (lives() > 0);
	}

	/**
     * Player's directed position getter
     * @return Current directedPosition sequence
     */
	public Sq<DirectedPosition> directedPositions() {
		return directedPos;
	}

	/**
     * Player's position getter
     * @return SubCell occupied by the player
     */
	public SubCell position() {
		return directedPos.head().position();
	}

	/**
     * Player's direction getter
     * @return the current player's Direction
     */
	public Direction direction() {
		return directedPos.head().direction();
	}

	/**
     * Player's maximum bomb number getter
     * @return the current player's bomb maximum
     */
	public int maxBombs() {
		return maxBombs;
	}

	/**
	 * Initiate a new player with a modified Bomb maximum
	 * @param newMaxBombs
	 *          the new bomb maximum 
	 * @return A player with a new bomb maximum
	 */
	public Player withMaxBombs(int newMaxBombs) {
		return new Player(this.id, this.lifeStates, this.directedPos,
				newMaxBombs, this.bombRange);
	}

	/**
     * Player's bomb range getter
     * @return the current player's bomb range
     */
	public int bombRange() {
		return bombRange;
	}

	/**
     * Initiate a new player with a modified Bomb range
     * @param newBombRange
     *          the new bomb range
     * @return A player with a new bomb range
     */
	public Player withBombRange(int newBombRange) {
		return new Player(this.id, this.lifeStates, this.directedPos,
				this.maxBombs, newBombRange);
	}

	/**
     * The player's bomb generator ()
     * @return a new bomb
     */
	public Bomb newBomb() {
		return new Bomb(id(), position().containingCell(),
				Ticks.BOMB_FUSE_TICKS, bombRange);
	}

	/**
     * 
     * @author Amine Chaouachi (260709) / Alban Favre (260025)
     *
     */
	public static class LifeState {
	    /**
         * @author Amine Chaouachi (260709) / Alban Favre (260025)
         *
         */
		public enum State {
		    /**
             * invulnerable Sate
             */
            INVULNERABLE,
            /**
            * vulnerable Sate
            */
            VULNERABLE,
            /**
             * Dying state
             */
            DYING,
            /**
             * dead state
             */
            DEAD;
		}

		@Override
	    public String toString() {
	        return "( " +lives  +", "+  state  +")";
	    }
		int lives;
		State state;

		/**
         * LifeState constructor
         * @param lives
         *      lives number
         * @param state
         *      State
         */
		public LifeState(int lives, State state) {
			this.lives = ArgumentChecker.requireNonNegative(lives);
			this.state = Objects.requireNonNull(state);
		}

		/**
         * Check's if the player can move
         * @return true if the state is either VULNERABLE or INVULNERABLE
         */
		public boolean canMove() {
			return (state == State.VULNERABLE || state == State.INVULNERABLE);

		}

		/**
         * LifeState's life number getter
         * @return Current lives total
         */
		public int lives() {
			return lives;
		}

		/**
         * LifeState's State getter
         * @return Current State
         */
		public State state() {
			return state;
		}

	}

	 /**
     * 
     * @author Amine Chaouachi (260709) / Alban Favre (260025)
     *
     */
	public static class DirectedPosition {
		private SubCell position;
		private Direction direction;

		@Override
        public String toString() {
            return "( " +position  +", "+  direction  +")";
        }
		/**
         * DirectedPosition constructor
         * @param position
         *          The position
         * @param direction
         *          The Direction
         */
		public DirectedPosition(SubCell position, Direction direction) {

			this.direction = Objects.requireNonNull(direction);
			this.position = Objects.requireNonNull(position);
		}

		/**
         * Generate a sequence for stopping
         * @param p
         *          the directed position
         * @return the sequence of DirectedMovement of movement stop 
         */
		public static Sq<DirectedPosition> stopped(DirectedPosition p) {
			return Sq.constant(p);
		}

		/**
		 * Generate a sequence for moving
		 * @param p
		 *        the directed position
		 * @return the sequence of DirectedMovement of movement
		 */
		public static Sq<DirectedPosition> moving(DirectedPosition p) {
			return Sq.iterate(p, c -> c.withPosition(c.position().neighbor(p.direction())));
		}

		/**
		 * the position subcell getter
		 * @return the current position
		 */
		public SubCell position() {
			return position;
		}

		/**
		 * the direction getter
		 * @return the current direction
		 */
		public Direction direction() {
			return direction;
		}

		/**
		 * create a new directed position with a new position
		 * @param newPosition
		 * @return DirectedPostion of the new position
		 */
		public DirectedPosition withPosition(SubCell newPosition) {
			return new DirectedPosition(newPosition, this.direction);
		}

		/**
         * create a new directed position with a new Direction
         * @param newDirection
         * @return DirectedPostion with new direction
         */
		public DirectedPosition withDirection(Direction newDirection) {
			return new DirectedPosition(this.position, newDirection);
		}
		
		

	}
}