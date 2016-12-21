import jAudioFeatureExtractor.AudioFeatures.*;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;
import scala.Array;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sri Harsha Chennavajjala on 02 Nov 2016.
 */
public class AC {
    public static Map<String, Integer> AFF = new HashMap<String, Integer>()
    {{
        put("Spectral_Centroid", 1);
        put("Spectral_Rolloff_Point", 2);
        put("Spectral_Flux", 3);
        put("Compactness", 4);
        put("Spectral_Variability", 5);
        put("Root_Mean_Square", 6);
        put("Fration_of_Low_Energy_Windows", 7);
        put("Zero_Crossings",8);
        put("Strongest_Beat", 9);
        put("Beat_Sum", 10);
        put("MFCC", 11);
        put("ConstantQ", 12);
        put("LPC", 13);
        put("Method_of_Moments", 14);
        put("Peak_Detection", 15);
        put("Area_Method_of_MFCCs", 16);
    }};
//    public static Array otherFeatures = Array.ofDim(1000, 1000, Double);
//    public enum AFF {
//        Spectral_Centroid(1),
//        Spectral_Rolloff_Point(2),
//        Spectral_Flux(3),
//        Compactness(4),
//        Spectral_Variability(5),
//        Root_Mean_Square(6),
//        Fration_of_Low_Energy_Windows(7),
//        Zero_Crossings(8),
//        Strongest_Beat(9),
//        Beat_Sum(10),
//        MFCC(11),
//        ConstantQ(12),
//        LPC(13),
//        Method_of_Moments(14),
//        Peak_Detection(15),
//        Area_Method_of_MFCCs(16);
//
//        private final int value;
//
//        AFF(int value) {
//
//            this.value = value;
//        }
//
//        public int getValue() {
//            return value;
//        }
//
//    }


    public static double[] feature(AudioSamples audio, int i) throws Exception {
        /**
         * 1. Spectral Centroid
         * 2. Spectral Rolloff Point
         * 3. Spectral Flux
         * 4. Compactness
         * 5. Spectral Variability
         * 6. Root Mean Square
         * 7. Fration of Low Energy Windows
         * 8. Zero Crossings
         * 9. Strongest Beat
         * 10. Beat Sum
         * 11. MFCC
         * 12. ConstantQ
         * 13. LPC
         * 14. Method of Moments
         * 15. Peak Detection
         * 16. Area Method of MFCCs
         *
         */
        FeatureExtractor featureExt;
        double[] samples = audio.getSamplesMixedDown();
        double sampleRate = audio.getSamplingRateAsDouble();
        double[][] otherFeatures = new double[1000][1000];
        double[][] windowSample;
        switch (i) {
            case 1:
                featureExt = new SpectralCentroid();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 2:
                featureExt = new PowerSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new SpectralRolloffPoint();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 3:
                windowSample = audio.getSampleWindowsMixedDown(3);
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(windowSample[0], sampleRate, otherFeatures);
                otherFeatures[1] = featureExt.extractFeature(windowSample[1], sampleRate, otherFeatures);
                otherFeatures[2] = featureExt.extractFeature(windowSample[2], sampleRate, otherFeatures);
                featureExt = new SpectralFlux();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 4:
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new Compactness();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 5:
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new SpectralVariability();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 6:
                featureExt = new RMS();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 7:
                featureExt = new RMS();
                windowSample = audio.getSampleWindowsMixedDown(100);
                for (int j = 0; j < 100; j++) {
                    otherFeatures[j] = featureExt.extractFeature(windowSample[j], sampleRate, null);
                }
                featureExt = new FractionOfLowEnergyWindows();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 8:
                featureExt = new ZeroCrossings();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 9:
                featureExt = new BeatHistogram();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new BeatHistogramLabels();
                otherFeatures[1] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new StrongestBeat();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 10:
                featureExt = new BeatHistogram();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new BeatSum();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 11:
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new MFCC();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 12:
                featureExt = new ConstantQ();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 13:
                featureExt = new LPC();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 14:
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new Moments();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 15:
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new PeakFinder();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);
            case 16:
                featureExt = new MagnitudeSpectrum();
                windowSample = audio.getSampleWindowsMixedDown(100);
                for (int j = 0; j < 100; j++) {
                    otherFeatures[j] = featureExt.extractFeature(windowSample[j], sampleRate, null);
                }
                featureExt = new AreaMoments();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            default:
                return null;

        }
    }
    public static void main(String[] args) {
        Map<String, Double> meanValues = new HashMap<String, Double>();
        Iterator it = AFF.entrySet().iterator();
        while(it.hasNext()) {
            File f = new File("data/AmbulanceSiren.wav");
            Map.Entry pair = (Map.Entry) it.next();
            try{
                AudioSamples as = new AudioSamples(f, "AmbulanceSiren", true);
                System.out.println(feature(as, Integer.parseInt(pair.getValue().toString())));
                meanValues.put(pair.getKey().toString(), mean(feature(as, Integer.parseInt(pair.getValue().toString()))));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        Iterator ite = meanValues.entrySet().iterator();
        while(ite.hasNext()) {
            Map.Entry pair = (Map.Entry) ite.next();
            System.out.println(pair.getKey().toString() + ": " + pair.getValue().toString());
        }
        String str = "AmbulanceSiren:" + meanValues.get("Zero_Crossings").toString() + ";" +
                meanValues.get("MFCC").toString() + ";" +
                meanValues.get("Spectral_Rolloff_Point").toString() + ";" +
                meanValues.get("Fration_of_Low_Energy_Windows").toString() + ";" +
                meanValues.get("Peak_Detection").toString() + ";" +
                meanValues.get("Root_Mean_Square").toString() + ";" +
                meanValues.get("Compactness").toString() + ";";
        System.out.println("Features: " + str);

    }

    public static double mean(double[] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        if(m.length != 0) {
            return sum / m.length;
        }
        else return 0;
    }
}
