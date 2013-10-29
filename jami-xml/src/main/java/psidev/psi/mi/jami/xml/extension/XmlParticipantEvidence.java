package psidev.psi.mi.jami.xml.extension;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Xml implementation of ParticipantEvidence
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/10/13</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
public class XmlParticipantEvidence extends AbstractXmlParticipant<InteractionEvidence, FeatureEvidence> implements ParticipantEvidence{

    @XmlLocation
    @XmlTransient
    private Locator locator;
    private boolean initialisedMethods = false;
    private XmlInteractionEvidence originalInteraction;
    private JAXBParticipantIdentificationWrapper jaxbParticipantionIdentificationWrapper;
    private JAXBExperimentalPreparationWrapper jaxbExperimentalPreparationWrapper;
    private JAXBExperimentalRoleWrapper jaxbExperimentalRoleWrapper;
    private JAXBExperimentalInteractorWrapper jaxbExperimentalInteractorWrapper;
    private JAXBHostOrganismWrapper jaxbHostOrganismWrapper;
    private JAXBConfidenceWrapper jaxbConfidenceWrapper;
    private JAXBParameterWrapper jaxbParameterWrapper;
    private List<ExperimentalCvTerm> originalIdentificationMethods;

    public XmlParticipantEvidence() {
    }

    public XmlParticipantEvidence(Interactor interactor) {
        super(interactor);
    }

    public XmlParticipantEvidence(Interactor interactor, CvTerm bioRole) {
        super(interactor, bioRole);
    }

    public XmlParticipantEvidence(Interactor interactor, Stoichiometry stoichiometry) {
        super(interactor, stoichiometry);
    }

    public XmlParticipantEvidence(Interactor interactor, CvTerm bioRole, Stoichiometry stoichiometry) {
        super(interactor, bioRole, stoichiometry);
    }

    protected void initialiseExperimentalPreparationWrapper() {
        this.jaxbExperimentalPreparationWrapper = new JAXBExperimentalPreparationWrapper();
    }

    protected void initialiseExperimentalRoleWrapper() {
        this.jaxbExperimentalRoleWrapper = new JAXBExperimentalRoleWrapper();
    }

    protected void initialiseConfidenceWrapper() {
        this.jaxbConfidenceWrapper = new JAXBConfidenceWrapper();
    }

    protected void initialiseParameterWrapper() {
        this.jaxbParameterWrapper = new JAXBParameterWrapper();
    }

    protected void initialiseIdentificationMethodWrapper(){
        Collection<Experiment> expToIgnore = Collections.EMPTY_LIST;

        if (this.jaxbParticipantionIdentificationWrapper == null){
            this.jaxbParticipantionIdentificationWrapper = new JAXBParticipantIdentificationWrapper();
        }

        if (originalInteraction != null){

            List<XmlExperiment> originalExperiments = originalInteraction.getOriginalExperiments();
            if (originalExperiments != null && !originalExperiments.isEmpty()){
                if (originalIdentificationMethods != null && !originalIdentificationMethods.isEmpty()){
                    expToIgnore = new ArrayList<Experiment>(originalIdentificationMethods.size());
                    for (ExperimentalCvTerm part : this.originalIdentificationMethods){
                        if (!part.getExperiments().isEmpty()){
                            expToIgnore.addAll(part.getExperiments());
                        }
                    }
                    this.originalIdentificationMethods = null;
                }

                for (XmlExperiment exp : originalExperiments){
                    if (exp.getParticipantIdentificationMethod() != null && !expToIgnore.contains(exp)){
                        this.jaxbParticipantionIdentificationWrapper.identificationMethods.add(exp.getParticipantIdentificationMethod());
                    }
                }
            }
        }

        initialisedMethods = true;
    }

    protected void initialiseHostOrganismWrapper() {
        this.jaxbHostOrganismWrapper = new JAXBHostOrganismWrapper();
    }

    protected void initialiseExperimentalInteractorWrapper() {
        this.jaxbExperimentalInteractorWrapper = new JAXBExperimentalInteractorWrapper();
    }

