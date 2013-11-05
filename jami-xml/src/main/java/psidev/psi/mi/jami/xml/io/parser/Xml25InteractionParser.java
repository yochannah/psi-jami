package psidev.psi.mi.jami.xml.io.parser;

import psidev.psi.mi.jami.datasource.DefaultFileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.xml.XmlEntryContext;
import psidev.psi.mi.jami.xml.exception.PsiXmlParserException;
import psidev.psi.mi.jami.xml.extension.*;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.URL;

/**
 * Parser generating basic interaction objects and ignore experimental details
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>16/10/13</pre>
 */

public class Xml25InteractionParser extends AbstractPsiXml25Parser<Interaction<? extends Participant>>{
    public Xml25InteractionParser(File file) throws FileNotFoundException, XMLStreamException, JAXBException {
        super(file);
    }

    public Xml25InteractionParser(InputStream inputStream) throws XMLStreamException, JAXBException {
        super(inputStream);
    }

    public Xml25InteractionParser(URL url) throws IOException, XMLStreamException, JAXBException {
        super(url);
    }

    public Xml25InteractionParser(Reader reader) throws XMLStreamException, JAXBException {
        super(reader);
    }

    @Override
    protected Unmarshaller createJAXBUnmarshaller() throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(XmlBasicInteraction.class, XmlExperiment.class, XmlInteractor.class,
                Availability.class, XmlSource.class, XmlAnnotation.class);
        return ctx.createUnmarshaller();
    }

    @Override
    protected void parseExperimentList() throws XMLStreamException, JAXBException {
        // read experiment list
        StartElement experimentList = (StartElement)getEventReader().nextEvent();
        setCurrentElement(peekNextPsiXml25Element());
        // process experiments. Each experiment will be loaded in entryContext so no needs to do something else
        if (getCurrentElement() != null){
            XMLEvent evt = getCurrentElement();
            String name = null;
            // skip experimentDescription up to the end of experiment list
            while (evt != null && (name == null || (name != null && !PsiXmlUtils.EXPERIMENTLIST_TAG.equals(name)))) {
                while (evt != null && !evt.isEndElement()){
                    skipNextElement();
                    evt = getSubEventReader().peek();
                }

                if (evt != null && evt.isEndElement()){
                    name = ((EndElement)evt).getName().getLocalPart();
                    skipNextElement();
                    evt = getSubEventReader().peek();
                }
            }
        }
        else{
            if (getListener() != null){
                FileSourceContext context = null;
                if (experimentList.getLocation() != null){
                    Location loc = experimentList.getLocation();
                    context = new DefaultFileSourceContext(new PsiXmLocator(loc.getLineNumber(), loc.getColumnNumber(), null));
                }
                getListener().onInvalidSyntax(context, new PsiXmlParserException("ExperimentList element does not contain any experimentDescription node. PSI-XML is not valid."));
            }
        }
        setCurrentElement(peekNextPsiXml25Element());
    }

    @Override
    protected void parseAvailabilityList(XmlEntryContext entryContext) throws XMLStreamException, JAXBException {
        // read availabilityList
        StartElement availabilityList = (StartElement)getEventReader().nextEvent();
        setCurrentElement(peekNextPsiXml25Element());
        // process experiments. Each experiment will be loaded in entryContext so no needs to do something else
        if (getCurrentElement() != null){
            XMLEvent evt = getCurrentElement();
            String name = null;
            // skip experimentDescription up to the end of experiment list
            while (evt != null && (name == null || (name != null && !PsiXmlUtils.AVAILABILITYLIST_TAG.equals(name)))) {
                while (evt != null && !evt.isEndElement()){
                    skipNextElement();
                    evt = getSubEventReader().peek();
                }

                if (evt != null && evt.isEndElement()){
                    name = ((EndElement)evt).getName().getLocalPart();
                    skipNextElement();
                    evt = getSubEventReader().peek();
                }
            }
        }
        else{
            if (getListener() != null){
                FileSourceContext context = null;
                if (availabilityList.getLocation() != null){
                    Location loc = availabilityList.getLocation();
                    context = new DefaultFileSourceContext(new PsiXmLocator(loc.getLineNumber(), loc.getColumnNumber(), null));
                }
                getListener().onInvalidSyntax(context, new PsiXmlParserException("AvailabilityList element does not contain any availability node. PSI-XML is not valid."));
            }
        }
        setCurrentElement(peekNextPsiXml25Element());
    }
}
