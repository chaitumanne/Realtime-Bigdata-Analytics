/**
  *@author : Sri Harsha Chennavajjala
  *version : 1.0.0
  *Machine Leaning Part of Audio Analysis
  */

import java.io.File
import javax.sound.sampled.AudioInputStream

import jAudioFeatureExtractor.AudioFeatures._
import jAudioFeatureExtractor.jAudioTools.AudioSamples
import org.apache.commons.io.FileUtils


/**
  *Feature Extraction from Audio Source
  */
object AudioFeature extends Enumeration {
  type AudioFeature = Value
  val Spectral_Centroid, Spectral_Rolloff_Point, Spectral_Flux, Compactness, Spectral_Variability, Root_Mean_Square, Fration_of_Low_Energy_Windows, Zero_Crossings, Strongest_Beat, Beat_Sum, MFCC, ConstantQ, LPC, Method_of_Moments, Peak_Detection, Area_Method_of_MFCCs = Value

}

object AudioFeatureExtraction {


  val AUDIO_CATEGORIES = List("AmbulanceSiren", "40_smith_wesson_8x_gunshot", "Bomb", "BombSiren", "GlassBreaking", "Grenade")
  def main(args: Array[String]) {
    val video = new File("data/ATMVideo.mp4");
    val conv = new ConvertMP4ToWAV();
    conv.convertToAudio(video);

    AudioFeatureExtraction("ATMVideo", "data/" + video.getName.substring(0, video.getName.lastIndexOf(".")) + ".wav")
//    AUDIO_CATEGORIES.foreach(x => AudioFeatureExtraction(x, "data/" + x + ".wav"));


  }
  def AudioFeatureExtraction(audioCategory: String, path: String): String = {

    val audio: AudioSamples = new AudioSamples(new File(path), path, false)

    val f: Array[Double] = feature(audio, AudioFeature.Zero_Crossings)
    val meanZCR = calculateMean(f);
    //val f1: Array[Double] = feature(audio, AudioFeature.Spectral_Flux)
    val f1: Array[Double] = feature(audio, AudioFeature.MFCC)
    val meanMFCC = calculateMean(f1)
    val f2: Array[Double] = feature(audio, AudioFeature.Spectral_Rolloff_Point)
    val meanSpectralRollOff = calculateMean(f2)
    val f3: Array[Double] = feature(audio, AudioFeature.Fration_of_Low_Energy_Windows)
    val meanLowEnergyWindows = calculateMean(f3)
    val f4: Array[Double] = feature(audio, AudioFeature.Peak_Detection)
    val meanPeakValue = calculateMean(f4)
    // val f6: Array[Double] = feature(audio, AudioFeature.LPC)
    val f5: Array[Double] = feature(audio, AudioFeature.Root_Mean_Square)
    val meanRMS = calculateMean(f5)
    val f6: Array[Double] = feature(audio, AudioFeature.Compactness)
    val meanCompactness = calculateMean(f6)
    val str = meanZCR + ";" + meanMFCC + ";" + meanSpectralRollOff + ";" + meanPeakValue + ";" + meanRMS + ";" + meanCompactness + ";"

    println(audioCategory + " Features: " + str)
    FileUtils.writeStringToFile(new File("data/FeaturesSet.txt"), audioCategory + ":" + str + "\n", true)

    str
  }

  @throws(classOf[Exception])
  def feature(audio: AudioSamples, i: AudioFeature.Value): Array[Double] = {
    var featureExt: FeatureExtractor = null
    val audioInputStream: AudioInputStream = audio.getAudioInputStreamMixedDown
    val samples: Array[Array[Double]] = audio.getSampleWindowsMixedDown(2825)
    val featureMeanSampleArray  = new Array[Double](1000)
    val sampleRate: Double = 44100
    val otherFeatures = Array.ofDim[Double](1000, 1000)
    var windowSample: Array[Array[Double]] = null
    val sampleRate1 = audio.getSamplingRateAsDouble
//    println("sampling rate is:" +sampleRate1)
//    println("samples length is:"+ samples.length)
//    println("Frame length is is:"+ audioInputStream.getFrameLength)
    for(index<-0 until samples.length){
      i match {
        case AudioFeature.Spectral_Centroid =>
          featureExt = new PowerSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureExt = new SpectralCentroid

          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Spectral_Rolloff_Point =>
          featureExt = new PowerSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new SpectralRolloffPoint
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Compactness =>
          featureExt = new MagnitudeSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new Compactness
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Spectral_Variability =>
          featureExt = new MagnitudeSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new SpectralVariability
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Root_Mean_Square =>
          featureExt = new RMS
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Fration_of_Low_Energy_Windows =>
          featureExt = new RMS
          windowSample = audio.getSampleWindowsMixedDown(5)
          for (j <- 0 to 100)
            otherFeatures(j) = featureExt.extractFeature(windowSample(j), sampleRate, null)
          featureExt = new FractionOfLowEnergyWindows
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Zero_Crossings =>
          featureExt = new ZeroCrossings
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

        case AudioFeature.Strongest_Beat =>
          featureExt = new BeatHistogram
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new BeatHistogramLabels
          otherFeatures(1) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new StrongestBeat
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)
        case AudioFeature.Beat_Sum =>
          featureExt = new BeatHistogram
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new BeatSum
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)
        case AudioFeature.MFCC =>
          featureExt = new MagnitudeSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new MFCC
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample) + 100
        case AudioFeature.ConstantQ =>
          featureExt = new ConstantQ
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)
        case AudioFeature.LPC =>
          featureExt = new LPC
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)
        case AudioFeature.Method_of_Moments =>
          featureExt = new MagnitudeSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new Moments
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)
        case AudioFeature.Peak_Detection =>
          featureExt = new MagnitudeSpectrum
          otherFeatures(0) = featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          featureExt = new PeakFinder
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)
        case AudioFeature.Area_Method_of_MFCCs =>
          featureExt = new MagnitudeSpectrum
          windowSample = audio.getSampleWindowsMixedDown(100)
          for (j <- 0 to 100)
            otherFeatures(j) = featureExt.extractFeature(windowSample(j), sampleRate, null)
          featureExt = new AreaMoments
          featureExt.extractFeature(samples(0), sampleRate, otherFeatures)
          val windowFeatureSample = featureExt.extractFeature(samples(index), sampleRate, otherFeatures)
          featureMeanSampleArray(index) = calculateMean(windowFeatureSample)

      }

    }
    featureMeanSampleArray
  }

  @throws(classOf[Exception])
  def calculateMean(sample: Array[Double]): Double = {
    var meanValue : Double =0
    for(i<-0 until sample.length)
    {
      meanValue += sample(i)
    }
    if (sample.length != 0 )
    meanValue = meanValue/sample.length
    meanValue
  }
}
