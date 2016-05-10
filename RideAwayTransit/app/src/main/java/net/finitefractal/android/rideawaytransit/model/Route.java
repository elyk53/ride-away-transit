package net.finitefractal.android.rideawaytransit.model;

import java.util.*;

/**
 * Represents a single route.
 * A route is the set of stops that a line visits in a particular direction
 */
public class Route
{
    String m_destination;
    ArrayList<Stop> m_stops;

    // Stops does not follow our normal idiom of taking any collection, because ordering
    // of the stop list is critical
    public Route(String destination, List<Stop> stops)
    {
        m_destination = destination;
        m_stops = new ArrayList<Stop>(stops);
    }

    public String getDestination() { return m_destination; }
    public int numStops() { return m_stops.size(); }
    public Stop getStop(int i) { return m_stops.get(i); }
    public Iterable<Stop> getStops() { return m_stops; }
}
