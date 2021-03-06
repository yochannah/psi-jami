package psidev.psi.mi.jami.xml.model.extension;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.ChecksumUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;
import psidev.psi.mi.jami.xml.XmlEntryContext;
import psidev.psi.mi.jami.xml.listener.PsiXmlParserListener;
import psidev.psi.mi.jami.xml.model.Entry;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Abstract class for xml interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>09/07/13</pre>
 */
@XmlTransient
public abstract class AbstractXmlInteraction<T extends Participant> implements FileSourceContext, Locatable, NamedInteraction<T>{

    private NamesContainer namesContainer;
    private InteractionXrefContainer xrefContainer;
    private Boolean intraMolecular;
    private int id;
    private Date updatedDate;
    private Date createdDate;
    private PsiXmlLocator sourceLocator;
    private Entry entry;

    private JAXBAttributeWrapper jaxbAttributeWrapper;
    private JAXBParticipantWrapper<T> jaxbParticipantWrapper;

    public AbstractXmlInteraction(){
        XmlEntryContext context = XmlEntryContext.getInstance();
        this.entry = context.getCurrentEntry();
    }

    public AbstractXmlInteraction(String shortName){
        XmlEntryContext context = XmlEntryContext.getInstance();
        this.entry = context.getCurrentEntry();
        setShortName(shortName);
    }

    public AbstractXmlInteraction(String shortName, CvTerm type){
        this(shortName);
        setInteractionType(type);
    }

    public String getShortName() {
        return this.namesContainer != null ? this.namesContainer.getShortLabel() : null;
    }

    public void setShortName(String name) {
        if (this.namesContainer == null){
            this.namesContainer = new NamesContainer();
        }
        this.namesContainer.setShortLabel(name);
    }

    @Override
    public List<Alias> getAliases() {
        if (this.namesContainer == null){
            this.namesContainer = new NamesContainer();
        }
        return this.namesContainer.getAliases();
    }

    @Override
    public void setFullName(String name) {
        if (this.namesContainer == null){
            this.namesContainer = new NamesContainer();
        }
        this.namesContainer.setFullName(name);
    }

    @Override
    public String getFullName() {
        return this.namesContainer != null ? this.namesContainer.getFullName() : null;
    }

    public String getRigid() {
        Checksum rigid = this.jaxbAttributeWrapper != null ? this.jaxbAttributeWrapper.rigid : null;
        return rigid != null ? rigid.getValue() : null;
    }

    public void setRigid(String rigid) {
        JAXBAttributeWrapper.InteractionChecksumList checksums = (JAXBAttributeWrapper.InteractionChecksumList)getChecksums();
        Checksum rigidAnnot = jaxbAttributeWrapper.rigid;

        // add new rigid if not null
        if (rigid != null){
            CvTerm rigidTopic = CvTermUtils.createRigid();
            // first remove old rigid if not null
            if (rigidAnnot != null){
                checksums.removeOnly(rigidAnnot);
            }
            jaxbAttributeWrapper.rigid = new XmlChecksum(rigidTopic, rigid);
            checksums.addOnly(jaxbAttributeWrapper.rigid);
        }
        // remove all rigid if the collection is not empty
        else if (!checksums.isEmpty()) {
            ChecksumUtils.removeAllChecksumWithMethod(checksums, Checksum.RIGID_MI, Checksum.RIGID);
            jaxbAttributeWrapper.rigid = null;
        }
    }

    public Collection<Xref> getIdentifiers() {
        if (xrefContainer == null){
            xrefContainer = new InteractionXrefContainer();
        }
        return this.xrefContainer.getIdentifiers();
    }

    public Collection<Xref> getXrefs() {
        if (xrefContainer == null){
            xrefContainer = new InteractionXrefContainer();
        }
        return this.xrefContainer.getXrefs();
    }

    public Collection<Checksum> getChecksums() {
        if (this.jaxbAttributeWrapper == null){
            initialiseAnnotationWrapper();
        }
        return this.jaxbAttributeWrapper.checksums;
    }

    public Collection<Annotation> getAnnotations() {
        if (this.jaxbAttributeWrapper == null){
            initialiseAnnotationWrapper();
        }
        return this.jaxbAttributeWrapper.annotations;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Date updated) {
        this.updatedDate = updated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }

    public abstract CvTerm getInteractionType();

    public abstract void setInteractionType(CvTerm term);

    public Collection<T> getParticipants() {
        if (jaxbParticipantWrapper == null){
            initialiseParticipantWrapper();
        }
        return this.jaxbParticipantWrapper.participants;
    }

    public boolean addParticipant(T part) {
        if (part == null){
            return false;
        }
        if (getParticipants().add(part)){
            part.setInteraction(this);
            return true;
        }
        return false;
    }

