package ch.epfl.xblast.server;

import ch.epfl.cs108.*;
import ch.epfl.xblast.*;

import ch.epfl.xblast.server.*;
import java.util.*;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
final public class GameState {

    /**
     * the permutation position, it define which permutation will be used in
     * private static List<List<PlayerID>> permutatedIds
     */
    private static int permutation = 0;
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    private static final Random RANDOM = new Random(2016);
    /**
     * the list of all possible permutation of PlayerIDs
     */
    private static List<List<PlayerID>> permutatedIds = Lists
            .permutations(Arrays.asList(PlayerID.PLAYER_1, PlayerID.PLAYER_2,
                    PlayerID.PLAYER_3, PlayerID.PLAYER_4));

    /**
     * Generate a random block sequence 1/3 chance to get a bomb bonus block
     * sequence, a range bonus block sequence or no bonus at all
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

    /**
     * the GameState's principal constructor
     * 
     * @param ticks
     *            numbers of ticks
     * @param board
     *            The Board
     * @param players
     *            list of all players
     * @param bombs
     *            list of all bombs
     * @param explosions
     *            the explosions in List<Sq<Sq<Cell>>> form
     * @param blasts
     *            lists of all sequences of cell where an explosion particules
     *            is located
     */
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

    /**
     * the GameState's secondary constructor (for the game's start)
     * 
     * @param board
     *            The initial Board
     * @param players
     *            list of all initial players
     */
    public GameState(Board board, List<Player> players) {
        this(0, board, players, new ArrayList<Bomb>(0),
                new ArrayList<Sq<Sq<Cell>>>(0), new ArrayList<Sq<Cell>>(0));
    }

    /**
     * the ticks getter
     * 
     * @return the GameState ticks value
     */
    public int ticks() {
        return ticks;
    }

    /**
     * checks if the game is over
     * 
     * @return true if the game's over, false if not
     */
    public boolean isGameOver() {
        return (alivePlayers().isEmpty() || ticks >= Ticks.TOTAL_TICKS);

    }

    /**
     * returns the current winner's PlayerID if a winner exists, if not, the
     * returned container will be empty
     * 
     * @return the Optional container of the winner's PlayerID
     */
    public Optional<PlayerID> winner() {
        if (Ticks.TOTAL_TICKS == 0
                || (isGameOver() && alivePlayers().size() > 1)) {
            return Optional.empty();
        }

        return Optional.of((alivePlayers().get(0).id()));

    }

    /**
     * the board getter
     * 
     * @return the current board
     */
    public Board board() {
        return board;
    }

    /**
     * the players getter
     * 
     * @return the current players list
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Compute remaining time
     * 
     * @return the remaining time
     */
    public double remainingTime() {
        return (Ticks.TOTAL_TICKS - ticks);
    }

    /**
     * the living players getter
     * 
     * @return the current alive players list
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
     * Create all the blasts needed for the next tick Side Note : This method
     * has been done following the guidelines mentioned in the project. No other
     * techniques were used.
     * 
     * @param blasts0
     *            The current blasts
     * @param board0
     *            the current board
     * @param explosions0
     *            the current explosion
     * @return the blasts of the next Tick.
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

    /**
     * associate a bomb and a Cell (which is returned)
     * 
     * @return the map of the cells and the attached bomb
     */
    public Map<Cell, Bomb> bombedCells() {
        Map<Cell, Bomb> bombed = new HashMap<Cell, Bomb>();

        for (Bomb b : bombs) {
            bombed.put(b.position(), b);
        }
        return bombed;
    }

    /**
     * associate a blast sequence and a Cell (which is returned)
     * 
     * @return the map of the cells and the attached Blast set
     */
    public Set<Cell> blastedCells() {
        Set<Cell> blasted = new HashSet<Cell>();

        for (Sq<Cell> b : blasts) {

            blasted.add(b.head());

        }
        return blasted;
    }

