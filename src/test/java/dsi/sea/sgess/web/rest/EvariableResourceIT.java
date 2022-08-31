package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Evariable;
import dsi.sea.sgess.repository.EvariableRepository;
import dsi.sea.sgess.repository.search.EvariableSearchRepository;
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
 * Integration tests for the {@link EvariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvariableResourceIT {

    private static final String DEFAULT_NOMVARIABLE = "AAAAAAAAAA";
    private static final String UPDATED_NOMVARIABLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCVARIABLE = "AAAAAAAAAA";
    private static final String UPDATED_DESCVARIABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CHAMP = "AAAAAAAAAA";
    private static final String UPDATED_CHAMP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/evariables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/evariables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvariableRepository evariableRepository;

    @Autowired
    private EvariableSearchRepository evariableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvariableMockMvc;

    private Evariable evariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evariable createEntity(EntityManager em) {
        Evariable evariable = new Evariable()
            .nomvariable(DEFAULT_NOMVARIABLE)
            .descvariable(DEFAULT_DESCVARIABLE)
            .champ(DEFAULT_CHAMP)
            .isActive(DEFAULT_IS_ACTIVE);
        return evariable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evariable createUpdatedEntity(EntityManager em) {
        Evariable evariable = new Evariable()
            .nomvariable(UPDATED_NOMVARIABLE)
            .descvariable(UPDATED_DESCVARIABLE)
            .champ(UPDATED_CHAMP)
            .isActive(UPDATED_IS_ACTIVE);
        return evariable;
    }

    @BeforeEach
    public void initTest() {
        evariableSearchRepository.deleteAll();
        evariable = createEntity(em);
    }

    @Test
    @Transactional
    void createEvariable() throws Exception {
        int databaseSizeBeforeCreate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        // Create the Evariable
        restEvariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evariable)))
            .andExpect(status().isCreated());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Evariable testEvariable = evariableList.get(evariableList.size() - 1);
        assertThat(testEvariable.getNomvariable()).isEqualTo(DEFAULT_NOMVARIABLE);
        assertThat(testEvariable.getDescvariable()).isEqualTo(DEFAULT_DESCVARIABLE);
        assertThat(testEvariable.getChamp()).isEqualTo(DEFAULT_CHAMP);
        assertThat(testEvariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEvariableWithExistingId() throws Exception {
        // Create the Evariable with an existing ID
        evariable.setId(1L);

        int databaseSizeBeforeCreate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evariable)))
            .andExpect(status().isBadRequest());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEvariables() throws Exception {
        // Initialize the database
        evariableRepository.saveAndFlush(evariable);

        // Get all the evariableList
        restEvariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomvariable").value(hasItem(DEFAULT_NOMVARIABLE)))
            .andExpect(jsonPath("$.[*].descvariable").value(hasItem(DEFAULT_DESCVARIABLE)))
            .andExpect(jsonPath("$.[*].champ").value(hasItem(DEFAULT_CHAMP)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEvariable() throws Exception {
        // Initialize the database
        evariableRepository.saveAndFlush(evariable);

        // Get the evariable
        restEvariableMockMvc
            .perform(get(ENTITY_API_URL_ID, evariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evariable.getId().intValue()))
            .andExpect(jsonPath("$.nomvariable").value(DEFAULT_NOMVARIABLE))
            .andExpect(jsonPath("$.descvariable").value(DEFAULT_DESCVARIABLE))
            .andExpect(jsonPath("$.champ").value(DEFAULT_CHAMP))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEvariable() throws Exception {
        // Get the evariable
        restEvariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvariable() throws Exception {
        // Initialize the database
        evariableRepository.saveAndFlush(evariable);

        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        evariableSearchRepository.save(evariable);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());

        // Update the evariable
        Evariable updatedEvariable = evariableRepository.findById(evariable.getId()).get();
        // Disconnect from session so that the updates on updatedEvariable are not directly saved in db
        em.detach(updatedEvariable);
        updatedEvariable
            .nomvariable(UPDATED_NOMVARIABLE)
            .descvariable(UPDATED_DESCVARIABLE)
            .champ(UPDATED_CHAMP)
            .isActive(UPDATED_IS_ACTIVE);

        restEvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvariable))
            )
            .andExpect(status().isOk());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        Evariable testEvariable = evariableList.get(evariableList.size() - 1);
        assertThat(testEvariable.getNomvariable()).isEqualTo(UPDATED_NOMVARIABLE);
        assertThat(testEvariable.getDescvariable()).isEqualTo(UPDATED_DESCVARIABLE);
        assertThat(testEvariable.getChamp()).isEqualTo(UPDATED_CHAMP);
        assertThat(testEvariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Evariable> evariableSearchList = IterableUtils.toList(evariableSearchRepository.findAll());
                Evariable testEvariableSearch = evariableSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEvariableSearch.getNomvariable()).isEqualTo(UPDATED_NOMVARIABLE);
                assertThat(testEvariableSearch.getDescvariable()).isEqualTo(UPDATED_DESCVARIABLE);
                assertThat(testEvariableSearch.getChamp()).isEqualTo(UPDATED_CHAMP);
                assertThat(testEvariableSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEvariable() throws Exception {
        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        evariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvariable() throws Exception {
        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        evariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvariable() throws Exception {
        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        evariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvariableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evariable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEvariableWithPatch() throws Exception {
        // Initialize the database
        evariableRepository.saveAndFlush(evariable);

        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();

        // Update the evariable using partial update
        Evariable partialUpdatedEvariable = new Evariable();
        partialUpdatedEvariable.setId(evariable.getId());

        partialUpdatedEvariable.nomvariable(UPDATED_NOMVARIABLE);

        restEvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvariable))
            )
            .andExpect(status().isOk());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        Evariable testEvariable = evariableList.get(evariableList.size() - 1);
        assertThat(testEvariable.getNomvariable()).isEqualTo(UPDATED_NOMVARIABLE);
        assertThat(testEvariable.getDescvariable()).isEqualTo(DEFAULT_DESCVARIABLE);
        assertThat(testEvariable.getChamp()).isEqualTo(DEFAULT_CHAMP);
        assertThat(testEvariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEvariableWithPatch() throws Exception {
        // Initialize the database
        evariableRepository.saveAndFlush(evariable);

        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();

        // Update the evariable using partial update
        Evariable partialUpdatedEvariable = new Evariable();
        partialUpdatedEvariable.setId(evariable.getId());

        partialUpdatedEvariable
            .nomvariable(UPDATED_NOMVARIABLE)
            .descvariable(UPDATED_DESCVARIABLE)
            .champ(UPDATED_CHAMP)
            .isActive(UPDATED_IS_ACTIVE);

        restEvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvariable))
            )
            .andExpect(status().isOk());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        Evariable testEvariable = evariableList.get(evariableList.size() - 1);
        assertThat(testEvariable.getNomvariable()).isEqualTo(UPDATED_NOMVARIABLE);
        assertThat(testEvariable.getDescvariable()).isEqualTo(UPDATED_DESCVARIABLE);
        assertThat(testEvariable.getChamp()).isEqualTo(UPDATED_CHAMP);
        assertThat(testEvariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEvariable() throws Exception {
        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        evariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvariable() throws Exception {
        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        evariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvariable() throws Exception {
        int databaseSizeBeforeUpdate = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        evariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvariableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evariable in the database
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEvariable() throws Exception {
        // Initialize the database
        evariableRepository.saveAndFlush(evariable);
        evariableRepository.save(evariable);
        evariableSearchRepository.save(evariable);

        int databaseSizeBeforeDelete = evariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the evariable
        restEvariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, evariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evariable> evariableList = evariableRepository.findAll();
        assertThat(evariableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEvariable() throws Exception {
        // Initialize the database
        evariable = evariableRepository.saveAndFlush(evariable);
        evariableSearchRepository.save(evariable);

        // Search the evariable
        restEvariableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + evariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomvariable").value(hasItem(DEFAULT_NOMVARIABLE)))
            .andExpect(jsonPath("$.[*].descvariable").value(hasItem(DEFAULT_DESCVARIABLE)))
            .andExpect(jsonPath("$.[*].champ").value(hasItem(DEFAULT_CHAMP)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