    public boolean removeParticipant(T part) {
        if (part == null){
            return false;
        }
        if (getParticipants().remove(part)){
            part.setInteraction(null);
            return true;
        }
        return false;
    }

    public boolean addAllParticipants(Collection<? extends T> participants) {
        if (participants == null){
            return false;
        }

        boolean added = false;
        for (T p : participants){
            if (addParticipant(p)){
                added = true;
            }
        }
        return added;
    }

    public boolean removeAllParticipants(Collection<? extends T> participants) {
        if (participants == null){
            return false;
        }

        boolean removed = false;
        for (T p : participants){
            if (removeParticipant(p)){
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Sets the value of the names property.
     *
     * @param value
     *     allowed object is
     *     {@link psidev.psi.mi.jami.xml.model.extension.NamesContainer }
     *
     */
    public void setJAXBNames(NamesContainer value) {
        this.namesContainer = value;
    }

    /**
     * Sets the value of the xrefContainer property.
     *
     * @param value
     *     allowed object is
     *     {@link psidev.psi.mi.jami.xml.model.extension.InteractorXrefContainer }
     *
     */
    public void setJAXBXref(InteractionXrefContainer value) {
        this.xrefContainer = value;
    }

    public boolean isIntraMolecular(){
        return intraMolecular != null ? intraMolecular : false;
    }

    /**
     * Sets the value of the intraMolecular property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIntraMolecular(boolean value) {
        this.intraMolecular = value;
    }

    /**
     * Gets the value of the id property.
     *
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     */
    public void setId(int value) {
        this.id = value;
        XmlEntryContext.getInstance().registerInteraction(this.id, this);
        if (getSourceLocator() != null){
            sourceLocator.setObjectId(this.id);
        }
    }

    @Override
    public Locator sourceLocation() {
        return (Locator)getSourceLocator();
    }

    public FileSourceLocator getSourceLocator() {
        return sourceLocator;
    }

    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            this.sourceLocator = null;
        }
        else if (sourceLocator instanceof PsiXmlLocator){
            this.sourceLocator = (PsiXmlLocator)sourceLocator;
            this.sourceLocator.setObjectId(getId());
        }
        else {
            this.sourceLocator = new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), getId());
        }
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return (getSourceLocator() != null ? "Interaction: "+getSourceLocator().toString():super.toString());
    }

    public void setJAXBAttributeWrapper(JAXBAttributeWrapper jaxbAttributeWrapper) {
        this.jaxbAttributeWrapper = jaxbAttributeWrapper;
    }

    public void setParticipantWrapper(JAXBParticipantWrapper<T> jaxbParticipantWrapper) {
        this.jaxbParticipantWrapper = jaxbParticipantWrapper;
        // initialise all participants because of back references
        if (this.jaxbParticipantWrapper != null && !this.jaxbParticipantWrapper.participants.isEmpty()){
            List<T> participantsToIterate = new ArrayList<T>(this.jaxbParticipantWrapper.participants);
            for (T participant : participantsToIterate){
                processAddedParticipant(participant);
                AbstractXmlParticipant xmlParticipant = (AbstractXmlParticipant)participant;
                // we have a participant pool, remove the previous participant and add the pool
                if (xmlParticipant.getParticipantPool() != null){
                    this.jaxbParticipantWrapper.participants.remove(participant);
                    this.jaxbParticipantWrapper.participants.add((T)xmlParticipant.getParticipantPool());
                }
            }
        }
        else{
            PsiXmlParserListener listener = XmlEntryContext.getInstance().getListener();
            if (listener != null){
                listener.onInteractionWithoutParticipants(this, this);
            }
        }
    }

    public InteractionXrefContainer getJAXBXref() {
        return xrefContainer;
    }

    protected void processAddedParticipant(T participant) {
        participant.setInteraction(this);
    }

    protected void initialiseAnnotationWrapper(){
        this.jaxbAttributeWrapper = new JAXBAttributeWrapper();
    }

    protected void initialiseParticipantWrapper(){
        this.jaxbParticipantWrapper = new JAXBParticipantWrapper();
    }

    protected NamesContainer getNamesContainer() {
        if (this.namesContainer == null){
            initialiseNamesContainer();
        }
        return namesContainer;
    }

    protected JAXBAttributeWrapper getAttributeWrapper() {
        if (this.jaxbAttributeWrapper == null){
            initialiseAnnotationWrapper();
        }
        return jaxbAttributeWrapper;
    }

    protected void initialiseNamesContainer(){
        this.namesContainer = new NamesContainer();
    }

    ////////////////////////////////////////////////////////////////// classes

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="interactionAttributeWrapper")
    public static class JAXBAttributeWrapper implements Locatable, FileSourceContext{
        private PsiXmlLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<Annotation> annotations;
        private InteractionChecksumList checksums;
        private JAXBAttributeList jaxbAttributes;
        private Checksum rigid;

        public JAXBAttributeWrapper(){
            initialiseAnnotations();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmlLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else if (sourceLocator instanceof PsiXmlLocator){
                this.sourceLocator = (PsiXmlLocator)sourceLocator;
            }
            else {
                this.sourceLocator = new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        protected void initialiseAnnotations(){
            annotations = new ArrayList<Annotation>();
            checksums = new InteractionChecksumList();
        }

        protected void initialiseAnnotationsWith(List<Annotation> annots){
            annotations = annots;
            checksums = new InteractionChecksumList();
        }

        protected List<Annotation> getAnnotations() {
            return annotations;
        }

        private void processAddedChecksumEvent(Checksum added) {
            if (rigid == null && ChecksumUtils.doesChecksumHaveMethod(added, Checksum.RIGID_MI, Checksum.RIGID)){
                // the rigid is not set, we can set the rigid
                rigid = added;
            }
        }

        private void processRemovedChecksumEvent(Checksum removed) {
            if (rigid == removed){
                rigid = ChecksumUtils.collectFirstChecksumWithMethod(checksums, Checksum.RIGID_MI, Checksum.RIGID);
            }
        }

        private void clearPropertiesLinkedToChecksums() {
            rigid = null;
        }

        @XmlElement(type=XmlAnnotation.class, name="attribute", required = true)
        public List<Annotation> getJAXBAttributes() {
            if (jaxbAttributes == null){
               jaxbAttributes = new JAXBAttributeList();
            }
            return jaxbAttributes;
        }

        protected class JAXBAttributeList extends ArrayList<Annotation> {

            public JAXBAttributeList(){
                super();
            }

            @Override
            public boolean add(Annotation annotation) {
                if (annotation == null){
                    return false;
                }
                return processAnnotation(null, annotation);
            }

            @Override
            public boolean addAll(Collection<? extends Annotation> c) {
                if (c == null){
                    return false;
                }
                boolean added = false;

                for (Annotation a : c){
                    if (add(a)){
                        added = true;
                    }
                }
                return added;
            }

            @Override
            public void add(int index, Annotation element) {
                processAnnotation(index, element);
            }

            @Override
            public boolean addAll(int index, Collection<? extends Annotation> c) {
                int newIndex = index;
                if (c == null){
                    return false;
                }
                boolean add = false;
                for (Annotation a : c){
                    if (processAnnotation(newIndex, a)){
                        newIndex++;
                        add = true;
                    }
                }
                return add;
            }

            protected boolean processAnnotation(Integer index, Annotation annotation) {
                if (AnnotationUtils.doesAnnotationHaveTopic(annotation, Checksum.CHECKSUM_MI, Checksum.CHECKUM)
                        || AnnotationUtils.doesAnnotationHaveTopic(annotation, Checksum.RIGID_MI, Checksum.RIGID)){
                    XmlChecksum checksum = new XmlChecksum(annotation.getTopic(), annotation.getValue() != null ? annotation.getValue() : PsiXmlUtils.UNSPECIFIED);
                    checksum.setSourceLocator(((FileSourceContext)annotation).getSourceLocator());
                    checksums.add(checksum);
                    return false;
                }
                else {
                    return addAnnotation(index, annotation);
                }
            }

            private boolean addAnnotation(Integer index, Annotation annotation) {
                if (index == null){
                    return annotations.add(annotation);
                }
                annotations.add(index, annotation);
                return true;
            }
        }

        private class InteractionChecksumList extends AbstractListHavingProperties<Checksum> {
            public InteractionChecksumList(){
                super();
            }

            @Override
            protected void processAddedObjectEvent(Checksum added) {
                processAddedChecksumEvent(added);
            }

            @Override
            protected void processRemovedObjectEvent(Checksum removed) {
                processRemovedChecksumEvent(removed);
            }

            @Override
            protected void clearProperties() {
                clearPropertiesLinkedToChecksums();
            }
        }

        @Override
        public String toString() {
            return "Interaction Attribute List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="interactionParticipantWrapper")
    public static class JAXBParticipantWrapper<T extends Participant> implements Locatable, FileSourceContext{
        private PsiXmlLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;
        private List<T> participants;

        public JAXBParticipantWrapper(){
            initialiseParticipants();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmlLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else if (sourceLocator instanceof PsiXmlLocator){
                this.sourceLocator = (PsiXmlLocator)sourceLocator;
            }
            else {
                this.sourceLocator = new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        protected void initialiseParticipants(){
            participants = new ArrayList<T>();
        }

        public List<T> getJAXBParticipants() {
            return participants;
        }

        @Override
        public String toString() {
            return "Participant List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }
}
