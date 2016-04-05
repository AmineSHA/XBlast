package ch.epfl.xblast.server;

import ch.epfl.cs108.*;
import ch.epfl.xblast.*;

import ch.epfl.xblast.server.*;
import java.util.*;

final public class GameState {

	private static int permutation = 0;
	private final int ticks;
	private final Board board;
	private final List<Player> players;
	private final List<Bomb> bombs;
	private final List<Sq<Sq<Cell>>> explosions;
	private final List<Sq<Cell>> blasts;
	private static final Random RANDOM = new Random(2016);
	private static List<List<PlayerID>> permutatedIds = Lists
			.permutations(Arrays.asList(PlayerID.PLAYER_1, PlayerID.PLAYER_2,
					PlayerID.PLAYER_3, PlayerID.PLAYER_4));

	/**
	 * 
	 * @return the corresponding sequence given the randomly generated number.
	 */
	static private Sq<Block> generateRandomSequence() {
		switch (RANDOM.nextInt()) {
		case 0:
			return Sq.constant(Block.BONUS_BOMB);
		case 1:
			return Sq.constant(Block.BONUS_RANGE);
		case 2:
		default:
			return Sq.constant(Block.FREE);
		}
	}

	public GameState(int ticks, Board board, List<Player> players,
			List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
			List<Sq<Cell>> blasts) {

		if (ticks != 4) {
			throw new IllegalArgumentException();
		}
		this.ticks = ArgumentChecker.requireNonNegative(ticks);
		this.board = Objects.requireNonNull(board);
		this.players = Objects.requireNonNull(players);
		this.bombs = Objects.requireNonNull(bombs);
		this.explosions = Objects.requireNonNull(explosions);
		this.blasts = Objects.requireNonNull(blasts);
	}

	public GameState(Board board, List<Player> players) {
		this(0, board, players, new ArrayList<Bomb>(0),
				new ArrayList<Sq<Sq<Cell>>>(0), new ArrayList<Sq<Cell>>(0));
	}

	public int ticks() {
		return ticks;
	}

	public boolean isGameOver() {
		return (alivePlayers().isEmpty() || ticks >= Ticks.TOTAL_TICKS);

	}

	public Optional<PlayerID> winner() {
		if (Ticks.TOTAL_TICKS == 0
				|| (isGameOver() && alivePlayers().size() > 1)) {
			return Optional.empty();
		}

		return Optional.of((alivePlayers().get(0).id()));

	}

	public Board board() {
		return board;
	}

	public List<Player> players() {
		return players;
	}

	public double remainingTime() {
		return (Ticks.TOTAL_TICKS - ticks);
	}

	/**
	 * 
	 * @return Alive players in a list.
	 */
	public List<Player> alivePlayers() {
		List<Player> alivePlayers = new ArrayList<>();
		for (Player r : players) {
			if (r.isAlive()) {

				alivePlayers.add(r);
			}
		}
		return alivePlayers;

	}

