package com.zhangyu.mr.allin;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class WholeFileMR {
	public static class MyMapper extends Mapper<NullWritable,Text,Text,NullWritable>{
		protected void map(NullWritable key, Text value,Context context)
				throws IOException, InterruptedException {
			System.out.println("value: "+value);
			context.write(value, NullWritable.get());
		}
	}
	public static class MyReducer extends Reducer<Text,NullWritable,Text,NullWritable>{
		@Override
		protected void reduce(Text k2, Iterable<NullWritable> v2s,Context context)
				throws IOException, InterruptedException {
			context.write(k2, NullWritable.get());
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: inFilePath outPath");
			System.exit(1);
		}
		Configuration conf = new Configuration();
		Job job = new Job(conf, "WholeFileMR");
		job.setJarByClass(WholeFileMR.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setInputFormatClass(WholeFileInputFormat.class);
		
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0:1);
	}
}
