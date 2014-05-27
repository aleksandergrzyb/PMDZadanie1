package pl.put.idss.dw.hadoop.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;

public class SQLTask {

	public static class SQLJoinMapper extends Mapper<Object, Text, Text, Text> {

		protected static String getInputFileName(Context context) {
			FileSplit fs = (FileSplit) context.getInputSplit();
			return fs.getPath().getName();
		}

		@Override
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

		}
	}

	public static class SQLJoinReducer extends
			Reducer<Text, Text, Text, IntWritable> {

		private static final Text date = new Text();

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
		}

	}

	public static class SQLGroupMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		@Override
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: sql <input_dir> <output_dir>");
			System.exit(2);
		}

		Job job = new Job(conf, "sql-join");
		job.setJarByClass(SQLTask.class);

		job.setMapperClass(SQLJoinMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(SQLJoinReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0] + "/sales.in")); // or
																					// sales.easy.in
		FileInputFormat.addInputPath(job, new Path(otherArgs[0] + "/dates.in"));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1] + "_tmp"));

		Job job2 = new Job(conf, "sql-group");
		job2.setJarByClass(SQLTask.class);

		job2.setMapperClass(SQLGroupMapper.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(IntWritable.class);

		job2.setReducerClass(IntSumReducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job2, new Path(otherArgs[1] + "_tmp"));
		FileOutputFormat.setOutputPath(job2, new Path(otherArgs[1]));

		if (job.waitForCompletion(true)) {
			System.exit(job2.waitForCompletion(true) ? 0 : 1);
		}
		System.exit(1);

	}

}
