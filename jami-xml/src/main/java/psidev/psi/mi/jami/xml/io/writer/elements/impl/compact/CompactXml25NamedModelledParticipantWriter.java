package psidev.psi.mi.jami.xml.io.writer.elements.impl.compact;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.xml.PsiXml25ObjectCache;
import psidev.psi.mi.jami.xml.io.writer.elements.CompactPsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25XrefWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.Xml25ModelledFeatureWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.abstracts.AbstractXml25NamedParticipantWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Compact XML 2.5 writer for a named modelled participant having a fullname and a shortname.
 * It ignores experimental details.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>14/11/13</pre>
 */

public class CompactXml25NamedModelledParticipantWriter extends AbstractXml25NamedParticipantWriter<ModelledParticipant, ModelledFeature> implements CompactPsiXml25ElementWriter<ModelledParticipant> {
    public CompactXml25NamedModelledParticipantWriter(XMLStreamWriter writer, PsiXml25ObjectCache objectIndex) {
        super(writer, objectIndex, new Xml25ModelledFeatureWriter(writer, objectIndex));
    }

    public CompactXml25NamedModelledParticipantWriter(XMLStreamWriter writer, PsiXml25ObjectCache objectIndex,
                                                      PsiXml25ElementWriter<Alias> aliasWriter, PsiXml25XrefWriter primaryRefWriter,
                                                      PsiXml25XrefWriter secondaryRefWriter, PsiXml25ElementWriter<Interactor> interactorWriter,
                                                      PsiXml25ElementWriter<CvTerm> biologicalRoleWriter, PsiXml25ElementWriter<ModelledFeature> featureWriter,
                                                      PsiXml25ElementWriter<Annotation> attributeWriter) {
        super(writer, objectIndex, aliasWriter, primaryRefWriter, secondaryRefWriter, interactorWriter, biologicalRoleWriter,
                featureWriter != null ? featureWriter : new Xml25ModelledFeatureWriter(writer, objectIndex), attributeWriter);
    }

    @Override
    protected void writeMolecule(Interactor interactor) throws XMLStreamException {
        super.writeMoleculeRef(interactor);
    }

    @Override
    protected void writeExperimentalPreparations(ModelledParticipant object) {
        // nothing to do
    }

    @Override
    protected void writeExperimentalRoles(ModelledParticipant object) {
        // nothing to do
    }

    @Override
    protected void writeParticipantIdentificationMethods(ModelledParticipant object) {
        // nothing to do
    }

    @Override
    protected void writeExperimentalInteractor(ModelledParticipant object) {
        // nothing to do
    }

    @Override
    protected void writeHostOrganisms(ModelledParticipant object) {
        // nothing to do
    }

    @Override
    protected void writeConfidences(ModelledParticipant object) {
        // nothing to do
    }

    @Override
    protected void writeParameters(ModelledParticipant object) {
        // nothing to do
    }
}