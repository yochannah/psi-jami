package psidev.psi.mi.jami.model.impl;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Molecule;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Xref;

/**
 * The default implementation for a molecule
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/05/13</pre>
 */

public class DefaultMolecule extends DefaultInteractor implements Molecule {
    public DefaultMolecule(String name, CvTerm type) {
        super(name, type);
    }

    public DefaultMolecule(String name, String fullName, CvTerm type) {
        super(name, fullName, type);
    }

    public DefaultMolecule(String name, CvTerm type, Organism organism) {
        super(name, type, organism);
    }

    public DefaultMolecule(String name, String fullName, CvTerm type, Organism organism) {
        super(name, fullName, type, organism);
    }

    public DefaultMolecule(String name, CvTerm type, Xref uniqueId) {
        super(name, type, uniqueId);
    }

    public DefaultMolecule(String name, String fullName, CvTerm type, Xref uniqueId) {
        super(name, fullName, type, uniqueId);
    }

    public DefaultMolecule(String name, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, type, organism, uniqueId);
    }

    public DefaultMolecule(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, fullName, type, organism, uniqueId);
    }
}
