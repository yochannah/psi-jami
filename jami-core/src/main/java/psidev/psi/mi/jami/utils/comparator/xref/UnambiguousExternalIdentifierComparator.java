package psidev.psi.mi.jami.utils.comparator.xref;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;

import java.util.Comparator;

/**
 * Unambiguous comparator for external identifiers (Xref database and id).
 * It compares first the databases and then the ids (case sensitive) but ignores the version.
 * To compare the databases, it looks first at the PSI-MI id (the database with PSI-MI id will always come first), otherwise it looks at the database shortlabel only.
 * If one database PSI-MI id is null, it will always comes after an Xref having a non null database PSI-MI id.
 * - Two external identifiers which are null are equals
 * - The external identifier which is not null is before null.
 * - If the two external identifiers are set :
 *     - compare the databases. If both databases are equal, compare the ids (is case sensitive)
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/12/12</pre>
 */

public class UnambiguousExternalIdentifierComparator implements Comparator<Xref> {

    private static UnambiguousExternalIdentifierComparator unambiguousIdentifierComparator;

    public UnambiguousExternalIdentifierComparator() {
    }

    /**
     * It compares first the databases and then the ids (case sensitive) but ignores the version.
     * To compare the databases, it looks first at the PSI-MI id (the database with PSI-MI id will always come first), otherwise it looks at the database shortlabel only.
     * If one database PSI-MI id is null, it will always comes after an Xref having a non null database PSI-MI id.
     * - Two external identifiers which are null are equals
     * - The external identifier which is not null is before null.
     * - If the two external identifiers are set :
     *     - compare the databases. If both databases are equal, compare the ids (is case sensitive)
     * @param externalIdentifier1
     * @param externalIdentifier2
     * */
    public int compare(Xref externalIdentifier1, Xref externalIdentifier2) {
        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (externalIdentifier1 == externalIdentifier2){
            return EQUAL;
        }
        else if (externalIdentifier1 == null){
            return AFTER;
        }
        else if (externalIdentifier2 == null){
            return BEFORE;
        }
        else {
            // compares databases first (cannot use CvTermComparator because have to break the loop)
            CvTerm database1 = externalIdentifier1.getDatabase();
            CvTerm database2 = externalIdentifier2.getDatabase();
            String mi1 = database1.getMIIdentifier();
            String mi2 = database2.getMIIdentifier();

            // if external id of database is set, look at database id only otherwise look at shortname
            int comp;
            if (mi1 != null && mi2 != null){
                comp = mi1.compareTo(mi2);
            }
            else if (mi1 == null && mi2 != null){
                return AFTER;
            }
            else if (mi2 == null && mi1 != null){
                return BEFORE;
            }
            else {
                String mod1 = database1.getMODIdentifier();
                String mod2 = database2.getMODIdentifier();
                if (mod1 != null && mod2 != null){
                    comp = mi1.compareTo(mi2);
                }
                else if (mod1 == null && mod2 != null){
                    return AFTER;
                }
                else if (mod2 == null && mod1 != null){
                    return BEFORE;
                }
                else {
                    String par1 = database1.getPARIdentifier();
                    String par2 = database2.getPARIdentifier();

                    if (par1 != null && par2 != null){
                        comp = mi1.compareTo(mi2);
                    }
                    else if (par1 == null && par2 != null){
                        return AFTER;
                    }
                    else if (par1 == null && par2 != null){
                        return BEFORE;
                    }
                    else {
                        comp = database1.getShortName().toLowerCase().trim().compareTo(database2.getShortName().toLowerCase().trim());
                    }
                }
            }

            if (comp != 0){
                return comp;
            }
            // check identifiers which cannot be null
            String id1 = externalIdentifier1.getId();
            String id2 = externalIdentifier2.getId();

            return id1.compareTo(id2);
        }
    }

    /**
     * Use UnambiguousIdentifierComparator to know if two external identifiers are equals.
     * @param externalIdentifier1
     * @param externalIdentifier2
     * @return true if the two external identifiers are equal
     */
    public static boolean areEquals(Xref externalIdentifier1, Xref externalIdentifier2){
        if (unambiguousIdentifierComparator == null){
            unambiguousIdentifierComparator = new UnambiguousExternalIdentifierComparator();
        }

        return unambiguousIdentifierComparator.compare(externalIdentifier1, externalIdentifier2) == 0;
    }

    /**
     *
     * @param externalIdentifier1
     * @return the hashcode consistent with the equals method for this comparator
     */
    public static int hashCode(Xref externalIdentifier1){
        if (unambiguousIdentifierComparator == null){
            unambiguousIdentifierComparator = new UnambiguousExternalIdentifierComparator();
        }
        if (externalIdentifier1 == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm database1 = externalIdentifier1.getDatabase();
        String mi = database1.getMIIdentifier();
        String mod = database1.getMODIdentifier();
        String par = database1.getPARIdentifier();

        if (mi != null){
            hashcode = 31*hashcode + mi.hashCode();
        }
        else if (mod != null){
            hashcode = 31*hashcode + mod.hashCode();
        }
        else if (par != null){
            hashcode = 31*hashcode + par.hashCode();
        }
        else {
            hashcode = 31*hashcode + database1.getShortName().toLowerCase().trim().hashCode();
        }

        hashcode = 31 * hashcode + externalIdentifier1.getId().hashCode();

        return hashcode;
    }
}
