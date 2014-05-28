package pl.put.idss.dw.hadoop.tasks;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;

public class NeighbourCount {
	
	public static class mapperOne extends Mapper<Object, Text, Text, IntWritable> {
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	}
	
	public static class reducerOne extends Reducer<>
	
	public static class mapperTwo extends Mapper<Object, Text, Text, IntWritable> {
		
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: neighbourcount <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "neighbourcount");
		job.setJarByClass(NeighbourCount.class);
		
		job.setMapperClass(mapperOne.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setReducerClass()
		
		Job job2 = new Job(conf, "neighbourcount2");
		job2.setJarByClass(NeighbourCount.class);
		
		job2.setMapperClass(mapperTwo.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(IntWritable.class);
		
		

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