    public CvTerm getExperimentalRole() {
        if (this.jaxbExperimentalRoleWrapper == null){
            initialiseExperimentalRoleWrapper();
        }
        if (this.jaxbExperimentalRoleWrapper.experimentalRoles.isEmpty()){
            this.jaxbExperimentalRoleWrapper.experimentalRoles.add(new XmlCvTerm(Participant.UNSPECIFIED_ROLE, Participant.UNSPECIFIED_ROLE_MI));
        }
        return this.jaxbExperimentalRoleWrapper.experimentalRoles.get(0);
    }

    public void setExperimentalRole(CvTerm expRole) {
        if (this.jaxbExperimentalRoleWrapper == null && expRole != null){
            initialiseExperimentalRoleWrapper();
            this.jaxbExperimentalRoleWrapper.experimentalRoles.add(expRole);
        }
        else if (expRole != null){
            if (!this.jaxbExperimentalRoleWrapper.experimentalRoles.isEmpty()){
                this.jaxbExperimentalRoleWrapper.experimentalRoles.remove(0);
            }
            this.jaxbExperimentalRoleWrapper.experimentalRoles.add(0, expRole);
        }
        else{
            if (!this.jaxbExperimentalRoleWrapper.experimentalRoles.isEmpty()){
                this.jaxbExperimentalRoleWrapper.experimentalRoles.remove(0);
            }
        }
    }

    public Collection<CvTerm> getIdentificationMethods() {
        if (!initialisedMethods){
            initialiseIdentificationMethodWrapper();
        }
        return this.jaxbParticipantionIdentificationWrapper.identificationMethods;
    }

    public Collection<CvTerm> getExperimentalPreparations() {
        if (jaxbExperimentalPreparationWrapper == null){
            initialiseExperimentalPreparationWrapper();
        }
        return this.jaxbExperimentalPreparationWrapper.experimentalPreparations;
    }

    public Organism getExpressedInOrganism() {
        return (this.jaxbHostOrganismWrapper != null && !this.jaxbHostOrganismWrapper.hostOrganisms.isEmpty())? this.jaxbHostOrganismWrapper.hostOrganisms.iterator().next() : null;
    }

    public void setExpressedInOrganism(Organism organism) {
        if (this.jaxbHostOrganismWrapper == null && organism != null){
            initialiseHostOrganismWrapper();
            this.jaxbHostOrganismWrapper.hostOrganisms.add(organism);
        }
        else if (organism != null){
            if (!this.jaxbHostOrganismWrapper.hostOrganisms.isEmpty()){
                this.jaxbHostOrganismWrapper.hostOrganisms.remove(0);
            }
            this.jaxbHostOrganismWrapper.hostOrganisms.add(0, organism);
        }
        else{
            if (!this.jaxbHostOrganismWrapper.hostOrganisms.isEmpty()){
                this.jaxbHostOrganismWrapper.hostOrganisms.remove(0);
            }
        }
    }

    public Collection<Confidence> getConfidences() {
        if (jaxbConfidenceWrapper == null){
            initialiseConfidenceWrapper();
        }
        return this.jaxbConfidenceWrapper.confidences;
    }

    public Collection<Parameter> getParameters() {
        if (this.jaxbParameterWrapper == null){
            initialiseParameterWrapper();
        }
        return this.jaxbParameterWrapper.parameters;
    }

    public Collection<ExperimentalInteractor> getExperimentalInteractors() {
        if (this.jaxbExperimentalInteractorWrapper == null){
            initialiseExperimentalInteractorWrapper();
        }
        return this.jaxbExperimentalInteractorWrapper.experimentalInteractors;
    }

    @Override
    public String toString() {
        return super.toString() + (getExperimentalRole() != null ? ", " + getExperimentalRole().toString() : "") + (getExpressedInOrganism() != null ? ", " + getExpressedInOrganism().toString() : "");
    }

    @Override
    @XmlElement(name = "names")
    public void setJAXBNames(NamesContainer value) {
        super.setJAXBNames(value);
    }

    @Override
    @XmlElement(name = "xref")
    public void setJAXBXref(XrefContainer value) {
        super.setJAXBXref(value);
    }

