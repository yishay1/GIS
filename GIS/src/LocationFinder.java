import java.io.File;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author ���
 *
 */
public class LocationFinder {
	
	ArrayList<singleScan> DB;
	ArrayList<singleScan> input;
	
	public LocationFinder(String pathDB,String pathInput)
	{
		File fDB=new File(pathDB);
		File fInput=new File(pathInput);
		this.DB=read.readOutputFolder(fDB.listFiles());
		this.input=read.readOutputFolder(fInput.listFiles());
	}
	
	public void findLocation()
	{
		for(int i=0;i<input.size();i++)
		{
			Locating(DB,input.get(i));
			
		}
		
	}
	
	public  void Locating(singleScan input)
	{
		ImaginationScansList list= new ImaginationScansList();
		double imagination=0;
		for(int j=0;j<this.DB.size();j++)
		{
			imagination=testImagination(DB.get(j),input.getWifiSpotsList());
			ImaginationScan scan=new ImaginationScan(DB.get(j),imagination);
			list.add(scan);
		}
		input.setCoordinate(findLocation(list,input));

	}
	
	private coordinate findLocation(ImaginationScansList list, singleScan inputScan) 
	{
		double moneLon=0;
		double moneLat=0;
		double moneAlt=0;
		double mech=0;
		double pi=1;

		for(int i=0;i<list.getSize();i++)
		{
			pi=list.getList().get(i).computePI(inputScan.getWifiSpotsList());
			moneLon+=list.getList().get(i).getScan().getCoordinate().getLon()*pi;
			moneLat+=list.getList().get(i).getScan().getCoordinate().getLat()*pi;
			moneAlt+=list.getList().get(i).getScan().getCoordinate().getAlt()*pi;
			mech+=pi;
		}
		coordinate coor=new coordinate(moneLon/mech,moneLat/mech,moneAlt/mech);
		return coor;
	}
	
	private double testImagination(singleScan singleScan,ArrayList<wifiSpot> input)
	{
		double imagination=0;
		int tmp=0;
		for(int i=0;i<input.size();i++)
		{
			tmp=singleScan.contains(input.get(i));
			if(tmp>=0)
			{
				imagination*=Math.abs(-input.get(i).getSignal()-Math.abs(-input.get(i).getSignal()-singleScan.getWifiSpotsList().get(tmp).getSignal()))/-input.get(i).getSignal();
			}
			else imagination*=0.1;
		}
		return imagination;
	}
	
}
