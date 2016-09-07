package edu.umkc.cm7cd.lab2

/**
  * Created by chaitu on 9/2/2016.
  */
import org.apache.spark._

object SparkProgramming {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "E:\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)
    val input = sc.textFile("StudList.csv")
    // Action: Count the no of students in a class
    println("No of students "+input.count())
    // Transformation: Displaying the student details
    val StudDetails = input.map{t => val p = t.split(",").map(word=>(word,1))
      (p(1))
    }
    val output=StudDetails.reduceByKey(_+_).map(item => item.swap).sortByKey(true,1).map(item => item.swap)
    output.saveAsTextFile("op")
    // Transformation, Action: Count the number of students who scored 93
    println("Number of students who scored 93: "+input.filter(line => line.contains("93")).count())
  }
}
