/**
  *@author : Sri Harsha Chennavajjala
  *version : 1.0.0
  *Machine Leaning Part of Audio Analysis
  */

import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.{Level, Logger}
/**
  *Classification of input audio stream.
  */


object AudioClassification {

  val TRAINING_PATH = "data/training/*"
  val TESTING_PATH = "data/testing/*"

  val AUDIO_CATEGORIES = List("AmbulanceSiren", "40_smith_wesson_8x_gunshot", "Bomb", "BombSiren", "GlassBreaking", "Grenade")


  def main(args: Array[String]) {

//    System.setProperty("hadoop.home.dir", "F:\\winutils")

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkDecisionTree").set("spark.driver.memory", "4g")
    val sc = new SparkContext(sparkConf)
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    val train = sc.textFile("data/training/FeaturesSet.txt")
    val X_train= train.map ( line =>{
      val parts = line.split(':')
//      println(AUDIO_CATEGORIES.indexOf(parts(0)).toDouble, Vectors.dense(parts(1).split(';').map(_.toDouble)))
      LabeledPoint(AUDIO_CATEGORIES.indexOf(parts(0)).toDouble, Vectors.dense(parts(1).split(';').map(_.toDouble)))

    })


    val test = sc.textFile("data/testing/TestDataSet.txt")
    val X_test= test.map ( f = line => {
      val parts = line.split(':')
//      println(AUDIO_CATEGORIES.indexOf(parts(0)).toDouble, Vectors.dense(parts(1).split(';').map(_.toDouble)))
      LabeledPoint(AUDIO_CATEGORIES.indexOf(parts(0)).toDouble, Vectors.dense(parts(1).split(';').map(_.toDouble)))

    })

    val numClasses = 10
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainClassifier(X_train, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)



    val labelAndPreds = X_test.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    labelAndPreds.foreach(f=>{
      println("prediction" +f._1 + ", Actual Label" + f._2)

    })
    val testErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / X_test.count()
    println("Test Error = " + testErr)
    println("Learned classification forest model:\n" + model.toDebugString)
    //val  accuracy = 1.0 * labelAndPreds.filter(x => x._1 == x._2).count() / test.count()



    println("Prediction and label" + labelAndPreds)


    val metrics = new MulticlassMetrics(labelAndPreds)
    //println("Confusion Matrix \n \n : "+metrics.confusionMatrix)
    println("Fmeasure is:"+metrics.fMeasure + "Precision is:" +metrics.precision)
    println("Accuracy : " + metrics.precision)
    // Save and load model
    model.save(sc, "ContextDecisionTreeClassificationModel")
    MongoUpload.insertIntoMongoDB(model.toDebugString);
    val sameModel = DecisionTreeModel.load(sc, "ContextDecisionTreeClassificationModel")
    // sameModel.save(sc, "myRandomForestClassificationModel1")

  }

}
