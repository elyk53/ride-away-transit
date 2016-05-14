package net.finitefractal.android.rideawaytransit.parser;

import net.finitefractal.android.rideawaytransit.model.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * Defines the basis for the parser of any transit agency, and provides some common
 * helper methods to make parsing easier
 */
public abstract class AgencyParser
{
    private Agency m_agency;
    protected AgencyParser(Agency agency)
    {
        m_agency = agency;
    }

    public Agency getAgency() { return m_agency; }

    public abstract TransitSystem loadFromWeb();

    protected Document loadXmlDocument(InputStream stream)
            throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        // We deliberately don't validate for now because Trimet's XSD does not match their XML
        // e.g. Stop elements have a "dir" attribute which is not listed. Revisit this
        // if we add another agency which provides an XSD that validates successfully
        return factory.newDocumentBuilder().parse(stream);
    }
}
