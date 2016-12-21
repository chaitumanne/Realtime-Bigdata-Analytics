/**
 * Created by Harsha on 9/13/2016.
 */

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class FindingClarifaiTags {
    private static String APP_ID = "LgP1VUXQOb7uNF2rDlQwZHEmobXiNVFUD0VgaZj_";
    private static String APP_SECRET = "oqXE8_-5t6G03s128g4Il63GqPofFbqzX8VX1sd-";

    public static void main(String args[]) throws IOException, InterruptedException {
        FindingClarifaiTags t = new FindingClarifaiTags();
        t.getTags();
    }

    public static void getTags() throws IOException, InterruptedException {
        ClarifaiClient clarifai = new ClarifaiClient(APP_ID, APP_SECRET);
        List<RecognitionResult> results = null;
        //int i=0;

        File path = new File("output/mainframes");

        File[] files = path.listFiles();



        for (int i = 0; i < files.length; i++) {
            results = clarifai.recognize(new RecognitionRequest(files[i]));
            String tags = "";
            int counter = 0;
            for (Tag tag : results.get(0).getTags()) {
                if(counter>3) break;
                System.out.println(tag.getName() + ": " + tag.getProbability());

                if (tag.getProbability()> 0.95 && tag.getProbability()< 0.99) {
                    if(tags!="") {
                        tags = tags + ", " + tag.getName();
                    }
                    else tags = tag.getName();
                    counter++;
                    MBFImage image = ImageUtilities.readMBF(files[i]);

                    image.drawText(tags, 20, 50, HersheyFont.ASTROLOGY, 20, RGBColour.ORANGE);

                    DisplayUtilities.displayName(image, "Short Video");


                }

            }
        }

/*
            results = clarifai.recognize(new RecognitionRequest(files));
        for (int i = 0; i < files.length; i++) {
            for (Tag tag : results.get(i).getTags()) {
                System.out.println(tag.getName() + ": " + tag.getProbability());
                /*MBFImage image = ImageUtilities.readMBF(files[i]);

                image.drawText(tag.getName(), 90, 150, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);

                DisplayUtilities.displayName(image, "videoFrames");*/
                //List l1 = tag.getName();
/*
                String name = "output/Tags/" + i + ".jpg";
                File outputFile = new File(name);
                try {
                    ImageIO.write(, "jpg", outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
*/
    }
}

