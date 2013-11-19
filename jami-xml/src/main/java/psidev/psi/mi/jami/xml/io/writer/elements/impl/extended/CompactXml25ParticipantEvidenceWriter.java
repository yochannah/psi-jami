package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended;

import org.codehaus.stax2.XMLStreamWriter2;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.xml.PsiXml25ObjectCache;
import psidev.psi.mi.jami.xml.extension.ExperimentalInteractor;
import psidev.psi.mi.jami.xml.extension.ExtendedPsi25ParticipantEvidence;
import psidev.psi.mi.jami.xml.io.writer.elements.CompactPsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25ElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25ParameterWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXml25XrefWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.abstracts.AbstractXml25ParticipantEvidenceWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.Xml25FeatureEvidenceWriter;
import psidev.psi.mi.jami.xml.utils.PsiXml25Utils;

import javax.xml.stream.XMLStreamException;

/**
 * Compact XML 2.5 writer for an extended participant evidence with full experimental details and having experimental interactors, list of host organisms and list of experimental roles.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>14/11/13</pre>
 */

public class CompactXml25ParticipantEvidenceWriter extends AbstractXml25ParticipantEvidenceWriter implements CompactPsiXml25ElementWriter<ParticipantEvidence> {
    private CompactPsiXml25ElementWriter<ExperimentalInteractor> experimentalInteractorWriter;

    public CompactXml25ParticipantEvidenceWriter(XMLStreamWriter2 writer, PsiXml25ObjectCache objectIndex) {
        super(writer, objectIndex, new Xml25FeatureEvidenceWriter(writer, objectIndex));
        this.experimentalInteractorWriter = new CompactXml25ExperimentalInteractorWriter(writer, objectIndex);
    }

    public CompactXml25ParticipantEvidenceWriter(XMLStreamWriter2 writer, PsiXml25ObjectCache objectIndex,
                                                 PsiXml25ElementWriter<Alias> aliasWriter, PsiXml25XrefWriter primaryRefWriter,
                                                 PsiXml25XrefWriter secondaryRefWriter, PsiXml25ElementWriter<CvTerm> biologicalRoleWriter,
                                                 PsiXml25ElementWriter<FeatureEvidence> featureWriter, PsiXml25ElementWriter<Annotation> attributeWriter,
                                                 PsiXml25ElementWriter<Interactor> interactorWriter, CompactPsiXml25ElementWriter<ExperimentalInteractor> experimentalInteractorWriter, PsiXml25ElementWriter<CvTerm> experimentalPreparationWriter,
                                                 PsiXml25ElementWriter<CvTerm> identificationMethodWriter, PsiXml25ElementWriter<Confidence> confidenceWriter,
                                                 PsiXml25ElementWriter<CvTerm> experimentalRoleWriter, PsiXml25ElementWriter<Organism> hostOrganismWriter,
                                                 PsiXml25ParameterWriter parameterWriter) {
        super(writer, objectIndex, aliasWriter, primaryRefWriter, secondaryRefWriter, biologicalRoleWriter, featureWriter != null ? featureWriter : new Xml25FeatureEvidenceWriter(writer, objectIndex), attributeWriter, interactorWriter,
                experimentalPreparationWriter,identificationMethodWriter, confidenceWriter, experimentalRoleWriter, hostOrganismWriter, parameterWriter);
        this.experimentalInteractorWriter = experimentalInteractorWriter != null ? experimentalInteractorWriter : new CompactXml25ExperimentalInteractorWriter(writer, objectIndex);
    }

    @Override
    protected void writeMolecule(Interactor interactor) throws XMLStreamException {
        super.writeMoleculeRef(interactor);
    }

    @Override
    protected void writeExperimentalRoles(ParticipantEvidence object) throws XMLStreamException {
        ExtendedPsi25ParticipantEvidence xmlParticipant = (ExtendedPsi25ParticipantEvidence)object;
        getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
        getStreamWriter().writeStartElement("experimentalRoleList");
        getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
        for (CvTerm expRole : xmlParticipant.getExperimentalRoles()){
            getExperimentalRoleWriter().write(expRole);
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
        }
        getStreamWriter().writeEndElement();
        getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
    }

    @Override
    protected void writeHostOrganisms(ParticipantEvidence object) throws XMLStreamException {
        ExtendedPsi25ParticipantEvidence xmlParticipant = (ExtendedPsi25ParticipantEvidence)object;
        if (!xmlParticipant.getHostOrganisms().isEmpty()){
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            getStreamWriter().writeStartElement("hostOrganismList");
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            for (Organism host : xmlParticipant.getHostOrganisms()){
                getHostOrganismWriter().write(host);
                getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            getStreamWriter().writeEndElement();
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
        }
    }

    @Override
    protected void writeExperimentalInteractor(ParticipantEvidence object) throws XMLStreamException {
        ExtendedPsi25ParticipantEvidence xmlParticipant = (ExtendedPsi25ParticipantEvidence)object;
        if (!xmlParticipant.getExperimentalInteractors().isEmpty()){
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            getStreamWriter().writeStartElement("experimentalInteractorList");
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            for (ExperimentalInteractor expInt : xmlParticipant.getExperimentalInteractors()){
                this.experimentalInteractorWriter.write(expInt);
                getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            getStreamWriter().writeEndElement();
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
        }
    }

    @Override
    protected void writeNames(ParticipantEvidence object) throws XMLStreamException {
        NamedEntity xmlParticipant = (NamedEntity) object;
        // write names
        boolean hasShortLabel = xmlParticipant.getShortName() != null;
        boolean hasFullLabel = xmlParticipant.getFullName() != null;
        boolean hasAliases = !xmlParticipant.getAliases().isEmpty();
        if (hasShortLabel || hasFullLabel | hasAliases){
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            getStreamWriter().writeStartElement("names");
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            // write shortname
            if (hasShortLabel){
                getStreamWriter().writeStartElement("shortLabel");
                getStreamWriter().writeCharacters(xmlParticipant.getShortName());
                getStreamWriter().writeEndElement();
                getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write fullname
            if (hasFullLabel){
                getStreamWriter().writeStartElement("fullName");
                getStreamWriter().writeCharacters(xmlParticipant.getFullName());
                getStreamWriter().writeEndElement();
                getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write aliases
            for (Object alias : xmlParticipant.getAliases()){
                getAliasWriter().write((Alias)alias);
                getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write end names
            getStreamWriter().writeEndElement();
            getStreamWriter().writeCharacters(PsiXml25Utils.LINE_BREAK);
        }
    }
}