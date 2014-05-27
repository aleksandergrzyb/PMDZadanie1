package pl.put.idss.dw.hadoop.tasks;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;

public class GrepCount {

	private static final String REGEXP_PARAMETER = "grep.regexp";

	public static class RegexpMapper extends Mapper<Object, Text, Text, LongWritable> {

		private static final LongWritable ONE = new LongWritable(1);

		private Pattern pattern;

		protected void setup(Context context) throws IOException, InterruptedException {
			// use context.getConfiguration() to compile pattern
		};

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			//@TODO user pattern matcher
		}

	}

	private static boolean runCountTask(Configuration conf, Path input, Path output) throws Exception {
		Job job = new Job(conf, "grepcount-count");
		job.setJarByClass(GrepCount.class);

		job.setMapperClass(RegexpMapper.class);
		job.setReducerClass(LongSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, output);

		return job.waitForCompletion(true);
	}

	private static boolean runSortTask(Configuration conf, Path input, Path output) throws Exception {
		Job job = new Job(conf, "grepcount-sort");
		job.setJarByClass(GrepCount.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(InverseMapper.class);
		job.setNumReduceTasks(1);
		job.setSortComparatorClass(LongWritable.DecreasingComparator.class);

		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, output);

		return job.waitForCompletion(true);
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err.println("Usage: grepcount <in> <out> <regexp>");
			System.exit(2);
		}
		conf.set(REGEXP_PARAMETER, otherArgs[2]);

		Path input = new Path(otherArgs[0]);
		Path tmp = new Path(otherArgs[1] + "-tmp");
		Path output = new Path(otherArgs[1]);

		if (!runCountTask(conf, input, tmp)) {
			System.exit(1);
		}
		System.exit(runSortTask(conf, tmp, output) ? 0 : 1);
	}

}
