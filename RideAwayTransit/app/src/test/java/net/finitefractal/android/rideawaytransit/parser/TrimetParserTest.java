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

    private TransitSystem loadSystem()
    {
        TrimetParser parser = new TrimetParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ROUTES_FILE);
        TransitSystem result = parser.loadFromXml(inputStream);
        Assert.assertNotNull(result);
        return result;
    }

    @Test
    public void TestAgency()
    {
        TransitSystem system = loadSystem();
        Assert.assertEquals(system.getAgency(), Agency.TRIMET);
    }
}
