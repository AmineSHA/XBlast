package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import javax.imageio.ImageIO;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class ImageCollection {

    private final Map<Integer, Image> imageMap = new HashMap<>();

    /**
     * player collection
     */
    static public final ImageCollection playerCollection = new ImageCollection(
            "player");
    /**
     * explosions and bombs collection
     */
    static public final ImageCollection explosionsBombsCollection = new ImageCollection(
            "explosion");
    /**
     * Board Collection
     */
    static public final ImageCollection BoardCollection = new ImageCollection(
            "block");
    /**
     * time and score collection
     */
    static public final ImageCollection timeAndScoreCollection = new ImageCollection(
            "score");

    /**
     * A vital method for printing image
     * 
     * @param name
     *            the folder name
     */
    public ImageCollection(String name) {
        String dirName = name; // p.ex. "player"
        try {
            File dir = new File(ImageCollection.class.getClassLoader()
                    .getResource(dirName).toURI());

            for (File f : dir.listFiles()) {
                try {
                    imageMap.put(Integer.parseInt(f.getName().substring(0, 3)),
                            ImageIO.read(f));
                }

                catch (NumberFormatException | IOException e) {
                    e.printStackTrace();
                }
            }

        }

        catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * An image getter (but will not work with invalid sprite)
     * 
     * @param index
     *            index
     * @return image at index index
     */
    public Image image(int index) {
        if (!imageMap.containsKey(index))
            throw new NoSuchElementException();
        return imageMap.get(index);
    }

    /**
     * An image getter (that work with invalid sprite) (if sprite is invalid,
     * the image will not be, so it looks like there is nothing)
     * 
     * @param index
     *            index
     * @return image at index index
     */
    public Image imageOrNull(int index) {
        if (!imageMap.containsKey(index))
            return null;
        return imageMap.get(index);
    }

}