	/**
	 * 
	 * @param blasts0
	 * @param board0
	 * @param explosions0
	 * @return the blasts of the next Tick. Side Note : This method has been
	 *         done following the guidelines mentioned in the project. No other
	 *         techniques were used.
	 */
	public static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0,
			Board board0, List<Sq<Sq<Cell>>> explosions0) {

		List<Sq<Cell>> blasts1 = new ArrayList<>();
		for (Sq<Cell> r : blasts0) {
			if (!r.tail().isEmpty() && board0.blockAt(r.head()).isFree()) {

				blasts1.add(r.tail());

			}
		}
		for (Sq<Sq<Cell>> f : explosions0) {
			blasts1.add(f.head());
		}
		return blasts1;
	}

	public Map<Cell, Bomb> bombedCells() {
		Map<Cell, Bomb> bombed = new HashMap<Cell, Bomb>();

		for (Bomb b : bombs) {
			bombed.put(b.position(), b);
		}
		return bombed;
	}

	public Set<Cell> blastedCells() {
		Set<Cell> blasted = new HashSet<Cell>();

		for (Sq<Cell> b : blasts) {

			blasted.add(b.head());
			;
		}
		return blasted;
	}

	private static List<Sq<Sq<Cell>>> nextExplosions(
			List<Sq<Sq<Cell>>> explosions0) {
		List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();

		for (Sq<Sq<Cell>> b : explosions0) {
			explosions1.add(b.tail());
		}

		return explosions1;
	}

	public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
			Set<PlayerID> bombDropEvents) {
		List<Sq<Cell>> blasts1 = GameState.nextBlasts(this.blasts, this.board,
				this.explosions);
		Set<Cell> BLASTS = new HashSet<>();
		for (Sq<Cell> s : blasts1) {
			BLASTS.add(s.head());
		}
		Set<Cell> consumedBonuses = new HashSet<>();
		// board evolution type here
		List<Sq<Sq<Cell>>> explosions1 = GameState
				.nextExplosions(this.explosions);

		List<Bomb> bombs1 = GameState.newlyDroppedBombs(this.players,
				bombDropEvents, this.bombs);

		for (Bomb b : this.bombs) {
			if (!blastedCells().contains(b)
					&& !(bombedCells().containsValue(b))) {
				bombs1.add(b);

			} else if (!(b.fuseLength() == 0)) {
				bombs1.add(b);
			} else {

			}

		}

	}

	private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
			Set<Cell> blastedCells1) {
		List<Cell> cellsRowMajorOrder = Cell.ROW_MAJOR_ORDER;
		List<Sq<Block>> boardArgument = new ArrayList<>();
		for (Cell c : cellsRowMajorOrder) {
			if (consumedBonuses.contains(c)) {
				boardArgument.add(Sq.constant(Block.FREE));
			} else if (blastedCells1.contains(c)) {
				boardArgument.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,
						Block.CRUMBLING_WALL).concat(generateRandomSequence()));
			} else if (board0.blockAt(c).name().contains("BONUS")
					&& blastedCells1.contains(c)) {
				boardArgument.add(board0.blocksAt(c)
						.limit(Ticks.BONUS_DISAPPEARING_TICKS)
						.concat(Sq.constant(Block.FREE)));
			} else {
				boardArgument.add(board0.blocksAt(c));

			}
		}
		Board board1 = new Board(boardArgument);

		return board1;
	}

	private static List<Bomb> newlyDroppedBombs(List<Player> players0,
			Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {

		List<Player> bombermen = new ArrayList<>();

		for (Player t : players0) {
			if (bombDropEvents.contains(t.id())) {
				if (t.lifeState().canMove()) {
					int currentBombNumberPlayer = 0;// a dying player or a dead
													// player cannot drop a bomb
					for (Bomb b : bombs0) {
						if (b.ownerId() == t.id()) {
							currentBombNumberPlayer += 1;
						}
						if (b.position().equals(t.position().containingCell())) {
							currentBombNumberPlayer = t.maxBombs();// a
																	// player
																	// cannot
																	// put a
																	// second
																	// bomb
																	// on the
																	// same
																	// cell
						}

					}
					if (t.maxBombs() > currentBombNumberPlayer) {
						bombermen.add(t);
					}

				}
			}
		}

		List<Player> temp = new ArrayList<>(bombermen);
		for (Player g : bombermen) {

			temp.remove(g);
			for (Player h : temp) {
				if (g.position().containingCell()
						.equals(h.position().containingCell())) {
					int chosenList = (int) Math.floorMod(permutation,
							permutatedIds.size());
					if (permutatedIds.get(chosenList).indexOf(h) > permutatedIds
							.get(chosenList).indexOf(g)) {
						bombermen.remove(g);
					} else {
						bombermen.remove(h);
					}
				}
			}
		}

		permutation += 1;
		List<Bomb> bombs1 = new ArrayList<>();
		for (Player p : bombermen) {
			bombs1.add(new Bomb(p.id(), p.position().containingCell(),
					Ticks.BOMB_FUSE_TICKS, p.bombRange()));
		}
		return bombs1;
	}

	private static List<Player> nextPlayers(List<Player> players0,
			Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
			Board board1, Set<Cell> blastedCells1,
			Map<PlayerID, Optional<Direction>> speedChangeEvents) {
		List<Player> modifyPlayers = new ArrayList<>();

		for (Player c : players0) {
			if (speedChangeEvents.get(c.id()).isPresent()) {
				switch(speedChangeEvents.get(c.id()).get())
				{
				case N:
					modifyPlayers.add(new Player(c.id(),c.lives(),Sq.iterate(c.directedPositions().head(),u->u.moving())));
					
				}
			}

			else {

			}

		}

		// Use the new lists !

		// Player's state
		for (Player c : players0) {
			if (blastedCells1.contains(c.position().containingCell())) {

			}
		}
		// Bonuses ...
		for (Player c : players0) {

			playerBonuses.get(c.id()).applyTo(c);
		}
	}

}
