package psidev.psi.mi.jami.enricher.enricherimplementation.protein;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.enricher.event.EnricherEvent;
import psidev.psi.mi.jami.enricher.exception.EnrichmentException;
import psidev.psi.mi.jami.enricher.listener.EnricherListener;
import psidev.psi.mi.jami.enricher.mockfetcher.protein.MockProteinFetcher;
import psidev.psi.mi.jami.model.Protein;
import psidev.psi.mi.jami.model.impl.DefaultProtein;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Gabriel Aldam (galdam@ebi.ac.uk)
 * Date: 23/05/13
 * Time: 14:39
 */
public class MaximumProteinUpdaterTest {
    private MaximumProteinUpdater maximumProteinUpdater;
    private MockProteinFetcher fetcher;
    private EnricherEvent event;

    private static final String TEST_SHORTNAME = "test shortName";
    private static final String TEST_FULLNAME = "test fullName";
    private static final String TEST_AC_FULL_PROT = "P12345";
    private static final String TEST_AC_HALF_PROT = "P11111";
    private static final String TEST_SEQUENCE = "GATTACA";


    @Before
    public void initialiseFetcherAndEnricher(){
        this.fetcher = new MockProteinFetcher();
        this.maximumProteinUpdater = new MaximumProteinUpdater(fetcher);

        Protein fullProtein = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME );
        fullProtein.setUniprotkb(TEST_AC_FULL_PROT);
        fullProtein.setSequence(TEST_SEQUENCE);
        fetcher.addNewProtein(TEST_AC_FULL_PROT, fullProtein);

        Protein halfProtein = new DefaultProtein(TEST_SHORTNAME);
        halfProtein.setUniprotkb(TEST_AC_HALF_PROT);
        fetcher.addNewProtein(TEST_AC_HALF_PROT, halfProtein);
        event = null;
    }

    @Test
    public void test_full_overwrite() throws EnrichmentException {
        Protein protein_with_all_fields = new DefaultProtein("test2 shortName", "test2 fullName");
        protein_with_all_fields.setUniprotkb(TEST_AC_FULL_PROT);
        protein_with_all_fields.setSequence("TAGTAG");

        assertNotNull(protein_with_all_fields.getShortName());
        assertNotNull(protein_with_all_fields.getFullName());
        assertNotNull(protein_with_all_fields.getUniprotkb());
        assertNotNull(protein_with_all_fields.getSequence());

        this.maximumProteinUpdater.enrichProtein(protein_with_all_fields);

        assertEquals( protein_with_all_fields.getShortName(), TEST_SHORTNAME);
        assertEquals( protein_with_all_fields.getFullName(), TEST_FULLNAME);
        assertEquals( protein_with_all_fields.getSequence(), TEST_SEQUENCE);
    }


    /**
     * Enrich an already complete protein with one which is only half complete.
     * This should not have any additions, nor throw any exceptions.
     * @throws EnrichmentException
     */
    @Test
    public void test_overwrite_does_not_change_fields_to_null_from_proteinEnriched() throws EnrichmentException{


        Protein protein_with_all_fields = new DefaultProtein("test2 shortName", "test2 fullName");
        protein_with_all_fields.setUniprotkb(TEST_AC_HALF_PROT);
        protein_with_all_fields.setSequence("TAGTAG");

        this.maximumProteinUpdater.enrichProtein(protein_with_all_fields);

        assertNotNull(protein_with_all_fields.getFullName());
        assertNotNull(protein_with_all_fields.getShortName());
        assertNotNull(protein_with_all_fields.getUniprotkb());
        assertNotNull(protein_with_all_fields.getSequence());
    }


    /**
     * Enrich an already completed protein with a less complete protein
     * Count additions, should be 0
     * Count mismatches, should be 1 (shortname)
     * Check the id, should be the same as the enriched protein.
     * Run enrichment on another protein of different id
     * Check the id, should be the same as new protein
     * @throws EnrichmentException
     */
    @Test
    public void test_enricher_event_is_cleared() throws EnrichmentException {
        Protein protein_test_one = new DefaultProtein("testpart1 shortName", "testpart1 fullName");
        protein_test_one.setUniprotkb(TEST_AC_HALF_PROT);
        protein_test_one.setSequence("TAGTAG");

        Protein protein_test_two = new DefaultProtein(TEST_SHORTNAME, TEST_FULLNAME);
        protein_test_two.setUniprotkb(TEST_AC_FULL_PROT);
        protein_test_two.setSequence(TEST_SEQUENCE);

        this.maximumProteinUpdater.addEnricherListener(new EnricherListener() {
            public void onEnricherEvent(EnricherEvent e) {
                event = e;
            }
        });

        //If this is failing, you may wish to use a logging listener to read the log.
        //this.maximumProteinUpdater.addEnricherListener(new LoggingEnricherListener());

        maximumProteinUpdater.enrichProtein(protein_test_one);

        assertEquals(event.getQueryID(), TEST_AC_HALF_PROT);
        assertTrue(event.getAdditions().size() == 0);
        assertTrue(event.getMismatches().size() == 0);
        assertTrue(event.getOverwrites().size() == 1);

        maximumProteinUpdater.enrichProtein(protein_test_two);

        assertEquals(event.getQueryID(), TEST_AC_FULL_PROT);
        assertTrue(event.getAdditions().size() == 0);
        assertTrue(event.getMismatches().size() == 0);
        assertTrue(event.getOverwrites().size() == 0);
    }



    @Test
    public void test_enricher_event_is_fired_and_has_correct_content() throws EnrichmentException {
        Protein protein_to_enrich = new DefaultProtein("test2 shortName", "test2 fullName");
        protein_to_enrich.setUniprotkb(TEST_AC_FULL_PROT);

        event = null;
        this.maximumProteinUpdater.addEnricherListener(new EnricherListener() {
            public void onEnricherEvent(EnricherEvent e) {
                event = e;
            }
        });

        maximumProteinUpdater.enrichProtein(protein_to_enrich);

        assertNotNull(event);
        assertTrue(event.getObjectType().equals("Protein"));
        assertTrue(event.getQueryID().equals(TEST_AC_FULL_PROT));
        assertTrue(event.getQueryIDType().equals("UniprotKB AC"));
        assertTrue(event.getAdditions().size() > 0);
        assertTrue(event.getMismatches().size() == 0);
        assertTrue(event.getOverwrites().size() > 0);
    }
}
