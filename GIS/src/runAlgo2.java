/**
 * 
 */

/**
 * @author ���
 *
 */
public class runAlgo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		personLocationFinder lf=new personLocationFinder("C:\\Users\\���\\Desktop\\testalgo","C:\\Users\\���\\Desktop\\testnogps");
		lf.findLocation();
		write.writeCsvFile(lf.input.getScansList(), "C:\\Users\\���\\Desktop\\testnogps\\check1.csv");
	}
}