    @Override
    @XmlElement(name = "interactor")
    public void setJAXBInteractor(XmlInteractor interactor) {
        super.setJAXBInteractor(interactor);
    }

    @Override
    @XmlElement(name = "interactionRef")
    public void setJAXBInteractionRef(Integer value) {
        super.setJAXBInteractionRef(value);
    }

    @Override
    @XmlElement(name = "interactorRef")
    public void setJAXBInteractorRef(Integer value) {
        super.setJAXBInteractorRef(value);
    }

    @Override
    @XmlElement(name = "biologicalRole", type = XmlCvTerm.class)
    public void setJAXBBiologicalRole(CvTerm bioRole) {
        super.setJAXBBiologicalRole(bioRole);
    }

    @Override
    @XmlAttribute(name = "id", required = true)
    public void setJAXBId(int value) {
        super.setJAXBId(value);
    }

    @Override
    @XmlElement(name="attributeList")
    public void setJAXBAttributeWrapper(JAXBAttributeWrapper jaxbAttributeWrapper) {
        super.setJAXBAttributeWrapper(jaxbAttributeWrapper);
    }

    @XmlElement(name = "featureList")
    public void setFeatureWrapper(JAXBFeatureWrapper jaxbFeatureWrapper) {
        super.setFeatureWrapper(jaxbFeatureWrapper);
    }

    @XmlElement(name="participantIdentificationMethodList")
    public void setJAXBParticipantIdentificationMethodWrapper(JAXBParticipantIdentificationWrapper wrapper) {
        this.jaxbParticipantionIdentificationWrapper = wrapper;
        // needs to initialise originalIdentificationMethods
        if (this.jaxbParticipantionIdentificationWrapper != null && !this.jaxbParticipantionIdentificationWrapper.identificationMethods.isEmpty()){
            this.originalIdentificationMethods = new ArrayList<ExperimentalCvTerm>(this.jaxbParticipantionIdentificationWrapper.identificationMethods.size());
            for (CvTerm v : this.jaxbParticipantionIdentificationWrapper.identificationMethods){
                this.originalIdentificationMethods.add((ExperimentalCvTerm)v);
            }
        }
    }

    @XmlElement(name="experimentalRoleList")
    public void setJAXBExperimentalRoleWrapper(JAXBExperimentalRoleWrapper wrapper) {
        this.jaxbExperimentalRoleWrapper = wrapper;
    }

    @XmlElement(name="experimentalPreparationList")
    public void setJAXBExperimentalPreparationWrapper(JAXBExperimentalPreparationWrapper wrapper) {
        this.jaxbExperimentalPreparationWrapper = wrapper;
    }

    @XmlElement(name="experimentalInteractorList")
    public void setExperimentalInteractorWrapper(JAXBExperimentalInteractorWrapper wrapper) {
        this.jaxbExperimentalInteractorWrapper = wrapper;
    }

    @XmlElement(name="hostOrganismList")
    public void setJAXBHostOrganismWrapper(JAXBHostOrganismWrapper wrapper) {
        this.jaxbHostOrganismWrapper = wrapper;
    }

    @XmlElement(name="parameterList")
    public void setJAXBParameterWrapper(JAXBParameterWrapper wrapper) {
        this.jaxbParameterWrapper = wrapper;
    }

    @XmlElement(name="confidenceList")
    public void setJAXBConfidenceWrapper(JAXBConfidenceWrapper wrapper) {
        this.jaxbConfidenceWrapper = wrapper;
    }

