package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended.xml25;

import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.extended.XmlCvTermWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.extended.XmlDbXrefWriter;

import javax.xml.stream.XMLStreamWriter;

/**
 * XML 2.5 writer for a basic feature (ignore experimental details)
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>13/11/13</pre>
 */

public class XmlFeatureWriter extends psidev.psi.mi.jami.xml.io.writer.elements.impl.xml25.XmlFeatureWriter {
    public XmlFeatureWriter(XMLStreamWriter writer, PsiXmlObjectCache objectIndex) {
        super(writer, objectIndex);
    }

    @Override
    protected void initialiseXrefWriter(){
        super.setXrefWriter(new XmlDbXrefWriter(getStreamWriter()));
    }

    @Override
    protected void initialiseRangeWriter() {
        super.setRangeWriter(new XmlRangeWriter(getStreamWriter()));
    }

    @Override
    protected void initialiseFeatureTypeWriter() {
        super.setFeatureTypeWriter(new XmlCvTermWriter(getStreamWriter()));
    }
}
