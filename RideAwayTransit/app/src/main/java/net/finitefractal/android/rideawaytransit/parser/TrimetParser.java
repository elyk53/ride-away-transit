package net.finitefractal.android.rideawaytransit.parser;

import android.location.Location;
import android.os.Debug;
import android.util.Log;

import net.finitefractal.android.rideawaytransit.R;
import net.finitefractal.android.rideawaytransit.model.*;

import org.w3c.dom.*;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.*;

/**
 * Parser for Trimet (Portland, OR Metro Area).
 * API documentation at http://developer.trimet.org/ws_docs/
 */
public class TrimetParser extends AgencyParser
{
    private static final String SCHEDULE_BASE_URL = "https://developer.trimet.org/ws/V1/routeConfig?dir=yes&stops=yes&appid=";

    public TrimetParser()
    {
        super(Agency.TRIMET);
    }

    /**
     * Loads the transit system information from the web-based source
     * If loading fails, the failure is written to the log
     * @return The transit system, or null if loading failed
     */
    public TransitSystem loadFromWeb()
    {
        try
        {
            URL url = new URL(SCHEDULE_BASE_URL + R.string.trimetApiKey);
            InputStream stream = url.openStream();
            return loadFromXml(stream);
        }
        catch(MalformedURLException e)
        {
            Log.wtf("TrimetLoadURL", e); // Hard-coded; should never be malformed
        }
        catch(IOException e)
        {
            Log.w("TrimetLoadURL", e);
        }

        return null;
    }

    /**
     * Loads the transit system information from the specified URI.
     * If parsing fails, the failure is logged as a warning
     * @param stream Input stream for the schedule's xml
     * @return The transit system, or null if parsing fails.
     */
    TransitSystem loadFromXml(InputStream stream)
    {
        try
        {
            Document doc = loadXmlDocument(stream);
            Hashtable<String, Stop> stops = new Hashtable<String, Stop>();
            ArrayList<Line> lines = new ArrayList<Line>();
            Element resultSet = doc.getDocumentElement();
            if(!resultSet.getNodeName().equals("resultSet"))
            {
                throw new ParseException("Expected resultSet but root node was " +
                        resultSet.getNodeName(), 0);
            }
            NodeList lineNodes = resultSet.getElementsByTagName("route");
            for(int i = 0; i < lineNodes.getLength(); ++i)
            {
                Element lineElem = (Element)lineNodes.item(i);
                Line line = parseLine(lineElem, stops);
                lines.add(line);
            }

            return new TransitSystem(Agency.TRIMET, lines);
        }
        catch(Exception e)
        {
            Log.w("TrimetLoadXML", e);
            return null;
        }
    }

    /**
     * Parses the metadata and stop list for a particular line.
     * @param root Root "Route" element for this line
     * @param stops Database of stops by ID, used to avoid creating duplicates.
     *              Note that this argument is modified by this method
     * @return The parsed line
     */
    private Line parseLine(Element root, Hashtable<String, Stop> stops)
    {
        ArrayList<Route> routeList = new ArrayList<Route>();
        String description = root.getAttribute("desc").trim();
        String number = root.getAttribute("route").trim();
        NodeList routeNodes = root.getElementsByTagName("dir");
        for(int i = 0; i < routeNodes.getLength(); ++i)
        {
            Element routeElem = (Element)routeNodes.item(i);
            routeList.add(parseRoute(routeElem, stops));
        }

        return new Line(Agency.TRIMET, number, description, routeList);
    }

     /**
     * Parses the metadata and stop list for a particular route of a line.
     * @param root Root "dir" element for this route (Trimet uses "direction" where we use "route")
     * @param stops Database of stops by ID, used to avoid creating duplicates.
     *              Note that this argument is modified by this method
     * @return The parsed Direction
     */
    private Route parseRoute(Element root, Hashtable<String, Stop> stops)
    {
        ArrayList<Stop> stopList = new ArrayList<Stop>();
        String dest = root.getAttribute("desc").trim();
        NodeList stopNodes = root.getElementsByTagName("stop");
        for(int i = 0; i < stopNodes.getLength(); ++i)
        {
            Element stopElem = (Element)stopNodes.item(i);
            stopList.add(getOrParseStop(stopElem, stops));
        }

        return new Route(dest, stopList);
    }

     /**
      * Parses the information for a particular stop, or retrieves the existing
      * one if a stop with this ID has already been processed
      * @param element "stop" element for this stop
      * @param stops Database of stops by ID, used to avoid creating duplicates.
      *              Note that this argument is modified by this method
     * @return The parsed Stop
     */
    private Stop getOrParseStop(Element element, Hashtable<String, Stop> stops)
    {
        String stopId = element.getAttribute("locid").trim();
        if(stops.containsKey(stopId))
            return stops.get(stopId);

        String description = element.getAttribute("desc").trim();
        String direction = element.getAttribute("dir").trim();
        double latitude = Double.parseDouble(element.getAttribute("lat"));
        double longitude = Double.parseDouble(element.getAttribute("lng"));

        // Directions will be of the form "northbound". Need to stop off the ending
        direction = direction.replace("bound", "").toUpperCase(Locale.US);
        Direction dir;
        try
        {
            dir = Direction.valueOf(direction);
        }
        catch(Exception e)
        {
            // Bidirectional platforms may not have a direction
            dir = Direction.NONE;
        }

        Stop stop = new Stop(stopId, description, latitude, longitude, dir);
        stops.put(stopId, stop);
        return stop;
    }
}

