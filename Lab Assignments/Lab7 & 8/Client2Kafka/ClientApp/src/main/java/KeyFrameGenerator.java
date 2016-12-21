import com.xuggle.xuggler.IContainer;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by harsha on 10/18/16.
 */
public class KeyFrameGenerator {
    static Video<MBFImage> video;
    //    VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
    static List<MBFImage> imageList = new ArrayList<MBFImage>();
    static List<Long> timeStamp = new ArrayList<Long>();
    static List<Double> mainPoints = new ArrayList<Double>();
    private static long frames, duration;
    private static int height, width;
    private static double fps;
    static List<String> keyFramesList = new ArrayList<String>();

    public static void Frames(String path){
        video = new XuggleVideo(new File(path));
        IContainer container = IContainer.make();
        int result = container.open("countdown.mkv", IContainer.Type.READ, null);
        duration = container.getDuration()/1000000;
        long fileSize = container.getFileSize();
//        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
        fps = video.getFPS();
//        duration = frames/fps;
        height = video.getHeight();
        width = video.getWidth();
        frames = (long)fps*duration;
        System.out.println("Total Frames: " + frames + "\nFPS: " + fps + "\nDuration: " + duration + " sec");
        System.out.println("Resolution: " + width + "*" + height + "\nFile Size: " + fileSize + " bytes");
        int j=0;
        for (MBFImage mbfImage : video) {
            BufferedImage bufferedFrame = ImageUtilities.createBufferedImageForDisplay(mbfImage);
            j++;
            String name = "output/frames/new" + j + ".jpg";
            File outputFile = new File(name);
            try {

                ImageIO.write(bufferedFrame, "jpg", outputFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            MBFImage b = mbfImage.clone();
            imageList.add(b);
            timeStamp.add(video.getTimeStamp());
        }
    }

    public static void MainFrames(){
        for (int i=0; i<imageList.size() - 1; i++)
        {
            MBFImage image1 = imageList.get(i);
            MBFImage image2 = imageList.get(i+1);
            DoGSIFTEngine engine = new DoGSIFTEngine();
            LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(image1.flatten());
            LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(image2.flatten());
            RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0, 1500,
                    new RANSAC.PercentageInliersStoppingCondition(0.5));
            LocalFeatureMatcher<Keypoint> matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                    new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);
            matcher.setModelFeatures(queryKeypoints);
            matcher.findMatches(targetKeypoints);
            double size = matcher.getMatches().size();
            mainPoints.add(size);
            System.out.println(size);
        }
        Double max = Collections.max(mainPoints);
        for(int i=0; i<mainPoints.size(); i++){
            if(((mainPoints.get(i))/max < 0.5) || i==0){
                Double name1 = mainPoints.get(i)/max;
                BufferedImage bufferedFrame = ImageUtilities.createBufferedImageForDisplay(imageList.get(i+1));
                String name = "output/mainframes/" + i + "_" + name1.toString() + ".jpg";
                File outputFile = new File(name);
                keyFramesList.add(name);
                try {
                    ImageIO.write(bufferedFrame, "jpg", outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Main frames names: ");
        for(String mainFrame: keyFramesList) {
            System.out.println(mainFrame);
        }
    }

    public static void main(String[] args) {
        String path = "countdown.mkv";
        String topic = "lab8";
//        Frames(path);
//        MainFrames();
        Producer<Integer, String> producer;
        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        properties.put("message.max.bytes", "10000000");
        producer = new Producer<Integer, String>(new ProducerConfig(properties));
        System.out.println("Key Frames Generated");
        File folder = new File("output/mainframes");
        File[] listOfFiles = folder.listFiles();
        EncodeData ed = new EncodeData();
        for(File keyFrame: listOfFiles) {
//            String msg = EncodeVideo(mainFrame.getPath());
            String msg = ed.EncodeToString(keyFrame.getPath());
            String fileName = keyFrame.getName();
            System.out.println("Simple File Name: " + fileName);
            String fileNameLength = Integer.toString(fileName.length());
            System.out.println("Simple Filename Length: " + fileNameLength);
//            msg = "*@#Harsha*@#" +  " " + fileName + " " + msg + "#@*Sri#@*";
            msg = fileName + " " + msg;
            System.out.println("File Name: " + keyFrame);
            System.out.println("Encoded String: " + msg);
            KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic, msg);//Encoding the Video
            producer.send(data);
            System.out.println("Message Sent");
        }
//            for(String keyFrame: keyFramesList) {
//            String msg = ed.EncodeToString(keyFrame);
//            System.out.println("File Name: " + keyFrame);
//            System.out.println("Encoded String: " + msg);
//        }



        producer.close();
    }
}
