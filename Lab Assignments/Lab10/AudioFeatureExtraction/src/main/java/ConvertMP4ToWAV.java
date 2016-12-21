import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;

/**
 * Created by harsha on 11/9/16.
 */
public class ConvertMP4ToWAV {

    public void convertToAudio(File video){
        System.out.println("Conversion is Started");
        File audioOutput = new File("data/" + video.getName().substring(0, video.getName().lastIndexOf(".")) + ".wav");
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s24le");
        audio.setBitRate(new Integer(128000));
        audio.setChannels(2);
        audio.setSamplingRate(new Integer(44100));

        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setFormat("wav");
        attributes.setAudioAttributes(audio);
        Encoder encoder =new Encoder();
        try {
            encoder.encode(video,audioOutput,attributes);
            System.out.println("Conversion is Finished");
        }
        catch (Exception e) {System.err.println(e);}
    }
}
