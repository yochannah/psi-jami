package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.utils.comparator.participant.DefaultModelledParticipantComparator;

/**
 * Default ModelledInteraction comparator.
 *
 * It will use a DefaultInteractionBaseComparator to compare basic interaction properties.
 * Then it will compare the modelledParticipants using DefaultModelledParticipantComparator.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/01/13</pre>
 */

public class DefaultModelledInteractionComparator extends ModelledInteractionComparator {

    private static DefaultModelledInteractionComparator defaultModelledInteractionComparator;

    /**
     * Creates a new DefaultModelledInteractionComparator. It will use a DefaultInteractionBaseComparator to
     * compare basic interaction properties
     */
    public DefaultModelledInteractionComparator() {
        super(new DefaultModelledParticipantComparator(), new DefaultInteractionBaseComparator());
    }

    @Override
    /**
     * It will use a DefaultInteractionBaseComparator to compare basic interaction properties.
     * Then it will compare the modelledParticipants using DefaultModelledParticipantComparator.
     * */
    public int compare(ModelledInteraction interaction1, ModelledInteraction interaction2) {
        return super.compare(interaction1, interaction2);
    }

    /**
     * Use DefaultModelledInteractionComparator to know if two modelled interactions are equals.
     * @param interaction1
     * @param interaction2
     * @return true if the two modelled interactions are equal
     */
    public static boolean areEquals(ModelledInteraction interaction1, ModelledInteraction interaction2){
        if (defaultModelledInteractionComparator == null){
            defaultModelledInteractionComparator = new DefaultModelledInteractionComparator();
        }

        return defaultModelledInteractionComparator.compare(interaction1, interaction2) == 0;
    }
}
