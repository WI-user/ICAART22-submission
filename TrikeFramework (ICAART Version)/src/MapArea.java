import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class MapArea {
	
	private Double leftBorder;
	private Double rightBorder;
	private Double topBorder;
	private Double bottomBorder;
	
	public MapArea() {
		//this.loadCoordinates("Positionsgraph_GPS.gpx");
		this.loadCoordinates("positionsgraph_utm.gpx");		
	}
	
	public MapArea(String filepath) {
		this.loadCoordinates(filepath);
	}
	
	public Double getLeftBorder() {
		return this.leftBorder;
	}
	
	public Double getRightBorder() {
		return this.rightBorder;
	}
	
	public Double getTopBorder() {
		return this.topBorder;
	}
	
	public Double getBottomBorder() {
		return this.bottomBorder;
	}

   private void loadCoordinates(String filepath) {
	   
	  Double left = Double.POSITIVE_INFINITY;
	  Double right = Double.NEGATIVE_INFINITY;
	  Double bottom = Double.POSITIVE_INFINITY;
	  Double top = Double.NEGATIVE_INFINITY;

      try {
         File inputFile = new File(filepath);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("trk");
         
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            //System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;

               NodeList tracksegments = eElement.getElementsByTagName("trkseg");
               for (int tracksegmentIndex = 0; tracksegmentIndex < tracksegments.getLength(); tracksegmentIndex++) {
            	   Node currentTracksegment = tracksegments.item(tracksegmentIndex);
                   if (currentTracksegment.getNodeType() == Node.ELEMENT_NODE) {
                       Element tElement = (Element) currentTracksegment;
                       NodeList trackpoints = tElement.getElementsByTagName("trkpt");
                       for (int trackpointIndex = 0; trackpointIndex < trackpoints.getLength(); trackpointIndex++) {
                    	   Node currentTrackpoint = trackpoints.item(trackpointIndex);
                    	   if (currentTrackpoint.getNodeType() == Node.ELEMENT_NODE) {
                               Element tptElement = (Element) currentTrackpoint;
                               Double lat = (Double.parseDouble(tptElement.getAttribute("lat")));
                               Double lon = (Double.parseDouble(tptElement.getAttribute("lon")));
                         	   if (lat < left) {left = lat;}
                        	   if (lat > right) {right = lat;}
                        	   if (lon < bottom) {bottom = lon;}
                        	   if (lon > top) {top = lon;}
                        	
                        	   
                    	   }
                       }
                   }
               }
            }
         }
         this.leftBorder = left;
         this.rightBorder = right;
         this.topBorder = top;
         this.bottomBorder = bottom;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}