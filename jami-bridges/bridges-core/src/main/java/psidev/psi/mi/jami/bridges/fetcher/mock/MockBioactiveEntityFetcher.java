package psidev.psi.mi.jami.bridges.fetcher.mock;

import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.fetcher.BioactiveEntityFetcher;
import psidev.psi.mi.jami.model.BioactiveEntity;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 09/08/13
 */
public class MockBioactiveEntityFetcher
        extends AbstractMockFetcher<BioactiveEntity>
        implements BioactiveEntityFetcher{

    public BioactiveEntity fetchBioactiveEntityByIdentifier(String identifier) throws BridgeFailedException {
        return super.getEntry(identifier);
    }

    public Collection<BioactiveEntity> fetchBioactiveEntityByIdentifiers(Collection<String> identifier) throws BridgeFailedException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}