    @Override
    public FileSourceLocator getSourceLocator() {
        if (super.getSourceLocator() == null && locator != null){
            super.setSourceLocator(new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), getJAXBId()));
        }
        return super.getSourceLocator();
    }

    @Override
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            super.setSourceLocator(null);
        }
        else{
            super.setSourceLocator(new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), getJAXBId()));
        }
    }

    @Override
    public void processAddedFeature(FeatureEvidence feature) {
        ((XmlFeatureEvidence)feature).setOriginalParticipant(this);
    }

    protected void setOriginalXmlInteraction(XmlInteractionEvidence i){
        this.originalInteraction = i;
        setInteraction(i);
    }

    protected XmlInteractionEvidence getOriginalInteraction() {
        return originalInteraction;
    }

    @Override
    protected void initialiseFeatureWrapper() {
        super.setFeatureWrapper(new JAXBFeatureWrapper());
    }

    ////////////////////////////////////////////////////// classes
    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceFeatureWrapper")
    public static class JAXBFeatureWrapper extends AbstractXmlEntity.JAXBFeatureWrapper<FeatureEvidence> {

        public JAXBFeatureWrapper(){
            super();
        }

        @XmlElement(type=XmlFeatureEvidence.class, name="feature", required = true)
        public List<FeatureEvidence> getJAXBFeatures() {
            return super.getJAXBFeatures();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceIdentificationWrapper")
    public static class JAXBParticipantIdentificationWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<CvTerm> identificationMethods;

        public JAXBParticipantIdentificationWrapper(){
            initialiseIdentificationMethods();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=ExperimentalCvTerm.class, name="participantIdentificationMethod", required = true)
        public List<CvTerm> getJAXBParticipantIdentificationMethods() {
            return this.identificationMethods;
        }

        protected void initialiseIdentificationMethods(){
            this.identificationMethods = new ArrayList<CvTerm>();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidencePreparationWrapper")
    public static class JAXBExperimentalPreparationWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<CvTerm> experimentalPreparations;

        public JAXBExperimentalPreparationWrapper(){
            initialiseExperimentalPreparations();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=ExperimentalCvTerm.class, name="experimentalPreparation", required = true)
        public List<CvTerm> getJAXBExperimentalPreparations() {
            return this.experimentalPreparations;
        }

        protected void initialiseExperimentalPreparations(){
            this.experimentalPreparations = new ArrayList<CvTerm>();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceRoleWrapper")
    public static class JAXBExperimentalRoleWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<CvTerm> experimentalRoles;

        public JAXBExperimentalRoleWrapper(){
            initialiseExperimentalRoles();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=ExperimentalCvTerm.class, name="experimentalRole", required = true)
        public List<CvTerm> getJAXBExperimentalRoles() {
            return this.experimentalRoles;
        }

        protected void initialiseExperimentalRoles(){
            this.experimentalRoles = new ArrayList<CvTerm>();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceInteractorWrapper")
    public static class JAXBExperimentalInteractorWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<ExperimentalInteractor> experimentalInteractors;

        public JAXBExperimentalInteractorWrapper(){
            initialiseExperimentalIneteractors();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=ExperimentalInteractor.class, name="experimentalInteractor", required = true)
        public List<ExperimentalInteractor> getJAXBExperimentalInteractors() {
            return this.experimentalInteractors;
        }

        protected void initialiseExperimentalIneteractors(){
            this.experimentalInteractors = new ArrayList<ExperimentalInteractor>();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceOrganismWrapper")
    public static class JAXBHostOrganismWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<Organism> hostOrganisms;

        public JAXBHostOrganismWrapper(){
            initialiseHostOrganisms();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=HostOrganism.class, name="hostOrganism", required = true)
        public List<Organism> getJAXBHostOrganisms() {
            return this.hostOrganisms;
        }

        protected void initialiseHostOrganisms(){
            this.hostOrganisms = new ArrayList<Organism>();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceConfidenceWrapper")
    public static class JAXBConfidenceWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<Confidence> confidences;

        public JAXBConfidenceWrapper(){
            initialiseConfidences();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=XmlConfidence.class, name="confidence", required = true)
        public List<Confidence> getJAXBConfidences() {
            return this.confidences;
        }

        protected void initialiseConfidences(){
            this.confidences = new ArrayList<Confidence>();
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="participantEvidenceParameterWrapper")
    public static class JAXBParameterWrapper implements Locatable, FileSourceContext {
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<Parameter> parameters;

        public JAXBParameterWrapper(){
            initialiseParameters();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        @XmlElement(type=XmlParameter.class, name="parameter", required = true)
        public List<Parameter> getJAXBParameters() {
            return this.parameters;
        }

        protected void initialiseParameters(){
            this.parameters = new ArrayList<Parameter>();
        }
    }
}