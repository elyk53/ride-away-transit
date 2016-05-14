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
    private String m_number;
    private String m_name;
    private Agency m_agency;
    private ArrayList<Route> m_routes;

    /**
     * Constructs a new line.
     *
     * @param agency The transit agency which runs this line
     * @param number The "number" of this line. More strictly speaking, this is the short identifier
     *               for the line, and may be a color or a letter in the case of some rail lines
     * @param name Descriptive name of the line - often a pair of destinations or the primary
     *             streets that the line traverses
     * @param routes The routes which this line runs
     */
    public Line(Agency agency, String number, String name, Collection<Route> routes)
    {
        m_agency = agency;
        m_number = number;
        m_name = name;
        m_routes = new ArrayList<Route>(routes);
    }

    public String getNumber() { return m_number; }
    public String getName() { return m_name; }
    public Agency getAgency() { return m_agency; }
    public Iterable<Route> getRoutes() { return m_routes; }
}
