package net.finitefractal.android.rideawaytransit.model;

import java.util.*;

/**
 * Represents a single bus line. A line consists of:
 * * Name
 * * Number
 * * Agency
 * * One or more Routes
 * A line can be uniquely identified by its number and agency
 */
public class Line
{
   private int m_number;
    private String m_name;
    private Agency m_agency;
    private ArrayList<Route> m_routes;

    public Line(Agency agency, int number, String name, Collection<Route> routes)
    {
        m_agency = agency;
        m_number = number;
        m_name = name;
        m_routes = new ArrayList<Route>();
    }

    public int getNumber() { return m_number; }
    public String getName() { return m_name; }
    public Agency getAgency() { return m_agency; }
    public Iterable<Route> getRoutes() { return m_routes; }
}
