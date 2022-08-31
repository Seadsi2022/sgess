package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Ecampagne;
import dsi.sea.sgess.repository.EcampagneRepository;
import dsi.sea.sgess.repository.search.EcampagneSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EcampagneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EcampagneResourceIT {

    private static final String DEFAULT_OBJETCAMPAGNE = "AAAAAAAAAA";
    private static final String UPDATED_OBJETCAMPAGNE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DEBUTCAMPAGNE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEBUTCAMPAGNE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINCAMPAGNE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINCAMPAGNE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DEBUTREELCAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEBUTREELCAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINREELCAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINREELCAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ISOPEN = false;
    private static final Boolean UPDATED_ISOPEN = true;

    private static final String ENTITY_API_URL = "/api/ecampagnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ecampagnes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EcampagneRepository ecampagneRepository;

    @Autowired
    private EcampagneSearchRepository ecampagneSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEcampagneMockMvc;

    private Ecampagne ecampagne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ecampagne createEntity(EntityManager em) {
        Ecampagne ecampagne = new Ecampagne()
            .objetcampagne(DEFAULT_OBJETCAMPAGNE)
            .description(DEFAULT_DESCRIPTION)
            .debutcampagne(DEFAULT_DEBUTCAMPAGNE)
            .fincampagne(DEFAULT_FINCAMPAGNE)
            .debutreelcamp(DEFAULT_DEBUTREELCAMP)
            .finreelcamp(DEFAULT_FINREELCAMP)
            .isopen(DEFAULT_ISOPEN);
        return ecampagne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ecampagne createUpdatedEntity(EntityManager em) {
        Ecampagne ecampagne = new Ecampagne()
            .objetcampagne(UPDATED_OBJETCAMPAGNE)
            .description(UPDATED_DESCRIPTION)
            .debutcampagne(UPDATED_DEBUTCAMPAGNE)
            .fincampagne(UPDATED_FINCAMPAGNE)
            .debutreelcamp(UPDATED_DEBUTREELCAMP)
            .finreelcamp(UPDATED_FINREELCAMP)
            .isopen(UPDATED_ISOPEN);
        return ecampagne;
    }

    @BeforeEach
    public void initTest() {
        ecampagneSearchRepository.deleteAll();
        ecampagne = createEntity(em);
    }

    @Test
    @Transactional
    void createEcampagne() throws Exception {
        int databaseSizeBeforeCreate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        // Create the Ecampagne
        restEcampagneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecampagne)))
            .andExpect(status().isCreated());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Ecampagne testEcampagne = ecampagneList.get(ecampagneList.size() - 1);
        assertThat(testEcampagne.getObjetcampagne()).isEqualTo(DEFAULT_OBJETCAMPAGNE);
        assertThat(testEcampagne.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEcampagne.getDebutcampagne()).isEqualTo(DEFAULT_DEBUTCAMPAGNE);
        assertThat(testEcampagne.getFincampagne()).isEqualTo(DEFAULT_FINCAMPAGNE);
        assertThat(testEcampagne.getDebutreelcamp()).isEqualTo(DEFAULT_DEBUTREELCAMP);
        assertThat(testEcampagne.getFinreelcamp()).isEqualTo(DEFAULT_FINREELCAMP);
        assertThat(testEcampagne.getIsopen()).isEqualTo(DEFAULT_ISOPEN);
    }

    @Test
    @Transactional
    void createEcampagneWithExistingId() throws Exception {
        // Create the Ecampagne with an existing ID
        ecampagne.setId(1L);

        int databaseSizeBeforeCreate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEcampagneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecampagne)))
            .andExpect(status().isBadRequest());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEcampagnes() throws Exception {
        // Initialize the database
        ecampagneRepository.saveAndFlush(ecampagne);

        // Get all the ecampagneList
        restEcampagneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecampagne.getId().intValue())))
            .andExpect(jsonPath("$.[*].objetcampagne").value(hasItem(DEFAULT_OBJETCAMPAGNE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].debutcampagne").value(hasItem(DEFAULT_DEBUTCAMPAGNE.toString())))
            .andExpect(jsonPath("$.[*].fincampagne").value(hasItem(DEFAULT_FINCAMPAGNE.toString())))
            .andExpect(jsonPath("$.[*].debutreelcamp").value(hasItem(DEFAULT_DEBUTREELCAMP.toString())))
            .andExpect(jsonPath("$.[*].finreelcamp").value(hasItem(DEFAULT_FINREELCAMP.toString())))
            .andExpect(jsonPath("$.[*].isopen").value(hasItem(DEFAULT_ISOPEN.booleanValue())));
    }

    @Test
    @Transactional
    void getEcampagne() throws Exception {
        // Initialize the database
        ecampagneRepository.saveAndFlush(ecampagne);

        // Get the ecampagne
        restEcampagneMockMvc
            .perform(get(ENTITY_API_URL_ID, ecampagne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ecampagne.getId().intValue()))
            .andExpect(jsonPath("$.objetcampagne").value(DEFAULT_OBJETCAMPAGNE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.debutcampagne").value(DEFAULT_DEBUTCAMPAGNE.toString()))
            .andExpect(jsonPath("$.fincampagne").value(DEFAULT_FINCAMPAGNE.toString()))
            .andExpect(jsonPath("$.debutreelcamp").value(DEFAULT_DEBUTREELCAMP.toString()))
            .andExpect(jsonPath("$.finreelcamp").value(DEFAULT_FINREELCAMP.toString()))
            .andExpect(jsonPath("$.isopen").value(DEFAULT_ISOPEN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEcampagne() throws Exception {
        // Get the ecampagne
        restEcampagneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEcampagne() throws Exception {
        // Initialize the database
        ecampagneRepository.saveAndFlush(ecampagne);

        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        ecampagneSearchRepository.save(ecampagne);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());

        // Update the ecampagne
        Ecampagne updatedEcampagne = ecampagneRepository.findById(ecampagne.getId()).get();
        // Disconnect from session so that the updates on updatedEcampagne are not directly saved in db
        em.detach(updatedEcampagne);
        updatedEcampagne
            .objetcampagne(UPDATED_OBJETCAMPAGNE)
            .description(UPDATED_DESCRIPTION)
            .debutcampagne(UPDATED_DEBUTCAMPAGNE)
            .fincampagne(UPDATED_FINCAMPAGNE)
            .debutreelcamp(UPDATED_DEBUTREELCAMP)
            .finreelcamp(UPDATED_FINREELCAMP)
            .isopen(UPDATED_ISOPEN);

        restEcampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEcampagne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEcampagne))
            )
            .andExpect(status().isOk());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        Ecampagne testEcampagne = ecampagneList.get(ecampagneList.size() - 1);
        assertThat(testEcampagne.getObjetcampagne()).isEqualTo(UPDATED_OBJETCAMPAGNE);
        assertThat(testEcampagne.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEcampagne.getDebutcampagne()).isEqualTo(UPDATED_DEBUTCAMPAGNE);
        assertThat(testEcampagne.getFincampagne()).isEqualTo(UPDATED_FINCAMPAGNE);
        assertThat(testEcampagne.getDebutreelcamp()).isEqualTo(UPDATED_DEBUTREELCAMP);
        assertThat(testEcampagne.getFinreelcamp()).isEqualTo(UPDATED_FINREELCAMP);
        assertThat(testEcampagne.getIsopen()).isEqualTo(UPDATED_ISOPEN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Ecampagne> ecampagneSearchList = IterableUtils.toList(ecampagneSearchRepository.findAll());
                Ecampagne testEcampagneSearch = ecampagneSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEcampagneSearch.getObjetcampagne()).isEqualTo(UPDATED_OBJETCAMPAGNE);
                assertThat(testEcampagneSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testEcampagneSearch.getDebutcampagne()).isEqualTo(UPDATED_DEBUTCAMPAGNE);
                assertThat(testEcampagneSearch.getFincampagne()).isEqualTo(UPDATED_FINCAMPAGNE);
                assertThat(testEcampagneSearch.getDebutreelcamp()).isEqualTo(UPDATED_DEBUTREELCAMP);
                assertThat(testEcampagneSearch.getFinreelcamp()).isEqualTo(UPDATED_FINREELCAMP);
                assertThat(testEcampagneSearch.getIsopen()).isEqualTo(UPDATED_ISOPEN);
            });
    }

    @Test
    @Transactional
    void putNonExistingEcampagne() throws Exception {
        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        ecampagne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ecampagne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ecampagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEcampagne() throws Exception {
        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        ecampagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ecampagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEcampagne() throws Exception {
        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        ecampagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcampagneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecampagne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEcampagneWithPatch() throws Exception {
        // Initialize the database
        ecampagneRepository.saveAndFlush(ecampagne);

        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();

        // Update the ecampagne using partial update
        Ecampagne partialUpdatedEcampagne = new Ecampagne();
        partialUpdatedEcampagne.setId(ecampagne.getId());

        partialUpdatedEcampagne.objetcampagne(UPDATED_OBJETCAMPAGNE).finreelcamp(UPDATED_FINREELCAMP);

        restEcampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEcampagne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEcampagne))
            )
            .andExpect(status().isOk());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        Ecampagne testEcampagne = ecampagneList.get(ecampagneList.size() - 1);
        assertThat(testEcampagne.getObjetcampagne()).isEqualTo(UPDATED_OBJETCAMPAGNE);
        assertThat(testEcampagne.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEcampagne.getDebutcampagne()).isEqualTo(DEFAULT_DEBUTCAMPAGNE);
        assertThat(testEcampagne.getFincampagne()).isEqualTo(DEFAULT_FINCAMPAGNE);
        assertThat(testEcampagne.getDebutreelcamp()).isEqualTo(DEFAULT_DEBUTREELCAMP);
        assertThat(testEcampagne.getFinreelcamp()).isEqualTo(UPDATED_FINREELCAMP);
        assertThat(testEcampagne.getIsopen()).isEqualTo(DEFAULT_ISOPEN);
    }

    @Test
    @Transactional
    void fullUpdateEcampagneWithPatch() throws Exception {
        // Initialize the database
        ecampagneRepository.saveAndFlush(ecampagne);

        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();

        // Update the ecampagne using partial update
        Ecampagne partialUpdatedEcampagne = new Ecampagne();
        partialUpdatedEcampagne.setId(ecampagne.getId());

        partialUpdatedEcampagne
            .objetcampagne(UPDATED_OBJETCAMPAGNE)
            .description(UPDATED_DESCRIPTION)
            .debutcampagne(UPDATED_DEBUTCAMPAGNE)
            .fincampagne(UPDATED_FINCAMPAGNE)
            .debutreelcamp(UPDATED_DEBUTREELCAMP)
            .finreelcamp(UPDATED_FINREELCAMP)
            .isopen(UPDATED_ISOPEN);

        restEcampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEcampagne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEcampagne))
            )
            .andExpect(status().isOk());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        Ecampagne testEcampagne = ecampagneList.get(ecampagneList.size() - 1);
        assertThat(testEcampagne.getObjetcampagne()).isEqualTo(UPDATED_OBJETCAMPAGNE);
        assertThat(testEcampagne.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEcampagne.getDebutcampagne()).isEqualTo(UPDATED_DEBUTCAMPAGNE);
        assertThat(testEcampagne.getFincampagne()).isEqualTo(UPDATED_FINCAMPAGNE);
        assertThat(testEcampagne.getDebutreelcamp()).isEqualTo(UPDATED_DEBUTREELCAMP);
        assertThat(testEcampagne.getFinreelcamp()).isEqualTo(UPDATED_FINREELCAMP);
        assertThat(testEcampagne.getIsopen()).isEqualTo(UPDATED_ISOPEN);
    }

    @Test
    @Transactional
    void patchNonExistingEcampagne() throws Exception {
        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        ecampagne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ecampagne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecampagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEcampagne() throws Exception {
        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        ecampagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecampagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEcampagne() throws Exception {
        int databaseSizeBeforeUpdate = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        ecampagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcampagneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ecampagne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ecampagne in the database
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEcampagne() throws Exception {
        // Initialize the database
        ecampagneRepository.saveAndFlush(ecampagne);
        ecampagneRepository.save(ecampagne);
        ecampagneSearchRepository.save(ecampagne);

        int databaseSizeBeforeDelete = ecampagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ecampagne
        restEcampagneMockMvc
            .perform(delete(ENTITY_API_URL_ID, ecampagne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ecampagne> ecampagneList = ecampagneRepository.findAll();
        assertThat(ecampagneList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ecampagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEcampagne() throws Exception {
        // Initialize the database
        ecampagne = ecampagneRepository.saveAndFlush(ecampagne);
        ecampagneSearchRepository.save(ecampagne);

        // Search the ecampagne
        restEcampagneMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ecampagne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecampagne.getId().intValue())))
            .andExpect(jsonPath("$.[*].objetcampagne").value(hasItem(DEFAULT_OBJETCAMPAGNE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].debutcampagne").value(hasItem(DEFAULT_DEBUTCAMPAGNE.toString())))
            .andExpect(jsonPath("$.[*].fincampagne").value(hasItem(DEFAULT_FINCAMPAGNE.toString())))
            .andExpect(jsonPath("$.[*].debutreelcamp").value(hasItem(DEFAULT_DEBUTREELCAMP.toString())))
            .andExpect(jsonPath("$.[*].finreelcamp").value(hasItem(DEFAULT_FINREELCAMP.toString())))
            .andExpect(jsonPath("$.[*].isopen").value(hasItem(DEFAULT_ISOPEN.booleanValue())));
    }
}
