package psidev.psi.mi.jami.xml.io.writer.elements.impl.compact.xml25;

import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;
import psidev.psi.mi.jami.xml.io.writer.elements.CompactPsiXmlElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.XmlFeatureWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.abstracts.AbstractXml25ParticipantWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Compact XML 2.5 writer for a basic participant (ignore experimental details)
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>14/11/13</pre>
 */

public class CompactXmlParticipantWriter extends AbstractXml25ParticipantWriter<Participant, Feature> implements CompactPsiXmlElementWriter<Participant> {
    public CompactXmlParticipantWriter(XMLStreamWriter writer, PsiXmlObjectCache objectIndex) {
        super(writer, objectIndex);
    }

    @Override
    protected void initialiseFeatureWriter() {
        super.setFeatureWriter( new XmlFeatureWriter(getStreamWriter(), getObjectIndex()));
    }

    @Override
    protected void writeMolecule(Interactor interactor) throws XMLStreamException {
        super.writeMoleculeRef(interactor);
    }

    @Override
    protected void writeExperimentalPreparations(Participant object) {
        // nothing to do
    }

    @Override
    protected void writeExperimentalRoles(Participant object) {
        // nothing to do
    }

    @Override
    protected void writeParticipantIdentificationMethods(Participant object) {
        // nothing to do
    }

    @Override
    protected void writeExperimentalInteractor(Participant object) {
        // nothing to do
    }

    @Override
    protected void writeHostOrganisms(Participant object) {
        // nothing to do
    }

    @Override
    protected void writeConfidences(Participant object) {
        // nothing to do
    }

    @Override
    protected void writeParameters(Participant object) {
        // nothing to do
    }
}