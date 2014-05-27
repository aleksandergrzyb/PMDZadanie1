package pl.put.idss.dw.hadoop.tasks;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;

import pl.put.idss.dw.hadoop.examples.WordCount;

public class NGramCount {

	private static final String GRAM_SIZE_PARAMETER = "gram.size";

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		private static final IntWritable ONE = new IntWritable(1);

		private final Text word = new Text();

		private int gramSize;

		/**
		 * In such a way, we are taking the program parameters:
		 */
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			gramSize = context.getConfiguration().getInt(GRAM_SIZE_PARAMETER, 3);
		};

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			//@TODO
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2 && otherArgs.length != 3) {
			System.err.println("Usage: ngramcount <in> <out> <n defaults 3>");
			System.exit(2);
		}
		if (otherArgs.length == 3) {
			conf.set(GRAM_SIZE_PARAMETER, otherArgs[2]);
		}

		Job job = new Job(conf, "ngramcount");
		job.setJarByClass(NGramCount.class);

		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
