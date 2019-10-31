package resource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Resources {

    private static class ResourcesHolder {
        private static final Resources INSTANCE = new Resources();
    }

    private Resources() {
    }

    public static final Resources getInstance() {
        return ResourcesHolder.INSTANCE;
    }

    public Icon getResourceIcon(String path) {

        return createIcon(createImage(path), 32, 32);
    }

    public Icon getGifIcon(String path) {

        return new ImageIcon(getClass().getResource(path));

    }

    /**
     * It is for the image icon without the description.
     * <p>
     * Create an icon from an original image, which has normally a bigger size.
     *
     * @param image  - the original image to be converted to icon
     * @param width  - the created icon width
     * @param height - the created icon height
     * @return an Icon object
     */
    public Icon createIcon(Image image, int width, int height) {

        BufferedImage iconImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = iconImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return new ImageIcon(iconImage);
    }

    /**
     * @param path - the path used to create the buffered image.
     * @return an BufferedImage object, or <code>null</code> if the given path
     * is not valid or an error occurs during reading.
     */
    public BufferedImage createImage(String path) {

        URL imageURL = Resources.class.getResource(path);
        if (imageURL != null) {
            try {
                return ImageIO.read(imageURL);
            } catch (IOException e) {

                System.err.println("an error occurs during reading.");
                e.printStackTrace();
                return null;
            }
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }




}
