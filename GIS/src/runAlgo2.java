import Algorithms.personLocationFinder;
import Libraries.write;

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
		write.writeCsvFile(lf.getInput().getScansList(), "C:\\Users\\���\\Desktop\\testnogps\\check1.csv");
	}
}
