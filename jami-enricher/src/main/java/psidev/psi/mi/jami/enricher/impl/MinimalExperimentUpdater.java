package psidev.psi.mi.jami.enricher.impl;

import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Publication;
import psidev.psi.mi.jami.utils.comparator.organism.DefaultOrganismComparator;

/**
 * Minimal updater for experiments
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 13/08/13
 */
public class MinimalExperimentUpdater extends MinimalExperimentEnricher{

    public MinimalExperimentUpdater(){
       super();
    }

    @Override
    protected void processInteractionDetectionMethod(Experiment experimentToEnrich, Experiment objectSource) throws EnricherException {

        if (experimentToEnrich.getInteractionDetectionMethod() != objectSource.getInteractionDetectionMethod()){
            CvTerm old = experimentToEnrich.getInteractionDetectionMethod();
            experimentToEnrich.setInteractionDetectionMethod(objectSource.getInteractionDetectionMethod());
            if (getExperimentEnricherListener() != null){
                getExperimentEnricherListener().onInteractionDetectionMethodUpdate(experimentToEnrich, old);
            }
        }
        processInteractionDetectionMethod(experimentToEnrich);
    }

    @Override
    protected void processPublication(Experiment experimentToEnrich, Experiment objectSource) throws EnricherException {
        if (experimentToEnrich.getPublication() != objectSource.getPublication()){
            Publication old = experimentToEnrich.getPublication();
            experimentToEnrich.setPublication(objectSource.getPublication());
            if (getExperimentEnricherListener() != null){
                getExperimentEnricherListener().onPublicationUpdate(experimentToEnrich, old);
            }
        }
        processPublication(experimentToEnrich);
    }

    @Override
    protected void processOrganism(Experiment experimentToEnrich, Experiment objectSource) throws EnricherException {
        if (!DefaultOrganismComparator.areEquals(experimentToEnrich.getHostOrganism(), objectSource.getHostOrganism())){
            Organism old = experimentToEnrich.getHostOrganism();
            experimentToEnrich.setHostOrganism(objectSource.getHostOrganism());
            if (getExperimentEnricherListener() != null){
                getExperimentEnricherListener().onHostOrganismUpdate(experimentToEnrich, old);
            }
        }
        processOrganism(experimentToEnrich);
    }
}
