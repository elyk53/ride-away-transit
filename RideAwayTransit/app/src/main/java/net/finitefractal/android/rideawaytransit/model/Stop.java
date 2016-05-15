package net.finitefractal.android.rideawaytransit.model;

import android.location.Location;

/**
 * Represents a single stop, possibly shared between multiple bus lines
 */
public class Stop
{
    private String m_id;
    private String m_name;
    private double m_latitude;
    private double m_longitude;
    private Direction m_direction;

    public Stop(String id, String name, double latitude, double longitude, Direction direction)
    {
        m_id = id;
        m_name = name;
        m_latitude = latitude;
        m_longitude = longitude;
        m_direction = direction;
    }

    public String getId() { return m_id; }
    public String getName() { return m_name; }
    public Direction getDirection() { return m_direction; }
    public double getLatitude() { return m_latitude; }
    public double getLongitude() { return m_longitude; }
    public Location getLocation()
    {
        Location loc = new Location("StopLocation"); // Arbitrary provider
        loc.setLatitude(m_latitude);
        loc.setLongitude(m_longitude);
        return loc;
    }
}
