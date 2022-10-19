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
			String[] tokens = value.toLowerCase().split("\\W+");
			for (String token : tokens) {
				if (token.length() > 0) {
					System.out.println(token);
					out.collect(new Tuple2<>(token, 1));
				}
			}
		}
	}

}


