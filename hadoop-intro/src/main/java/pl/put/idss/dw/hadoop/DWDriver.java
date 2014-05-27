package pl.put.idss.dw.hadoop;

import org.apache.hadoop.util.ProgramDriver;

import pl.put.idss.dw.hadoop.examples.WordCount;
import pl.put.idss.dw.hadoop.examples.WordCount2;
import pl.put.idss.dw.hadoop.tasks.GrepCount;
import pl.put.idss.dw.hadoop.tasks.NGramCount;
import pl.put.idss.dw.hadoop.tasks.NeighbourCount;
import pl.put.idss.dw.hadoop.tasks.SQLTask;

public class DWDriver {

	public static void main(String[] args) {
		//Shows the list of available programs
		ProgramDriver driver = new ProgramDriver();
		try {
			//You have to add a class of your map-reduce program here
			driver.addClass("wordcount", WordCount.class, "Word Count Example");
			driver.addClass("wordcount2", WordCount2.class, "Word Count 2 Example");
			driver.addClass("ngram", NGramCount.class, "NGram Count Example");
			driver.addClass("grepcount", GrepCount.class, "Grep Count Example");
			driver.addClass("neighbourcount", NeighbourCount.class, "Neighbour Count Example");
			driver.addClass("sql", SQLTask.class, "SQL Query Task");
			
			driver.driver(args);
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
