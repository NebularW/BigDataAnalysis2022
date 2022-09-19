import org.apache.spark.{SparkContext,SparkConf}
object Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("GroupSort").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val test =List(("key1","123",12,2,0.13),("key1","123",12,3,0.18),("key2","234",12,1,0.09),("key1","345",12,8,0.75),("key2","456",12,5,0.45))
    val rdd = sc.parallelize(test)

    val rdd1= rdd.map(l => (l._1, (l._2, l._3, l._4, l._5))).groupByKey()
      .flatMap(line => {
        val topItem = line._2.toArray.sortBy(_._4)(Ordering[Double].reverse)
        topItem.map(f=>(line._1,f._1,f._4)).toList
      })
    rdd1.foreach(println)

    sc.stop()
  }
}