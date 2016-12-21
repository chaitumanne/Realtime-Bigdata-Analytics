import org.openimaj.image.MBFImage;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

/**
 * Created by Naga on 06-09-2016.
 */
public class WebCam {
    public static void main(String args[]) throws VideoCaptureException {
        final Video<MBFImage> video = new VideoCapture(320, 240);
        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
    }
}
