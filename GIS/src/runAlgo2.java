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
		LocationFinder lf=new LocationFinder("C:\\Users\\���\\Desktop\\testalgo","C:\\Users\\���\\Desktop\\testnogps");
		lf.findLocation();
		write.writeCsvFile(lf.input, "C:\\Users\\���\\Desktop\\testnogps\\check1.csv");
	}
}
