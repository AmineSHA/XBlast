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

	public static final int MAX_TIME = 60;

	private GameStateDeserializer() {
	}

	public static GameState deserializeGameState(List<Byte> encoded) {

		int boardListMark = encoded.get(0);
		int splosionsListMark = encoded.get(boardListMark) + boardListMark + 1;
		int playerPortionSize = 17;
		int time = encoded.get(encoded.size() - 1);

		List<Byte> encodedCopy = new ArrayList<>(encoded);

		// getting the image list of the board, the explosions, and the players
		List<Image> boardImage = deserializeBoardAndRowMajoredIt(encodedCopy
				.subList(1, boardListMark));
		List<Image> splosions = deserializeExplosions(encodedCopy.subList(
				boardListMark + 1, splosionsListMark));
		List<Player> players = deserializePlayers(encodedCopy.subList(
				splosionsListMark + 1, splosionsListMark + playerPortionSize));

		// scoreLine later ... ScoreLine !
		ImageCollection timeAndScoreCollection = new ImageCollection("score");
		List<Image> scoreLine = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			if (i == 2) {
				scoreLine.add(timeAndScoreCollection.image(12));
			}
			if (players.get(i).lives() > 0)
				scoreLine.add(timeAndScoreCollection.image((i + 1) * 2 - 2));
			else
				scoreLine.add(timeAndScoreCollection.image((i + 1) * 2 - 1));

			scoreLine.add((timeAndScoreCollection.image(10)));
			scoreLine.add((timeAndScoreCollection.image(11)));
		}

		// timeLine !
		List<Image> timeLine = new ArrayList<>();
		Image full = timeAndScoreCollection.image(21);
		Image empty = timeAndScoreCollection.image(20);
		for (int i = 0; i < time; i++) {
			timeLine.add(full);
		}
		for (int i = 0; i < MAX_TIME - time; i++) {
			timeLine.add(empty);
		}

		return new GameState(players, boardImage, splosions, scoreLine,
				timeLine);

	}

	public static List<Image> deserializeBoardAndRowMajoredIt(
			List<Byte> encodedBoard) {

		ImageCollection BoardCollection = new ImageCollection("block");
		Image board[] = new Image[Cell.COUNT];

		for (Cell c : Cell.SPIRAL_ORDER)
			board[c.rowMajorIndex()] = BoardCollection.image(encodedBoard
					.get(Cell.SPIRAL_ORDER.indexOf(c)));

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

		// TODO la partie dessous est surtout utile pour les tests
		if (encodedPlayers.size() != PlayerID.values().length * 4)
			throw new IllegalArgumentException();

		List<Byte> temp = new ArrayList<>(encodedPlayers);

		ImageCollection playerCollection = new ImageCollection("player");
		List<Player> playerList = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new Player(PlayerID.values()[i], Byte
					.toUnsignedInt(temp.get(0)), new SubCell(Byte
					.toUnsignedInt(temp.get(1)),
					Byte.toUnsignedInt(temp.get(2))), playerCollection
					.image(temp.get(3))));
			temp = temp.subList(4, temp.size());

		}
		return playerList;
	}

}