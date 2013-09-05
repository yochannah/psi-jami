package psidev.psi.mi.jami.bridges.fetcher;

import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.model.Publication;

import java.util.Collection;


/**
 * The interface for fetching a publication record.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 29/07/13
 */
public interface PublicationFetcher {

    /**
     * Uses the  identifier to search for a publication and return a completed record.
     * @param identifier    The identifier of the publication to search for.
     * @param source        The database that the identifier is from.
     * @return              A completed record for the publication or null if no publication could be found.
     * @throws BridgeFailedException
     */
    public Publication getPublicationByIdentifier(String identifier , PublicationIdentifierSource source)
            throws BridgeFailedException;

    /**
     * Uses the identifiers to search for publications and return completed records.
     * @param identifiers   The identifiers of the publications to search for.
     * @param source        The database that the identifier is from.
     * @return              Completed records for the publications.
     * @throws BridgeFailedException
     */
    public Collection<Publication> getPublicationsByIdentifiers(Collection<String> identifiers , PublicationIdentifierSource source)
            throws BridgeFailedException;

}