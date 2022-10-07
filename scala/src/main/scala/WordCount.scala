package main.scala

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.io.{File, PrintWriter}
import scala.collection.mutable
import scala.io.Source

object WordCount {
  def main(args: Array[String]): Unit = {
    val inputPath = new File("").getAbsoluteFile + "\\test.txt"
    // 使用spark进行词频统计
    useSpark(inputPath)
    // 不使用spark进行词频统计
    noUseSpark(inputPath)
  }

  /**
   * 使用spark进行词频统计
   *
   * @param inputPath 输入文件路径
   */
  def useSpark(inputPath: String): Unit = {
    // 创建spark配置
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    // 创建spark上下文环境
    val sc = new SparkContext(conf)
    // 通过文件系统构造RDD
    val lines: RDD[String] = sc.textFile(inputPath)
    // 将每一行按照空格切割合并，之后将每个单词小写
    val lowerWords: RDD[String] = lines.flatMap(lines => lines.split(" ")).map(word => word.toLowerCase)
    // 统计词频并且按照key值进行规约
    val wordCount: RDD[(String, Int)] = lowerWords.map(word => (word, 1)).reduceByKey((a, b) => a + b)
    // 按照词频降序排序
    val result = wordCount.sortBy(tuple => tuple._2, ascending = false)
    // 将统计结果打印到控制台
    result.collect().foreach(println)
    // 关闭spark
    sc.stop()
  }

  /**
   * 不使用spark，仅使用scala进行词频统计
   *
   * @param inputPath 输入文件路径
   */
  def noUseSpark(inputPath: String): Unit = {
    // 创建一个Map<Key:String Value:Int>，用于保存词频
    val wordCount = mutable.Map.empty[String, Int]
    // 获取文本
    val lines = readData(inputPath)
    // 将文本按照空格切割并且小写
    val lowerWords = lines.flatMap(line => line.split(" ")).map(word => word.toLowerCase)
    // 统计词频
    lowerWords foreach {
      word =>
        if (wordCount.contains(word)) wordCount(word) += 1
        else wordCount(word) = 1
    }
    // 按照词频降序排序
    val result = wordCount.toList.sortBy(_._2).reverse
    // 将统计结果打印在控制台上并保存结果
    println(result.toArray.mkString("Array(", ", ", ")"))
    saveResult(result)
  }

  /**
   * 读取文件数据
   *
   * @param inputPath 输入文件路径
   * @return 读取的文本
   */
  def readData(inputPath: String): Iterator[String] = {
    // 从文件读取数据
    val data = Source.fromFile(inputPath, "UTF-8")
    // 获取文本
    val lines = data.getLines()
    //返回结果
    lines
  }

  /**
   * 保存词频统计的结果（txt文件）
   *
   * @param result 词频统计的结果
   */
  def saveResult(result: List[(String, Int)]): Unit = {
    // 创建writer
    val writer = new PrintWriter(new File("result.txt"))
    result foreach {
      tuple =>
        val line = String.format("%-15s\t", tuple._1) + tuple._2 + "\n"
        writer.write(line)
    }
    writer.close()


  }
}
