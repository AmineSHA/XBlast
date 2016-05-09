package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class GameStateDeserializer {

	private static final int MAX_TIME = 60;
	private static final int NUMBER_OF_BYTE_PER_PLAYER=4;
	
	private static final int DEAD_PLAYER_SCORE_SPRITE=0;
	private static final int ALIVE_PLAYER_SCORE_SPRITE=1;
	private static final int MIDDLE_TILE_SPRITE=10;
	private static final int RIGHT_TILE_SPRITE=11;
	private static final int VOID_TILE_SPRITE=12;
	private static final int OFF_LED_TIME_SPRITE=20;
	private static final int ON_LED_TIME_SPRITE=21;

	private GameStateDeserializer() {
	}

	public static GameState deserializeGameState(List<Byte> encoded) {

		int boardListMark = encoded.get(0)+1;
		int splosionsListMark = encoded.get(boardListMark) + boardListMark + 1;
		int playerPortionSize = PlayerID.values().length*NUMBER_OF_BYTE_PER_PLAYER;
		int time = encoded.get(encoded.size() - 1);

		List<Byte> encodedCopy = new ArrayList<>(encoded);

		// getting the image list of the board, the explosions, and the players
		List<Image> boardImage = deserializeBoardAndRowMajoredIt(RunLengthEncoder.decode(encodedCopy
				.subList(1, boardListMark)));
		List<Image> splosions = deserializeExplosions(RunLengthEncoder.decode(encodedCopy.subList(
				boardListMark + 1, splosionsListMark)));
		List<Player> players = deserializePlayers(encodedCopy.subList(
				splosionsListMark, splosionsListMark + playerPortionSize));

		
		ImageCollection timeAndScoreCollection = new ImageCollection("score");
		List<Image> scoreLine = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
		    //playerValue is the correct sprite group in image.score
		    int playerValue=i*2;
			if (i == 2) {
			    for (int j = 0; j < 6; j++) 
			        scoreLine.add(timeAndScoreCollection.image(VOID_TILE_SPRITE));
                
				
			}
			if (players.get(i).lives() > 0)
				scoreLine.add(timeAndScoreCollection.image(playerValue+ALIVE_PLAYER_SCORE_SPRITE));
			else
				scoreLine.add(timeAndScoreCollection.image(playerValue+DEAD_PLAYER_SCORE_SPRITE));

			scoreLine.add((timeAndScoreCollection.image(MIDDLE_TILE_SPRITE)));
			scoreLine.add((timeAndScoreCollection.image(RIGHT_TILE_SPRITE)));
		}

		// timeLine 
		List<Image> timeLine = new ArrayList<>();
		Image full = timeAndScoreCollection.image(ON_LED_TIME_SPRITE);
		Image empty = timeAndScoreCollection.image(OFF_LED_TIME_SPRITE);
		for (int i = 0; i < time; i++) {
			timeLine.add(full);
		}
		for (int i = 0; i < MAX_TIME - time; i++) {
			timeLine.add(empty);
		}

		return new GameState(players, boardImage, splosions, scoreLine,
				timeLine);

	}

	private static List<Image> deserializeBoardAndRowMajoredIt(
			List<Byte> decodedBoard) {

		ImageCollection BoardCollection = new ImageCollection("block");
		Image board[] = new Image[Cell.COUNT];

		for (int i =0; i < Cell.COUNT;i++){
			board[i] = BoardCollection.image(decodedBoard
					.get(Cell.SPIRAL_ORDER.get(i).rowMajorIndex()));
		}

		return new ArrayList<Image>(Arrays.asList(board));

	}

	private static List<Image> deserializeExplosions(
			List<Byte> encodedExplosionsBombs) {

		ImageCollection explosionsBombsCollection = new ImageCollection(
				"explosion");
		List<Image> explosionBomb = new ArrayList<>();

		for (Byte b : encodedExplosionsBombs)
			explosionBomb.add(explosionsBombsCollection.imageOrNull(b));

		return explosionBomb;

	}

	// methode testee(pour la re tester il faut la passer en private)
	private static List<Player> deserializePlayers(List<Byte> encodedPlayers) {


		List<Byte> temp = new ArrayList<>(encodedPlayers);

		ImageCollection playerCollection = new ImageCollection("player");
		List<Player> playerList = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new Player(PlayerID.values()[i], Byte
					.toUnsignedInt(temp.get(0)), new SubCell(Byte
					.toUnsignedInt(temp.get(1)),
					Byte.toUnsignedInt(temp.get(2))), playerCollection
					.imageOrNull(temp.get(3))));
			temp = temp.subList(4, temp.size());

		}
		return playerList;
	}

}