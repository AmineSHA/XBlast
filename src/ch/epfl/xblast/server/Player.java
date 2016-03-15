package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.ArgumentChecker;

public class Player {

	PlayerID id;
	private Sq<LifeState> lifeStates;
	Sq<DirectedPosition> directedPos;
	int maxBombs;
	int bombRange;

	Player(PlayerID id, Sq<LifeState> lifeStates,
			Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) {
		ArgumentChecker.requireNonNegative(maxBombs);
		ArgumentChecker.requireNonNegative(bombRange);
		Objects.requireNonNull(id);
		Objects.requireNonNull(lifeStates);
		Objects.requireNonNull(directedPos);
		this.id = id;
		this.lifeStates = lifeStates;
		this.directedPos = directedPos;
		this.maxBombs = maxBombs;
		this.bombRange = bombRange;

	}

	Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) {
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

	Sq<LifeState> lifeStates() {
		return lifeStates;
	}

	private Sq<LifeState> constructLifeStateSequence(int lives) {
		Sq<LifeState> dyingBasisSequence = Sq.repeat(Ticks.PLAYER_DYING_TICKS,
				new LifeState(lives, LifeState.State.DYING));
		
		if (!isAlive()) {
			return dyingBasisSequence.concat(Sq.constant(new LifeState(0, LifeState.State.DEAD)));
		} else {
			return dyingBasisSequence.concat(Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
					new LifeState(lives -1, LifeState.State.INVULNERABLE)).concat(
					Sq.constant(new LifeState(lives - 1,
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

	int maxBombs() {
		return maxBombs;
	}

	public Player withMaxBombs(int newMaxBombs) {
		return new Player(
				id,
				Sq.constant(new LifeState(0, LifeState.State.DEAD)),
				Sq.constant(new DirectedPosition(new SubCell(0, 0), Direction.S)),
				0, 0);
	}

	public int bombRange() {
		return bombRange;
	}

	public Player withBombRange(int newBombRange) {
		return new Player(
				id,
				Sq.constant(new LifeState(0, LifeState.State.DEAD)),
				Sq.constant(new DirectedPosition(new SubCell(0, 0), Direction.S)),
				0, 0);
	}

	public Bomb newBomb() {
		return new Bomb(id(), position().containingCell(),
				Ticks.BOMB_FUSE_TICKS, bombRange);
	}

	static class LifeState {
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
			if (state == State.VULNERABLE || state == State.INVULNERABLE) {
				return true;
			}
			return false;
		}

		public int lives() {
			return lives;
		}

		public State state() {
			return state;
		}

	}

	static class DirectedPosition {
		private SubCell position;
		private Direction direction;

		public DirectedPosition(SubCell position, Direction direction) {

			if (position == null || direction == null) {
				throw new NullPointerException();
			}

			this.direction = direction;
			this.position = position;
		}

		Sq<DirectedPosition> stopped(DirectedPosition p) {
			return Sq.constant(p);
		}

		Sq<DirectedPosition> moving(DirectedPosition p) {
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