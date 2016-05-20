package net.finitefractal.android.rideawaytransit.parser;

import net.finitefractal.android.rideawaytransit.model.*;

import org.junit.*;

import java.io.InputStream;

/**
 * Tests for parsing of Trimet "routeConfig" xml file.
 */
public class TrimetParserTest
{
    private static final String ROUTES_FILE = "trimetRoutes.xml";

    private TransitSystem m_system;

    @Before
    public void loadSystem()
    {
        TrimetParser parser = new TrimetParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ROUTES_FILE);
        m_system = parser.loadFromXml(inputStream);
        Assert.assertNotNull(m_system);
    }

    @Test
    public void TestAgency()
    {
        Assert.assertEquals(Agency.TRIMET, m_system.getAgency());
    }

    @Test
    public void TestMaxLines()
    {
        // Make sure all the MAX lines were parsed. Serves as an easy to remember
        // sanity check that parsing didn't stop pre-maturely.
        boolean seenRed, seenBlue, seenYellow, seenGreen, seenOrange;
        seenRed = seenBlue = seenYellow = seenGreen = seenOrange = false;
        for (Line line : m_system.getLines())
        {
            String lineName = line.getName();
            switch (lineName)
            {
                case "MAX Blue Line":
                    seenBlue = true;
                    break;
                case "MAX Red Line":
                    seenRed = true;
                    break;
                case "MAX Yellow Line":
                    seenYellow = true;
                    break;
                case "MAX Green Line":
                    seenGreen = true;
                    break;
                case "MAX Orange Line":
                    seenOrange = true;
                    break;
                default:
                    continue;
            }
            Assert.assertEquals(
                    "Wrong number of routes for line " + lineName, 2, line.getNumRoutes());
        }
        Assert.assertTrue("Did not find blue line", seenBlue);
        Assert.assertTrue("Did not find red line", seenRed);
        Assert.assertTrue("Did not find yellow line", seenYellow);
        Assert.assertTrue("Did not find green line", seenGreen);
        Assert.assertTrue("Did not find orange line", seenOrange);
    }

    // Make sure we can retrieve line 4 and that it has the right description
    @Test
    public void TestLine4Descriptions()
    {
        Line line4 = m_system.getLine("4");
        String desc = line4.getName();
        Assert.assertEquals("Invalid line 4 name", "4-Division/Fessenden", desc);
    }

    // Make sure we can retrieve line 35 and that it has the right start and
    // end points for both directions
    @Test
    public void TestLine35Directions()
    {
        Line line35 = m_system.getLine("35");
        Assert.assertEquals("Wrong number of routes for line 35 has", 2, line35.getNumRoutes());
        Route southBound = line35.getRoute(0);
        Route northBound = line35.getRoute(1);

        String southBoundDest = southBound.getDestination();
        String northBoundDest = northBound.getDestination();

        Assert.assertEquals("Incorrect southbound destination for line 35",
                "To Oregon City Transit Center", southBoundDest);
        Assert.assertEquals("Incorrect northbound destination for line 35",
                "To University of Portland", northBoundDest);

        Stop southFirstStop = southBound.getStop(0);
        Stop northFirstStop = northBound.getStop(0);

        String southStopName = southFirstStop.getName();
        String southStopId = southFirstStop.getId();
        Direction southDirection = southFirstStop.getDirection();
        String northStopName = northFirstStop.getName();
        String northStopId = northFirstStop.getId();
        Direction northDirection = northFirstStop.getDirection();

        Assert.assertEquals("Incorrect first southbound stop",
                "N Portsmouth & Willamette", southStopName);
        Assert.assertEquals("Incorrect first southbound stop ID", "9635", southStopId);
        Assert.assertEquals("Incorrect southbound stop direction", Direction.SOUTH, southDirection);
        Assert.assertEquals("Incorrect first northbound stop", "Oregon City Transit Center", northStopName);
        Assert.assertEquals("Incorrect northbound first stop ID", "8762", northStopId);
        // Yes west, not North - stop direction doesn't necessarily match the overall route direction
        Assert.assertEquals("Incorrect northBound stop direction", Direction.WEST, northDirection);
    }

    @Test
    public void TestStopOverlap()
    {
        // When multiple routes share a stop, they should point to the same Stop object
        // Line 54's start point in one direction is the same as its end point in the other
        Line line54 = m_system.getLine("54");
        Route toBeaverton = line54.getRoute(0);
        Route toPortland = line54.getRoute(1);
        Stop burnsideBroadway0 = toBeaverton.getStop(0);
        Stop burnsideBroadway1 = toPortland.getStop(toPortland.numStops() - 1);
        Assert.assertSame("Shared stops are not the same object", burnsideBroadway0, burnsideBroadway1);
    }
}
