package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import javax.imageio.ImageIO;

public final class ImageCollection {
    
    private final Map<Integer, Image> imageMap = new HashMap<>();

    public ImageCollection(String name) {
        String dirName = name; // p.ex. "player"
        try {
            File dir = new File(ImageCollection.class.getClassLoader()
                    .getResource(dirName).toURI());

            for (File f : dir.listFiles()) {
                try {
                    imageMap.put(Integer.parseInt(f.getName().substring(0, 3)),
                            ImageIO.read(f));
                } catch (NumberFormatException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Image image(int index) {
        if (!imageMap.containsKey(index))
            throw new NoSuchElementException();
        return imageMap.get(index);
    }

    public Image imageOrNull(int index) {
        if (!imageMap.containsKey(index))
            return null;
        return imageMap.get(index);
    }

}