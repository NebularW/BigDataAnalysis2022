import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object WordCount {
  def main(args: Array[String]) {
//    在电脑对应路径下应有一个用于存放单词的文件
        val inputFile = "D:\\Code\\Course\\BigDataAnalysis\\hw1\\test.txt"
        val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")

        val sc = new SparkContext(conf)
        val textFile = sc.textFile(inputFile)
        val wordCount = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).
          reduceByKey((a, b) => a + b)

        //将统计结果打印到控制台上
        wordCount.collect().foreach(println)
    println("Hello World!")
  }
}