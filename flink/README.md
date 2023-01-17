# Flink实验报告

## 实验截图

购买集群平台资源

![image-20221019144647686](http://img.nebular.site/md/image-20221019144647686.png)



使用MRS平台在线提交Flink作业

![image-20221019153819728](http://img.nebular.site/md/image-20221019153819728.png)

作业运行成功

![image-20221019154112690](http://img.nebular.site/md/image-20221019154112690.png)

OBS 文件系统中生成的文件

![image-20221019154156459](http://img.nebular.site/md/image-20221019154156459.png)

文件内容（部分截图）

![image-20221019154247558](http://img.nebular.site/md/image-20221019154247558.png)







在MRS平台和OBS文件系统在线运行自己的flink程序

![image-20221019161430164](http://img.nebular.site/md/image-20221019161430164.png)

作业完成

![image-20221019161558726](http://img.nebular.site/md/image-20221019161558726.png)

OBS文件系统查看输出文件

![image-20221019161626521](http://img.nebular.site/md/image-20221019161626521.png)

输出文件内容（部分）

![1](http://img.nebular.site/md/1.png)

## 实验输入参数

```bash
-Dfs.obs.access.key=SULGLX8BCGX1AVENAMUR -Dfs.obs.secret.key=0F3Dj8ssRdhiyRGqmbQnSRb2gO0uUn6lT6poS6JN --input obs://mrs-201250044-wxy/input/article.txt --output obs://mrs-201250044-wxy/output/output1.txt
```

```bash
-Dfs.obs.access.key=SULGLX8BCGX1AVENAMUR -Dfs.obs.secret.key=0F3Dj8ssRdhiyRGqmbQnSRb2gO0uUn6lT6poS6JN --input obs://mrs-201250044-wxy/input/article.txt --output obs://mrs-201250044-wxy/output/201250044_wxy.txt
```

## 实验代码

```java
package org.wxy;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.util.Collector;


public class BatchJob {

	public static void main(String[] args) throws Exception {
		String input;
		String output;

		ParameterTool params = ParameterTool.fromArgs(args);

		try {
			input = params.getRequired("input");
			output = params.getRequired("output");
		} catch (RuntimeException e) {
			System.out.println("Argument Error");
			e.printStackTrace();
			return;
		}
		//入口类，可以用来设置参数和创建数据源以及提交任务
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		//将结果输出于同一个文件中，设置并行度为 1：
		env.setParallelism(1);
		//创建了一个字符串类型的DataSet
		DataSet<String> text = env.readTextFile(input);
		//处理数据，切分(flatMap, split)，分组(groupBy)，统计(累加 sum)。
		DataSet<Tuple2<String, Integer>> counts = text.flatMap(new Tokenizer()).groupBy(0).sum(1);
		//写入结果文件
		counts.writeAsText(output, FileSystem.WriteMode.OVERWRITE);
		env.execute("Flink Batch Java API Skeleton");
	}

	public static class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
		@Override
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
			String[] tokens = value.toLowerCase().split("\\a*");
			for (String token : tokens) {
				if (token.length() > 0 && token.compareTo("a")>=0 && token.compareTo("z")<=0) {
					System.out.println(token);
					out.collect(new Tuple2<>(token, 1));
				}
			}
		}
	}

}
```



