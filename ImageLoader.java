import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lukado on 23. 11. 2016.
 */

class ImageLoader {
    /**
     * Načte obrázek od objektu
     * @param fileLocation umístění
     * @return obrázek
     */
    public BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * Zapíše obrázek na disk
     * @param img obrázek
     * @param fileLocation umístění
     * @param extension přípona
     */
    public void writeImage(BufferedImage img, String fileLocation,
                    String extension) {
        try {
            File outfile = new File(fileLocation);
            if (!outfile.exists()) new File("img/").mkdir();
            ImageIO.write(img, extension, outfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
