package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Equestionnaire;
import dsi.sea.sgess.repository.EquestionnaireRepository;
import dsi.sea.sgess.repository.search.EquestionnaireSearchRepository;
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
 * Integration tests for the {@link EquestionnaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquestionnaireResourceIT {

    private static final String DEFAULT_OBJETQUEST = "AAAAAAAAAA";
    private static final String UPDATED_OBJETQUEST = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTIONQUEST = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTIONQUEST = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/equestionnaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/equestionnaires";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquestionnaireRepository equestionnaireRepository;

    @Autowired
    private EquestionnaireSearchRepository equestionnaireSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquestionnaireMockMvc;

    private Equestionnaire equestionnaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equestionnaire createEntity(EntityManager em) {
        Equestionnaire equestionnaire = new Equestionnaire()
            .objetquest(DEFAULT_OBJETQUEST)
            .descriptionquest(DEFAULT_DESCRIPTIONQUEST)
            .isActive(DEFAULT_IS_ACTIVE);
        return equestionnaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equestionnaire createUpdatedEntity(EntityManager em) {
        Equestionnaire equestionnaire = new Equestionnaire()
            .objetquest(UPDATED_OBJETQUEST)
            .descriptionquest(UPDATED_DESCRIPTIONQUEST)
            .isActive(UPDATED_IS_ACTIVE);
        return equestionnaire;
    }

    @BeforeEach
    public void initTest() {
        equestionnaireSearchRepository.deleteAll();
        equestionnaire = createEntity(em);
    }

    @Test
    @Transactional
    void createEquestionnaire() throws Exception {
        int databaseSizeBeforeCreate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        // Create the Equestionnaire
        restEquestionnaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isCreated());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Equestionnaire testEquestionnaire = equestionnaireList.get(equestionnaireList.size() - 1);
        assertThat(testEquestionnaire.getObjetquest()).isEqualTo(DEFAULT_OBJETQUEST);
        assertThat(testEquestionnaire.getDescriptionquest()).isEqualTo(DEFAULT_DESCRIPTIONQUEST);
        assertThat(testEquestionnaire.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEquestionnaireWithExistingId() throws Exception {
        // Create the Equestionnaire with an existing ID
        equestionnaire.setId(1L);

        int databaseSizeBeforeCreate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquestionnaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEquestionnaires() throws Exception {
        // Initialize the database
        equestionnaireRepository.saveAndFlush(equestionnaire);

        // Get all the equestionnaireList
        restEquestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equestionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].objetquest").value(hasItem(DEFAULT_OBJETQUEST)))
            .andExpect(jsonPath("$.[*].descriptionquest").value(hasItem(DEFAULT_DESCRIPTIONQUEST)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEquestionnaire() throws Exception {
        // Initialize the database
        equestionnaireRepository.saveAndFlush(equestionnaire);

        // Get the equestionnaire
        restEquestionnaireMockMvc
            .perform(get(ENTITY_API_URL_ID, equestionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equestionnaire.getId().intValue()))
            .andExpect(jsonPath("$.objetquest").value(DEFAULT_OBJETQUEST))
            .andExpect(jsonPath("$.descriptionquest").value(DEFAULT_DESCRIPTIONQUEST))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEquestionnaire() throws Exception {
        // Get the equestionnaire
        restEquestionnaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEquestionnaire() throws Exception {
        // Initialize the database
        equestionnaireRepository.saveAndFlush(equestionnaire);

        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        equestionnaireSearchRepository.save(equestionnaire);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());

        // Update the equestionnaire
        Equestionnaire updatedEquestionnaire = equestionnaireRepository.findById(equestionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedEquestionnaire are not directly saved in db
        em.detach(updatedEquestionnaire);
        updatedEquestionnaire.objetquest(UPDATED_OBJETQUEST).descriptionquest(UPDATED_DESCRIPTIONQUEST).isActive(UPDATED_IS_ACTIVE);

        restEquestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEquestionnaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEquestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        Equestionnaire testEquestionnaire = equestionnaireList.get(equestionnaireList.size() - 1);
        assertThat(testEquestionnaire.getObjetquest()).isEqualTo(UPDATED_OBJETQUEST);
        assertThat(testEquestionnaire.getDescriptionquest()).isEqualTo(UPDATED_DESCRIPTIONQUEST);
        assertThat(testEquestionnaire.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Equestionnaire> equestionnaireSearchList = IterableUtils.toList(equestionnaireSearchRepository.findAll());
                Equestionnaire testEquestionnaireSearch = equestionnaireSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEquestionnaireSearch.getObjetquest()).isEqualTo(UPDATED_OBJETQUEST);
                assertThat(testEquestionnaireSearch.getDescriptionquest()).isEqualTo(UPDATED_DESCRIPTIONQUEST);
                assertThat(testEquestionnaireSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEquestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        equestionnaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equestionnaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        equestionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        equestionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquestionnaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equestionnaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEquestionnaireWithPatch() throws Exception {
        // Initialize the database
        equestionnaireRepository.saveAndFlush(equestionnaire);

        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();

        // Update the equestionnaire using partial update
        Equestionnaire partialUpdatedEquestionnaire = new Equestionnaire();
        partialUpdatedEquestionnaire.setId(equestionnaire.getId());

        partialUpdatedEquestionnaire.objetquest(UPDATED_OBJETQUEST).isActive(UPDATED_IS_ACTIVE);

        restEquestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquestionnaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        Equestionnaire testEquestionnaire = equestionnaireList.get(equestionnaireList.size() - 1);
        assertThat(testEquestionnaire.getObjetquest()).isEqualTo(UPDATED_OBJETQUEST);
        assertThat(testEquestionnaire.getDescriptionquest()).isEqualTo(DEFAULT_DESCRIPTIONQUEST);
        assertThat(testEquestionnaire.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEquestionnaireWithPatch() throws Exception {
        // Initialize the database
        equestionnaireRepository.saveAndFlush(equestionnaire);

        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();

        // Update the equestionnaire using partial update
        Equestionnaire partialUpdatedEquestionnaire = new Equestionnaire();
        partialUpdatedEquestionnaire.setId(equestionnaire.getId());

        partialUpdatedEquestionnaire.objetquest(UPDATED_OBJETQUEST).descriptionquest(UPDATED_DESCRIPTIONQUEST).isActive(UPDATED_IS_ACTIVE);

        restEquestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquestionnaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        Equestionnaire testEquestionnaire = equestionnaireList.get(equestionnaireList.size() - 1);
        assertThat(testEquestionnaire.getObjetquest()).isEqualTo(UPDATED_OBJETQUEST);
        assertThat(testEquestionnaire.getDescriptionquest()).isEqualTo(UPDATED_DESCRIPTIONQUEST);
        assertThat(testEquestionnaire.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEquestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        equestionnaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equestionnaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        equestionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        equestionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(equestionnaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equestionnaire in the database
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEquestionnaire() throws Exception {
        // Initialize the database
        equestionnaireRepository.saveAndFlush(equestionnaire);
        equestionnaireRepository.save(equestionnaire);
        equestionnaireSearchRepository.save(equestionnaire);

        int databaseSizeBeforeDelete = equestionnaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the equestionnaire
        restEquestionnaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, equestionnaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equestionnaire> equestionnaireList = equestionnaireRepository.findAll();
        assertThat(equestionnaireList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(equestionnaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEquestionnaire() throws Exception {
        // Initialize the database
        equestionnaire = equestionnaireRepository.saveAndFlush(equestionnaire);
        equestionnaireSearchRepository.save(equestionnaire);

        // Search the equestionnaire
        restEquestionnaireMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + equestionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equestionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].objetquest").value(hasItem(DEFAULT_OBJETQUEST)))
            .andExpect(jsonPath("$.[*].descriptionquest").value(hasItem(DEFAULT_DESCRIPTIONQUEST)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
