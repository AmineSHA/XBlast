package ch.epfl.xblast.server.debug;

import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Ticks;

public class RandomGame {
    private static Block __ = Block.FREE;
    private static Block XX = Block.INDESTRUCTIBLE_WALL;
    private static Block yy = Block.DESTRUCTIBLE_WALL;
    private static Board board = Board.ofQuadrantNWBlocksWalled(Arrays.asList(// ---------------------
            Arrays.asList(__, __, __, __, __, yy, __),
            Arrays.asList(__, XX, yy, XX, yy, XX, yy),
            Arrays.asList(__, yy, __, __, __, yy, __),
            Arrays.asList(yy, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, yy, __, yy, __, __, __),
            Arrays.asList(yy, XX, yy, XX, yy, XX, __)));
    private static List<Player> players = Arrays.asList(
            new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3),
            new Player(PlayerID.PLAYER_2, 3, new Cell(1, 11), 2, 3),
            new Player(PlayerID.PLAYER_3, 3, new Cell(13, 1), 2, 3),
            new Player(PlayerID.PLAYER_4, 3, new Cell(13, 11), 2, 3));
    private static GameState game = new GameState(board, players);
    private static RandomEventGenerator REG = new RandomEventGenerator(2016, 30,
            100);

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //while (!game.isGameOver()) {
        for(int i=0;i<420;i++){
            game = game.next(REG.randomSpeedChangeEvents(),
                    REG.randomBombDropEvents());
            GameStatePrinter.printGameState(game);
            try {
                Thread.sleep(1000 / Ticks.TICKS_PER_SECOND);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }
    }

}