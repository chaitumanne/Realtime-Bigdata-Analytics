import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;

import java.io.File;
import java.io.IOException;

/**
 * Created by Naga on 07-09-2016.
 */
public class VideoReConsc {

    public static void main(String[] args) throws IOException, InterruptedException {

        File path = new File("output/mainframes");

        File[] files = path.listFiles();

        for (int i = 0; i < files.length; i++){

            MBFImage image = ImageUtilities.readMBF(files[i]);

            image.drawText("LifeWild ", 90, 150, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);

            DisplayUtilities.displayName(image, "videoFrames");
        }
    }
}