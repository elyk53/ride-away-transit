package net.finitefractal.android.rideawaytransit.model;

import java.util.*;

/**
 * Root container for all the lines of a particular transit agency
 */
public class TransitSystem
{
    private Agency m_agency;

    // Key: Line number/letter. Value: Route
    private Hashtable<String, Line> m_lines;

    public TransitSystem(Agency agency, Iterable<Line> lines)
    {
        m_agency = agency;
        m_lines = new Hashtable<String, Line>();
        for(Line l : lines)
            m_lines.put(l.getNumber(), l);
    }

    /**
     *
     * @return The agency responsible for this transit system
     */
    public Agency getAgency() { return m_agency; }

    /**
     *
     * @return All of the lines in this system
     */
    public Iterable<Line> getLines()
    {
        return m_lines.values();
    }

    /**
     * Gets the line with the given number, if it exists
     * @param number Number (i.e. short identifier) for the line to look up
     * @return The line associated with the given number, or {@code null} if it does not exist
     */
    public Line getLine(String number)
    {
        return m_lines.get(number);
    }
}