    /**
     * Create all the explosions needed for the next tick
     * 
     * @param explosions0
     *            All explosions of the current tick
     * @return the explosions for the next tick
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();

        for (Sq<Sq<Cell>> b : explosions0) {
            explosions1.add(b.tail());
        }

        return explosions1;
    }

    /**
     * Create the next tick GameState
     * 
     * @param speedChangeEvents
     *            Player's direction change
     * @param bombDropEvents
     *            the PlayerID list of player who want to drop a bomb
     * @return the next Gamestate
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {
        
        
        
        List<Sq<Cell>> blasts1 = GameState.nextBlasts(this.blasts, this.board,
                this.explosions);

        Map<PlayerID, Bonus> playerBonuses1 = new HashMap<>();//this is used for nextplayer
        
        Set<Cell> consumedBonuses = new HashSet<>();
        for (Player p : players) {
            SubCell sub=p.position();
            if(sub.isCentral()&&board.blockAt(sub.containingCell()).isBonus()){
                consumedBonuses.add(sub.containingCell());
                playerBonuses1.put(p.id(), board.blockAt(sub.containingCell()).associatedBonus());
            }
        }
        
        Board board1 = nextBoard(this.board, consumedBonuses, blastedCells());

        
        
        
        List<Sq<Sq<Cell>>> explosions1 = GameState
                .nextExplosions(this.explosions);

        List<Bomb> bombs1 = GameState.newlyDroppedBombs(this.players,
                bombDropEvents, this.bombs);

        for (Bomb b : this.bombs) {
            if (!(b.fuseLength() == 0)) {// TODO si sa marche pas c'est ici le
                                         // prob (== =<)
                bombs1.add(b);
            }

            else if (!blastedCells().contains(b)) {
                bombs1.add(b);

            } else {
                explosions1.addAll(b.explosion());
            }

        }
        
        
        
//        List<Player> nextPlayers(List<Player> players0,
//                Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
//                Board board1, Set<Cell> blastedCells1,
//                Map<PlayerID, Optional<Direction>> speedChangeEvents)

        Set<Cell> bombedCells1Set = new HashSet<>();
        Map<Cell, Bomb> bombedCells1Map= bombedCells();
        for (Cell z : bombedCells1Map.keySet()) {
            bombedCells1Set.add(z);
        }
        
        
        List<Player> Player1 = nextPlayers(this.players, playerBonuses1, bombedCells1Set, board1, blastedCells(), speedChangeEvents);
        
        
        return new GameState(this.ticks+1, board1, Player1, bombs1, explosions1, blasts1);

        
    }

    /**
     * Create the next Board needed for the next tick
     * 
     * @param board0
     *            Current board
     * @param consumedBonuses
     *            Bonuses which have been consumed this tick
     * @param blastedCells1
     *            The cells that will be blasted next tick
     * @return the board for the next tick
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {
        List<Cell> cellsRowMajorOrder = Cell.ROW_MAJOR_ORDER;
        List<Sq<Block>> boardArgument = new ArrayList<>();
        for (Cell c : cellsRowMajorOrder) {
            if (consumedBonuses.contains(c)) {
                boardArgument.add(Sq.constant(Block.FREE));
            } else if (blastedCells1.contains(c)
                    && board0.blockAt(c) == Block.DESTRUCTIBLE_WALL) {
                boardArgument.add(Sq
                        .repeat(Ticks.WALL_CRUMBLING_TICKS,
                                Block.CRUMBLING_WALL)
                        .concat(generateRandomSequence()));
            } else if (board0.blockAt(c).name().contains("BONUS")
                    && blastedCells1.contains(c)) {
                boardArgument.add(
                        board0.blocksAt(c).limit(Ticks.BONUS_DISAPPEARING_TICKS)
                                .concat(Sq.constant(Block.FREE)));
            } else {
                boardArgument.add(board0.blocksAt(c));

            }
        }
        Board board1 = new Board(boardArgument);

        return board1;
    }

    /**
     * the method which will define which bomb can actually be dropped out of
     * all players that request a bomb drop
     * 
     * @param players0
     *            the current players
     * @param bombDropEvents
     *            the PlayerID list of player who want to drop a bomb
     * @param bombs0
     *            all current bombs on the board
     * @return the bombs that can be dropped
     */
    public static List<Bomb> newlyDroppedBombs(List<Player> players0,
            Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
        List<Player> bombermen = new ArrayList<>();

        for (Player t : players0) {
            if (bombDropEvents.contains(t.id())) {
                if (t.lifeState().canMove()) {
                    int currentBombNumberPlayer = 0;/*
                                                     * a dying player or a dead
                                                     * player cannot drop bomb
                                                     */
                    for (Bomb b : bombs0) {
                        if (b.ownerId() == t.id()) {
                            currentBombNumberPlayer += 1;
                        }
                        if (b.position()
                                .equals(t.position().containingCell())) {
                            currentBombNumberPlayer = t
                                    .maxBombs();/*
                                                 * a player cannot put a second
                                                 * bomb on the same cell
                                                 */
                        }

                    }
                    if (t.maxBombs() > currentBombNumberPlayer) {
                        bombermen.add(t);
                    }

                }
            }
        }

        List<Player> temp = new ArrayList<>(bombermen);
        List<Player> endBombermen = new ArrayList<>(bombermen);
        for (Player g : bombermen) {

            temp.remove(g);
            for (Player h : temp) {
                if (g.position().containingCell()
                        .equals(h.position().containingCell())) {
                    int chosenIndex = (int) Math.floorMod(permutation,
                            permutatedIds.size());
                    if (permutatedIds.get(chosenIndex)
                            .indexOf(h.id()) < permutatedIds.get(chosenIndex)
                                    .indexOf(g.id())) {

                        endBombermen.remove(g);
                    } else {

                        endBombermen.remove(h);
                    }
                }
            }
        }

        permutation += 1;
        List<Bomb> bombs1 = new ArrayList<>();
        for (Player p : endBombermen) {
            bombs1.add(new Bomb(p.id(), p.position().containingCell(),
                    Ticks.BOMB_FUSE_TICKS, p.bombRange()));
        }

        return bombs1;
    }

    /**
     * Create the players for the next tick
     * 
     * @param players0
     *            current players
     * @param playerBonuses
     *            set of player and their bonuses
     * @param bombedCells1
     *            the cell that will be bombed next tick
     * @param board1
     *            the board of the next tick
     * @param blastedCells1
     *            the cells that will be blasted next tick
     * @param speedChangeEvents
     *            Player's direction change
     * @return the list of players of the next tick
     */
    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {
        List<Player> modifyPlayers = new ArrayList<>();

        for (Player c : players0) {
            if (speedChangeEvents.get(c.id()).isPresent()) {
                switch (speedChangeEvents.get(c.id()).get()) {
                case N:
                    modifyPlayers.add(new Player(c.id(), c.lives(), Sq.iterate(
                            c.directedPositions().head(), u -> u.moving())));

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
