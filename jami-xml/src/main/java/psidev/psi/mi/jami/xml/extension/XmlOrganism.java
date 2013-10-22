package psidev.psi.mi.jami.xml.extension;

import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.utils.comparator.organism.UnambiguousOrganismComparator;

import javax.xml.bind.annotation.*;
import java.util.Collection;

/**
 * Xml implementation of an organism
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>22/07/13</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "bioSource", propOrder = {
        "JAXBNames",
        "JAXBCellType",
        "JAXBCompartment",
        "JAXBTissue"
})
@XmlSeeAlso({
        HostOrganism.class
})
public class XmlOrganism implements Organism, FileSourceContext{

    private NamesContainer namesContainer;
    private int taxId;
    private CvTerm cellType;
    private CvTerm compartment;
    private CvTerm tissue;

    private PsiXmLocator sourceLocator;

    public XmlOrganism(){

    }

    public XmlOrganism(int taxId){
        if (taxId == -1 || taxId == -2 || taxId == -3 || taxId == -4 || taxId > 0){
            this.taxId = taxId;
        }
        else {
            throw new IllegalArgumentException("The taxId "+taxId+" is not a valid taxid. Only NCBI taxid or -1, -2, -3, -4 are valid taxids.");
        }
    }

    public XmlOrganism(int taxId, String commonName){
        this(taxId);
        this.namesContainer = new NamesContainer();
        this.namesContainer.setJAXBShortLabel(commonName);
    }

    public XmlOrganism(int taxId, String commonName, String scientificName){
        this(taxId, commonName);
        this.namesContainer.setJAXBFullName(scientificName);
    }

    public XmlOrganism(int taxId, CvTerm cellType, CvTerm tissue, CvTerm compartment){
        this(taxId);
        this.cellType = cellType;
        this.tissue = tissue;
        this.compartment = compartment;
    }

    public XmlOrganism(int taxId, String commonName, CvTerm cellType, CvTerm tissue, CvTerm compartment){
        this(taxId, commonName);
        this.cellType = cellType;
        this.tissue = tissue;
        this.compartment = compartment;
    }

    public XmlOrganism(int taxId, String commonName, String scientificName, CvTerm cellType, CvTerm tissue, CvTerm compartment){
        this(taxId, commonName, scientificName);
        this.cellType = cellType;
        this.tissue = tissue;
        this.compartment = compartment;
    }

    public CvTerm getCellType() {
        return this.cellType;
    }

    public void setCellType(CvTerm cellType) {
        this.cellType = cellType;
    }

    public CvTerm getCompartment() {
        return this.compartment;
    }

    public void setCompartment(CvTerm compartment) {
        this.compartment = compartment;
    }

    public CvTerm getTissue() {
        return this.tissue;
    }

    public void setTissue(CvTerm tissue) {
        this.tissue = tissue;
    }

    public int getTaxId() {
        return this.taxId;
    }

    public void setTaxId(int id) {
        if (id == -1 || id == -2 || id == -3 || id == -4 || id > 0){
            this.taxId = id;
        }
        else {
            throw new IllegalArgumentException("The taxId "+id+" is not a valid taxid. Only NCBI taxid or -1, -2, -3, -4 are valid taxids.");
        }
    }

    public String getCommonName() {
        return this.namesContainer != null ? this.namesContainer.getJAXBShortLabel() : null;
    }

    public void setCommonName(String name) {
        if (this.namesContainer == null){
            this.namesContainer = new NamesContainer();
        }
        this.namesContainer.setJAXBShortLabel(name);
    }

    public String getScientificName() {
        return this.namesContainer != null ? this.namesContainer.getJAXBFullName() : null;
    }

    public void setScientificName(String name) {
        if (this.namesContainer == null){
            this.namesContainer = new NamesContainer();
        }
        this.namesContainer.setJAXBFullName(name);
    }

    public Collection<Alias> getAliases() {
        if (this.namesContainer == null){
            this.namesContainer = new NamesContainer();
        }
        return this.namesContainer.getJAXBAliases();
    }

    /**
     * Gets the value of the names property.
     *
     * @return
     *     possible object is
     *     {@link NamesContainer }
     *
     */
    @XmlElement(name = "names")
    public NamesContainer getJAXBNames() {
        if (namesContainer != null && namesContainer.isEmpty()){
            return null;
        }
        return namesContainer;
    }

    /**
     * Sets the value of the names property.
     *
     * @param value
     *     allowed object is
     *     {@link NamesContainer }
     *
     */
    public void setJAXBNames(NamesContainer value) {
        this.namesContainer = value;
    }

    @XmlElement(name = "cellType", type = XmlOpenCvTerm.class)
    public CvTerm getJAXBCellType() {
        return this.cellType;
    }

    public void setJAXBCellType(XmlOpenCvTerm cellType) {
        this.cellType = cellType;
    }

    @XmlElement(name = "compartment", type = XmlOpenCvTerm.class)
    public CvTerm getJAXBCompartment() {
        return this.compartment;
    }

    public void setJAXBCompartment(XmlOpenCvTerm compartment) {
        this.compartment = compartment;
    }

    @XmlElement(name = "tissue", type = XmlOpenCvTerm.class)
    public CvTerm getJAXBTissue() {
        return this.tissue;
    }

    public void setJAXBTissue(XmlCvTerm tissue) {
        this.tissue = tissue;
    }

    @XmlAttribute(name = "ncbiTaxId", required = true)
    public int getJAXBTaxId() {
        return this.taxId;
    }

    public void setJAXBTaxId(int id) {
        this.taxId = id;
    }

    @XmlLocation
    @XmlTransient
    public Locator getSaxLocator() {
        return sourceLocator;
    }

    public void setSaxLocator(Locator sourceLocator) {
        if (sourceLocator == null){
            this.sourceLocator = null;
        }
        else{
            this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getColumnNumber(), null);
        }    }

    public FileSourceLocator getSourceLocator() {
        return sourceLocator;
    }

    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            this.sourceLocator = null;
        }
        else{
            this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
        }    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (!(o instanceof Organism)){
            return false;
        }

        return UnambiguousOrganismComparator.areEquals(this, (Organism) o);
    }

    @Override
    public int hashCode() {
        return UnambiguousOrganismComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return taxId + "(" + (getCommonName() != null ? getCommonName() : "-" )+")";
    }
}
