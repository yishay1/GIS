package main.java.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import main.java.objects.coordinate;
import main.java.objects.singleScan;


/**
 *
 * @author Yehonatan&Yishay
 * @description A library class. contains functions to filter ArrayList of singleScan objects.
 *
 */
public class Filter implements Serializable{
	int type;
	int not;
	String input1;
	String input2;
	String input3;
	String input4;
	
	
//	public static void mainFilter(ArrayList<singleScan> scansList) 
//	{
//		boolean flag=true;
//		Scanner choose=new Scanner(System.in);
//		while(flag)
//		{
//			System.out.println("filter by:\n1.for time choose 1\n2.for id choose 2\n3.for location choose 3\n4.to not filter-press any key");
//			String input=choose.nextLine();
//			//		choose.close();
//
//			if(!input.equals("1") && !input.equals("2") && !input.equals("3"))
//			{
//				System.out.println("you chose not to filter");
//				flag=false;
//			}
//			else
//			{
//				int i=Integer.parseInt(input);
//				if(i==1)
//					filterByTime(scansList,1,"","");
////				if(i==2)
////					filterByID(scansList,1);
////				if(i==3)
////					filterByLocation(scansList,1,"","");	
//			}
//		}	
//	}
	
	public Filter(String[] arr)
	{
		this.type=Integer.parseInt(arr[0]);
		this.not=Integer.parseInt(arr[1]);
		this.input1=arr[2];
		if(arr[3]!=null)
			this.input2=arr[3];
		if(arr[5]!=null)
		{
			this.input3=arr[4];
			this.input4=arr[5];
		}
	}
	
	public void filter(ArrayList<singleScan> scansList)
	{
		if(this.input4!=null)
			filterByLocation(scansList);
		else if(this.input2!=null)
			filterByTime(scansList);
		else
		filterByID(scansList);
	}
	
	/**
	 * @param singleScanList
	 * this functions filter the singleScans by its time using start time and end time that the functions gets from the user.
	 */
	
	/**
	 * @param singleScanList
	 * this functions filter the singleScans by its time using start time and end time that the functions gets from the user.
	 */
	private void filterByTime(ArrayList<singleScan> scansList)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		GregorianCalendar startTime= new GregorianCalendar();
		try {
			startTime.setTime(dateFormat.parse(this.input1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GregorianCalendar endTime= new GregorianCalendar();
		try {
			endTime.setTime(dateFormat.parse(this.input2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<scansList.size();i++)
		{
			GregorianCalendar scanTime= new GregorianCalendar();
			try {
				scanTime.setTime(dateFormat.parse(scansList.get(i).getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.not==1){
				if(!((scanTime.compareTo(startTime)>0) && (scanTime.compareTo(endTime)<0)))
				{
					scansList.remove(i);
					i--;
				}
			}
			if(this.not==0){
				if((scanTime.compareTo(startTime)>0) && (scanTime.compareTo(endTime)<0))
				{
					scansList.remove(i);
					i--;
				}
			}
		}
	}
	/**
	 * @param singleScanList
	 * Uses to filter the singleScans by location.
	 * the function gets Minimum coordinate and Maximum coordinate and creates an imagined rectangle area and remove all the coordinates out of the border of the rectangle. 
	 * 
	 */
	private void filterByLocation(ArrayList<singleScan> scansList)
	{	
		String minLon=this.input1;
		String minLat=this.input2;
		String maxLon=this.input3;
		String maxLat=this.input4;
		
		coordinate min=new coordinate(minLon,minLat);
		coordinate max=new coordinate(maxLon,maxLat);
		
		for(int i=0;i<scansList.size();i++)
		{
			coordinate singleScanCoor=scansList.get(i).getCoordinate();

			if(this.not==1 && (singleScanCoor.compare(min)<0 || (singleScanCoor.compare(max)>0 || singleScanCoor.compare(max)==-2)))
			{
				scansList.remove(i);
				i--;
			}
			if(this.not==0 && (singleScanCoor.compare(min)>=0 && (singleScanCoor.compare(max)<=0 && singleScanCoor.compare(max)!=-2)))
			{
				scansList.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * @param singleScanList
	 * filter the singleScans by id. remove all the singleScans with different id.
	 * if not=0 not filter, if not=1 regular filter
	 */
	private void filterByID(ArrayList<singleScan> scansList)
	{	
		String ID=this.input1;
		
		for(int i=0;i<scansList.size();i++)
		{
			if( this.not==1 && !ID.equals(scansList.get(i).getId()))
			{
				scansList.remove(i);
				i--;
			}
			if(this.not==0 && ID.equals(scansList.get(i).getId()))
			{
				scansList.remove(i);
				i--;
			}
		}
	}

	public int getType() {
		return type;
	}

	public int getNot() {
		return not;
	}

	public String getInput1() {
		return input1;
	}

	public String getInput2() {
		return input2;
	}

	public String getInput3() {
		return input3;
	}

	public String getInput4() {
		return input4;
	}
	
	public String toString()
	{
		String s="";
		if(this.type==0)
			s+="  AND";
		else
			s+="  OR";
		if(this.getNot()==0)
			s+=" NOT";
		String s1="";
		if(this.input4!=null)
			s1=s+"\n  Location filter-\n    between: " + this.input1 + "," + this.input2 +"\n    and "+this.input3+"," + this.input4 +".";
		else if(this.input2!=null)
			s1=s+"\n  Time filter-\n    from: "+ this.input1+"\n    to: "+this.input2;
		else
			s1=s+"\n  ID filter: "+this.input1;
		s1+="\n";
		return s1;
	}
	
	String toSQLQuerry()
	{
		String s="";
		if(type==0)
			s+="&&";
		else s+="||";
		if(not==0)
		{
			s+="!";
		}
		s+="(";
		if(input4!=null)
		{
			s+="lon>='"+input1+"' && lat>='"+input2+"'"+" && lon<='"+input3+"' && lat<='"+input4+"'";
		}
		else if(input2!=null)
		{
			s+="time>='"+input1+"' and time<='"+input2+"'";
		}
		else
		{
			 s+="device=='"+input1+"'";
		}
		return s+")";
	}
}
