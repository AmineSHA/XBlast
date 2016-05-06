package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.*;

import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class GameState {

    private final List<Player> players;
    private final List<Image> boardImages;
    private final List<Image> bombsAndExplosionsImages;
    private final List<Image> scoreLine;
    private final List<Image> timeLine;

    public GameState(List<Player> players, List<Image> boardImages,
            List<Image> bombsAndExplosionsImages, List<Image> scoreLine,
            List<Image> timeLine) {

        this.players=new ArrayList<>(Objects.requireNonNull(players));
        this.boardImages=new ArrayList<>(Objects.requireNonNull(boardImages));
        this.bombsAndExplosionsImages=new ArrayList<>(bombsAndExplosionsImages);
        this.scoreLine=new ArrayList<>(Objects.requireNonNull(scoreLine));
        this.timeLine=new ArrayList<>(Objects.requireNonNull(timeLine));
    }

    public static final class Player {
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;

        public Player(PlayerID id, int lives, SubCell position, Image image) {
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;

        }

        @Override
        public String toString() {
            return id + " " + lives + " " + position + " ("
                    + image.getWidth(null) + " " + image.getHeight(null) + ")";
        }

    }
}