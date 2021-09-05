package module3;

//Java utilities libraries

import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/**
 * EarthquakeCityMap An application with an interactive map displaying earthquake data. Author: UC San Diego
 * Intermediate Software Development MOOC team
 *
 * @author Your name here Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

    // You can ignore this.  It's to keep eclipse from generating a warning.
    private static final long serialVersionUID = 1L;

    // IF YOU ARE WORKING OFFLINE, change the value of this variable to true
    private static final boolean offline = false;

    // Less than this threshold is a light earthquake
    public static final float THRESHOLD_MODERATE = 5;
    // Less than this threshold is a minor earthquake
    public static final float THRESHOLD_LIGHT = 4;

    final int YELLOW = color(255, 255, 0);
    final int PINK = color(255, 182, 193);
    final int BLUE = color(0, 0, 255);
    final int BLACK = color(0, 0, 0);
    final int CREAM = color(248, 240, 198);

    /**
     * This is where to find the local tiles, for working without an Internet connection
     */
    public static String mbTilesString = "blankLight-1-3.mbtiles";

    // The map
    private UnfoldingMap map;

    //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";


    public void setup() {
        size(950, 600);

        if (offline) {
            map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
            earthquakesURL = "2.5_week.atom";    // Same feed, saved Aug 7, 2015, for working offline
        } else {
            map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.HybridProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
            //earthquakesURL = "2.5_week.atom";
        }

        map.zoomToLevel(2);
        MapUtils.createDefaultEventDispatcher(this, map);

        // The List you will populate with new SimplePointMarkers
        List<Marker> markers = new ArrayList<Marker>();

        //Use provided parser to collect properties for each earthquake
        //PointFeatures have a getLocation method
        List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

        //TODO (Step 3): Add a loop here that calls createMarker (see below)
        // to create a new SimplePointMarker for each PointFeature in
        // earthquakes.  Then add each new SimplePointMarker to the
        // List markers (so that it will be added to the map in the line below)
        for (int i = 0; i < earthquakes.size(); i++) {
            PointFeature feature = earthquakes.get(i);
            SimplePointMarker val = createMarker(feature);
            markers.add(val);
        }

        // Add the markers to the map so that they are displayed
        map.addMarkers(markers);
    }

    /* createMarker: A suggested helper method that takes in an earthquake
     * feature and returns a SimplePointMarker for that earthquake
     *
     * In step 3 You can use this method as-is.  Call it from a loop in the
     * setup method.
     *
     * TODO (Step 4): Add code to this method so that it adds the proper
     * styling to each marker based on the magnitude of the earthquake.
     */
    private SimplePointMarker createMarker(PointFeature feature) {
        // To print all of the features in a PointFeature (so you can see what they are)
        // uncomment the line below.  Note this will only print if you call createMarker
        // from setup
        //System.out.println(feature.getProperties());

        // Create a new SimplePointMarker at the location given by the PointFeature
        SimplePointMarker marker = new SimplePointMarker(feature.getLocation());

        Object magObj = feature.getProperty("magnitude");
        float mag = Float.parseFloat(magObj.toString());

        // Here is an example of how to use Processing's color method to generate
        // an int that represents the color YELLOW.

        if (mag >= THRESHOLD_MODERATE) {
            marker.setColor(PINK);
            marker.setRadius(20);
        } else if (mag >= THRESHOLD_LIGHT) {
            marker.setColor(YELLOW);
            marker.setRadius(10);
        } else {
            marker.setColor(BLUE);
            marker.setRadius(5);
        }

        // TODO (Step 4): Add code below to style the marker's size and color
        // according to the magnitude of the earthquake.
        // Don't forget about the constants THRESHOLD_MODERATE and
        // THRESHOLD_LIGHT, which are declared above.
        // Rather than comparing the magnitude to a number directly, compare
        // the magnitude to these variables (and change their value in the code
        // above if you want to change what you mean by "moderate" and "light")


        // Finally return the marker
        return marker;
    }

    public void draw() {
        background(10);
        map.draw();
        addKey();
    }


    // helper method to draw key in GUI
    // TODO: Implement this method to draw the key
    private void addKey() {
        // Remember you can use Processing's graphics methods here
        fill(CREAM);
        rect(0, 0, 200, 400);

        fill(BLACK);
        text("Earthquake Key", 25, 25);

        final int TEXT_X = 50;
        final int TEXT_Y = 75;
        final int Y_INC = 50;

        text("5.0+ Magnitude", TEXT_X, TEXT_Y);
        text("4.0+ Magnitude", TEXT_X, TEXT_Y + Y_INC);
        text("below 4.0", TEXT_X, TEXT_Y + 2 * Y_INC);

        final int HALF_TEXT_HEIGHT = 5;
        final int ELLIPSE_X = 25;
        fill(PINK);
        final int HIGH_R = 20;
        ellipse(ELLIPSE_X, TEXT_Y - HALF_TEXT_HEIGHT, HIGH_R, HIGH_R);
        fill(YELLOW);
        final int MID_R = 10;
        ellipse(ELLIPSE_X, TEXT_Y + Y_INC - HALF_TEXT_HEIGHT, MID_R, MID_R);
        fill(BLUE);
        final int LOW_R = 5;
        ellipse(ELLIPSE_X, TEXT_Y + 2 * Y_INC - HALF_TEXT_HEIGHT, LOW_R, LOW_R);


    }
}
