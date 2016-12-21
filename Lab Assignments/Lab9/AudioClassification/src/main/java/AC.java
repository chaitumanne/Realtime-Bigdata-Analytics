import jAudioFeatureExtractor.AudioFeatures.*;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;

/**
 * Created by Mayanka on 20-Mar-16.
 */
public class AC {
    public enum AFF {
        Spectral_Centroid(1),
        Spectral_Rolloff_Point(2),
        Spectral_Flux(3),
        Compactness(4),
        Spectral_Variability(5),
        Root_Mean_Square(6),
        Fration_of_Low_Energy_Windows(7),
        Zero_Crossings(8),
        Strongest_Beat(9),
        Beat_Sum(10),
        MFCC(11),
        ConstantQ(12),
        LPC(13),
        Method_of_Moments(14),
        Peak_Detection(15),
        Area_Method_of_MFCCs(16);

        private final int value;

        AFF(int value) {

            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


    public static double[] feature(AudioSamples audio, AFF i) throws Exception {
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
        double[][] otherFeatures = null;
        double[][] windowSample;
        switch (i.getValue()) {
            case 1:
                featureExt = new SpectralCentroid();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 2:
                featureExt = new PowerSpectrum();
                otherFeatures[0] = featureExt.extractFeature(samples, sampleRate, otherFeatures);
                featureExt = new SpectralRolloffPoint();
                return featureExt.extractFeature(samples, sampleRate, otherFeatures);

            case 3:
                windowSample = audio.getSampleWindowsMixedDown(2);
                featureExt = new MagnitudeSpectrum();
                otherFeatures[0] = featureExt.extractFeature(windowSample[0], sampleRate, otherFeatures);
                otherFeatures[1] = featureExt.extractFeature(windowSample[1], sampleRate, otherFeatures);
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
}
