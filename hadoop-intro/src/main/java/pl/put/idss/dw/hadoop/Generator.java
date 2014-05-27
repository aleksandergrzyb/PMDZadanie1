package pl.put.idss.dw.hadoop;

/**
 * This class has been used to generate a data set.
 */
public class Generator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 30; ++i) {
			for (int j = 0; j < 10; ++j) {
				int rand = (int) Math.floor(Math.random()*5);
				System.out.println(i+ " " + j + " " + (2.0 + rand*0.5));
			}
		}
	}

}
