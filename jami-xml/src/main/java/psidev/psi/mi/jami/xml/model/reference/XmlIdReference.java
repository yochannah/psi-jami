package psidev.psi.mi.jami.xml.model.reference;

import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.xml.cache.PsiXmlIdCache;

/**
 * A XML reference to an parent having an id
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/10/13</pre>
 */

public interface XmlIdReference extends FileSourceContext{

    /**
     * Register itself as an id to resolve later
     */
    public void registerForResolution();

    /**
     * Resolve the references given a map of parsed objects
     * @param parsedObjects
     * @return true if the reference was solved. false otherwise
     */
    public boolean resolve(PsiXmlIdCache parsedObjects);
}
