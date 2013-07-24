package psidev.psi.mi.jami.enricher.impl.participant;

import psidev.psi.mi.jami.enricher.*;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.participant.listener.ParticipantEnricherListener;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.model.*;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 19/06/13
 */
public abstract class AbstractParticipantEnricher<P extends Participant , F extends Feature>
        implements ParticipantEnricher<P , F>  {


    protected ParticipantEnricherListener listener;

    protected ProteinEnricher proteinEnricher;
    protected CvTermEnricher cvTermEnricher;
    protected FeatureEnricher<F> featureEnricher;


    public void enrichParticipants(Collection<P> participantsToEnrich) throws EnricherException {

        for(Participant participant : participantsToEnrich){
            enrichParticipant((P) participant);
        }
    }


    public void enrichParticipant(P participantToEnrich) throws EnricherException {

        if(participantToEnrich == null) throw new IllegalArgumentException("Attempted to enrich a null participant.");

        this.featureEnricher.setFeaturesToEnrich(participantToEnrich);

        processParticipant(participantToEnrich);

        // Enrich Interactor
        CvTerm interactorType = participantToEnrich.getInteractor().getInteractorType();
        if(interactorType.getMIIdentifier().equalsIgnoreCase(Interactor.UNKNOWN_INTERACTOR_MI)
                || interactorType.getShortName().equalsIgnoreCase(Interactor.UNKNOWN_INTERACTOR)
                || interactorType.getMIIdentifier().equalsIgnoreCase(Protein.PROTEIN_MI)
                || interactorType.getShortName().equalsIgnoreCase(Protein.PROTEIN)){

            if(getProteinEnricher() != null){
                //Todo is there a more elegant solution to this?
                if(participantToEnrich.getInteractor() instanceof Protein){
                    getProteinEnricher().enrichProtein( (Protein) participantToEnrich.getInteractor() );
                } else {
                    if(listener != null) listener.onParticipantEnriched(participantToEnrich ,
                            EnrichmentStatus.FAILED,
                            "Found interactor of type "+interactorType.getShortName()+
                                    " ("+interactorType.getMIIdentifier()+") "+
                                    "but was not an instance of 'Protein', " +
                                    "was "+participantToEnrich.getInteractor().getClass()+" instead.");
                    return;
                }
            }
        }

        if( getFeatureEnricher() != null )
                getFeatureEnricher().enrichFeatures(participantToEnrich.getFeatures());

        if( listener != null )
            listener.onParticipantEnriched(participantToEnrich , EnrichmentStatus.SUCCESS , null);

    }

    protected void processParticipant(P participantToEnrich) throws EnricherException {
        // Enrich BioRole
        if(getCvTermEnricher() != null)
            getCvTermEnricher().enrichCvTerm(participantToEnrich.getBiologicalRole());
    }

    public void setParticipantListener(ParticipantEnricherListener participantEnricherListener) {
        this.listener = participantEnricherListener;
    }

    public ParticipantEnricherListener getParticipantEnricherListener() {
        return listener;
    }

    public void setProteinEnricher(ProteinEnricher proteinEnricher) {
        this.proteinEnricher = proteinEnricher;
        EnricherUtil.linkFeatureEnricherToProteinEnricher(getFeatureEnricher(), proteinEnricher);
    }

    public ProteinEnricher getProteinEnricher(){
        return proteinEnricher;
    }

    public void setCvTermEnricher(CvTermEnricher cvTermEnricher){
        this.cvTermEnricher = cvTermEnricher;
    }

    public CvTermEnricher getCvTermEnricher(){
        return cvTermEnricher;
    }

    public void setFeatureEnricher(FeatureEnricher<F> featureEnricher){
        this.featureEnricher = featureEnricher;
        EnricherUtil.linkFeatureEnricherToProteinEnricher(featureEnricher, getProteinEnricher());
    }

    public FeatureEnricher<F> getFeatureEnricher(){
        return featureEnricher;
    }
}
