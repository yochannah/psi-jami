package psidev.psi.mi.jami.model;

import java.util.Collection;

/**
 * Molecule or complex of molecules that interacts with other molecules/complexes
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>22/11/12</pre>
 */

public interface Interactor {

    public static final String UNKNOWN_INTERACTOR = "unknown participant";
    public static final String UNKNOWN_INTERACTOR_MI = "MI:0329";

    /**
     * The short name of the interactor.
     * It cannot be null or empty.
     * Ex: brca2
     * @return the short name
     */
    public String getShortName();

    /**
     * Sets the short name of an interactor
     * @param name : short name
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setShortName(String name);

    /**
     * The full name of the interactor.
     * It can be null
     * Ex: Breast cancer type 2 susceptibility protein
     * @return the full name
     */
    public String getFullName();

    /**
     * Sets the full name of the interactor
     * @param name : full name
     */
    public void setFullName(String name);

    /**
     * Set of identifiers for this interactor. The identifiers can be from different databases, can be primary identifiers and secondary identifiers but they must be unambiguous.
     * The Collection cannot be null, when an interactor does not have any identifiers, the method should return an empty Collection.
     * Ex: uniprotkb secondary accession O00183, primary accessions, ...
     * @return the alternative identifier
     */
    public <X extends Xref> Collection<X> getIdentifiers();

    /**
     * The identifier in the list of identifiers which is the preferred identifier.
     * If no identifiers is preferred, it should return the first identifier of the list of identifiers, null if the list of identifiers
     * is empty.
     * @return the preferred identifier in the list of identifiers, null if the list of identifiers is empty
     */
    public Xref getPreferredIdentifier();

    /**
     * Set of checksums computed for this interactor.
     * The Collection cannot be null so when an interactor does not have a checksum, the method should return an empty Collection
     * Ex: rogid:u1FCes02jPb3CGRj1aDkzpbSiuI9606, standard Inchi key, ...
     * @return the set of checksums
     */
    public <C extends Checksum> Collection<C> getChecksums();

    /**
     * Collection of other xrefs that give more information about the interactor.
     * Ex: GO references to gives function/process/location information
     * @return other xrefs
     */
    public <X extends Xref> Collection<X> getXrefs();

    /**
     * Collection of annotations for an interactor.
     * The set cannot be null and if the interactor does not have any annotations, the method should return an empty Collection.
     * Ex: pharmacology, isoform-comment, etc.
     * @return the annotations
     */
    public <A extends Annotation> Collection<A> getAnnotations();

    /**
     * Collection of aliases for an interactor
     * The Collection cannot be null and if the interactor does not have any aliases, the method should return an empty Collection.
     * Ex: complex-synonym, author-assigned name, ...
     * @return the aliases
     */
    public <A extends Alias> Collection<A> getAliases();

    /**
     * The original source organism for this interactor.
     * It can be null in case of chemical compounds/synthetic peptides
     * @return the organism
     */
    public Organism getOrganism();

    /**
     * Sets the source organism of this interactor
     * @param organism : source organism
     */
    public void setOrganism(Organism organism);

    /**
     * The molecule type of this interactor.
     * It is a controlled vocabulary term and cannot be null.
     * Ex: protein, gene, small molecule, ...
     * @return interactor type
     */
    public CvTerm getInteractorType();

    /**
     * Sets the molecule type for this interactor
     * If the given type is null, this method automatically sets the interactor type to 'unknown participant' (MI:0329)
     * @param type : molecule type
     */
    public void setInteractorType(CvTerm type);
}
