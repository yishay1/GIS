

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

/**
 * @author Yehonatan&Yishay
 * 
 * @description this class represents an object of the type kmlFile and its constructor takes a list of singleScan object 
 * 				and process it into a kml file that shows the wifi spots on the map.
 * 
 */

public class kmlFile {
	private File kmlFileOutput;
	private final String DocumentName = "WifiSpotsMap";
	private final Kml kmlObject = new Kml();
	private Document document = kmlObject.createAndSetDocument().withName(DocumentName);
	private ArrayList<singleScan> scansList = new ArrayList<singleScan>();

	public kmlFile(ArrayList<singleScan> scansList, String outputPath){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		this.kmlFileOutput = new File(outputPath +"\\outputEarth"+dateFormat.format(date)+".kml");
		this.scansList = scansList;
		addStyles(this.document);
		addScansPlacemarks();
		removeDuplicateMac();
		addWifiSpotsPlacemarks();
		exportKml();
		


	}


	private void addStyles(Document document){
		document.createAndAddStyle().withId("wifiIcon").createAndSetIconStyle().withIcon(new Icon().withHref("http://www.freepngimg.com/download/wifi/4-2-wi-fi-png-images.png"));
		document.createAndAddStyle().withId("Magnifier").createAndSetIconStyle().withIcon(new Icon().withHref("https://images.vexels.com/media/users/3/132064/isolated/preview/27a9fb54f687667ecfab8f20afa58bbb-search-businessman-circle-icon-by-vexels.png"));
	}

	private void removeDuplicateMac(){
		Map<String, wifiSpot> macToSignalMap = new HashMap<>();

		List<ArrayList<wifiSpot>> allWifiSpotLists = scansList.stream().map(singleScan::getWifiSpotsList)
				.collect(Collectors.toList());

		allWifiSpotLists.forEach(wifiSpotList -> wifiSpotList.forEach(wifiSpot -> {
			if (macToSignalMap.containsKey(wifiSpot.getMac())) {
				if (wifiSpot.compareBySignal(macToSignalMap.get(wifiSpot.getMac()))==1) {
					macToSignalMap.put(wifiSpot.getMac(), wifiSpot);
				}
			}
			else {
				macToSignalMap.put(wifiSpot.getMac(), wifiSpot);
			}
		}));
		allWifiSpotLists.forEach(wifiSpotList -> wifiSpotList
				.removeIf(wifiSpot -> !wifiSpot.equals(macToSignalMap.get(wifiSpot.getMac()))));








		//		ArrayList<wifiSpot> withoutDuplicatesWifiSpots = new ArrayList<wifiSpot>();
		//		for (int i=0;i<scansList.size();i++){
		//			singleScan currentScan = scansList.get(i);
		//			for(int j=0;j<currentScan.getWifiSpotsList().size();j++){
		//				wifiSpot currentWifiSpot = currentScan.getWifiSpotsList().get(j);
		//				boolean flag = false;
		//				for(int k = 0; k<withoutDuplicatesWifiSpots.size();k++){
		//					if(currentWifiSpot.getMac().equals(withoutDuplicatesWifiSpots.get(k).getMac())){
		//						flag = true;
		//						if(currentWifiSpot.compareBySignal(withoutDuplicatesWifiSpots.get(k))==1){
		//							withoutDuplicatesWifiSpots.remove(k);
		//							withoutDuplicatesWifiSpots.add(currentWifiSpot);
		//							break;	
		//						}
		//						else break;
		//					}
		//				}
		//				if(!flag) withoutDuplicatesWifiSpots.add(currentWifiSpot); 
		//			}
		//			
		//		}
		//		return withoutDuplicatesWifiSpots;
	}

	private void addScansPlacemarks(){
		for (int i=0;i<scansList.size();i++){
			coordinate scanLocation = scansList.get(i).getCoordinate();
			String model = scansList.get(i).getId();
			String time = scansList.get(i).getTime();
			int numOfWifiSpots = scansList.get(i).getSize();
			String Description = numOfWifiSpots + "wifi networks found:\n";
			for (int j=0;j<scansList.get(i).getWifiSpotsList().size();j++){
				Description+= scansList.get(i).getWifiSpotsList().get(j).getSsid()+"\n";
			}
			Placemark p = this.document.createAndAddPlacemark();
			p.createAndSetTimeStamp().withWhen(time.replace(' ', 'T'));
			p.withName(model).withDescription(Description).withStyleUrl("#Magnifier").createAndSetPoint().addToCoordinates(scanLocation.getLon(), scanLocation.getLat(), scanLocation.getAlt());
		}
	}

	private void addWifiSpotsPlacemarks(){
		for (int i = 0; i < scansList.size(); i++) {
			coordinate scanLocation = scansList.get(i).getCoordinate();
			String model = scansList.get(i).getId();
			String time = scansList.get(i).getTime();
			ArrayList<wifiSpot> currentWifiSpotsList = scansList.get(i).getWifiSpotsList();
			for (int j = 0; j < (currentWifiSpotsList.size()); j++) {
				String ssid = currentWifiSpotsList.get(j).getSsid();
				String mac = currentWifiSpotsList.get(j).getMac();
				String frequency = currentWifiSpotsList.get(j).getChannel();
				String signal = currentWifiSpotsList.get(j).getSignal();
				String description = "SSID: " + "<b>" + ssid + "</b>" + "<br/>" + "MAC: " + "<b>" + mac + "</b>"
						+ "<br/>" + "Time: " + "<b>" + time + "</b>" + "<br/>" + "Model: " + "<b>" + model
						+ "</b>" + "<br/>" + "Longitude: " + "<b>" + scanLocation.getLon() + "</b>" + "<br/>" + "Latitude: "
						+ "<b>" + scanLocation.getLat() + "</b>" + "<br/>"  + "Altitude: " 
						+ "<b>"+ scanLocation.getAlt() + "</b>" + "<br/>" +  "Frequency: " + "<b>" + frequency + "</b>"
						+ "<br/>" + "Signal: " + "<b>" + signal + "</b>";
				Placemark p = this.document.createAndAddPlacemark();
				p.createAndSetTimeStamp().withWhen(time.replace(' ', 'T'));
				p.withName(ssid).withStyleUrl("#wifiIcon").withOpen(Boolean.TRUE).withDescription(description).createAndSetPoint().addToCoordinates(scanLocation.getLon(), scanLocation.getLat(),scanLocation.getAlt());
			}
		}
	}
	
	private void exportKml(){
		try{
		kmlObject.marshal(kmlFileOutput);
		System.out.println("kml exported successfully!");
		}
		catch (IOException ex) {
			System.out.print("Error exporting file\n" + ex);
			System.exit(2);
		}
	}
	
	
	
}
