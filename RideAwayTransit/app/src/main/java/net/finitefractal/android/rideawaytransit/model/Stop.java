package net.finitefractal.android.rideawaytransit.model;

import android.location.Location;

/**
 * Represents a single stop, possibly shared between multiple bus lines
 */
public class Stop
{
    private int m_id;
    private String m_name;
    private Location m_location;
    private Direction m_direction;

    public Stop(int id, String name, Location location, Direction direction)
    {
        m_id = id;
        m_name = name;
        m_location = location;
        m_direction = direction;
    }

    public int getId() { return m_id; }
    public String getName() { return m_name; }
    public Location getLocation() { return m_location; }
    public Direction getDirection() { return m_direction; }
}
