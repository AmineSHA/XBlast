package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.ArgumentChecker;

final public class Player {

	private final PlayerID id;
	private final Sq<LifeState> lifeStates;
	private final Sq<DirectedPosition> directedPos;
	private final int maxBombs;
	private final int bombRange;

	public Player(PlayerID id, Sq<LifeState> lifeStates,
			Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) {

		this.id = Objects.requireNonNull(id);
		this.lifeStates = Objects.requireNonNull(lifeStates);
		this.directedPos = Objects.requireNonNull(directedPos);
		this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
		this.bombRange = ArgumentChecker.requireNonNegative(bombRange);

	}

	public Player(PlayerID id, int lives, Cell position, int maxBombs,
			int bombRange) {
		this(id, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
				new LifeState(lives, LifeState.State.INVULNERABLE)).concat(
				Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE))),
				Sq.constant(new DirectedPosition(SubCell
						.centralSubCellOf(position), Direction.S)), maxBombs,
				bombRange);

	}

	public PlayerID id() {
		return id;
	}

	public Sq<LifeState> lifeStates() {
		return lifeStates;
	}

	public LifeState lifeState() {
		return lifeStates().head();
	}
/**
 * Basically is the method statesForNextLife I just thought it would be clearer to put this piece of code in another method
 * 
 * @param lives
 * @returns à  sequence of LifeStates.
 */
	private Sq<LifeState> constructLifeStateSequence(int lives) {   
		Sq<LifeState> dyingBasisSequence = Sq.repeat(Ticks.PLAYER_DYING_TICKS,
				new LifeState(lives, LifeState.State.DYING));

		if (!isAlive()) {
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

	public Sq<LifeState> statesForNextLife() {

		return constructLifeStateSequence(lifeStates.head().lives());

	}

	public int lives() {
		return lifeStates.head().lives();
	}

	public boolean isAlive() {
		return (lives() > 0);
	}

	public Sq<DirectedPosition> directedPositions() {
		return directedPos;
	}

	public SubCell position() {
		return directedPos.head().position();
	}

	public Direction direction() {
		return directedPos.head().direction();
	}

	public int maxBombs() {
		return maxBombs;
	}

	public Player withMaxBombs(int newMaxBombs) {
		return new Player(this.id, this.lifeStates, this.directedPos,
				newMaxBombs, this.bombRange);
	}

	public int bombRange() {
		return bombRange;
	}

	public Player withBombRange(int newBombRange) {
		return new Player(this.id, this.lifeStates, this.directedPos,
				this.maxBombs, newBombRange);
	}

	public Bomb newBomb() {
		return new Bomb(id(), position().containingCell(),
				Ticks.BOMB_FUSE_TICKS, bombRange);
	}

	public static class LifeState {
		public enum State {
			INVULNERABLE, VULNERABLE, DYING, DEAD;
		}

		int lives;
		State state;

		public LifeState(int lives, State state) {
			this.lives = lives;
			this.state = state;
		}

		public boolean canMove() {
			return (state == State.VULNERABLE || state == State.INVULNERABLE);

		}

		public int lives() {
			return lives;
		}

		public State state() {
			return state;
		}

	}

	public static class DirectedPosition {
		private SubCell position;
		private Direction direction;

		public DirectedPosition(SubCell position, Direction direction) {

			this.direction = Objects.requireNonNull(direction);
			this.position = Objects.requireNonNull(position);
		}

		public static Sq<DirectedPosition> stopped(DirectedPosition p) {
			return Sq.constant(p);
		}

		public static Sq<DirectedPosition> moving(DirectedPosition p) {
			return Sq.iterate(p, c -> {
				return new DirectedPosition(c.position.neighbor((c.direction)),
						p.direction);
			});
		}

		public SubCell position() {
			return position;
		}

		public Direction direction() {
			return direction;
		}

		public DirectedPosition withPosition(SubCell newPosition) {
			return new DirectedPosition(newPosition, this.direction);
		}

		public DirectedPosition withDirection(Direction newDirection) {
			return new DirectedPosition(this.position, newDirection);
		}

	}
}