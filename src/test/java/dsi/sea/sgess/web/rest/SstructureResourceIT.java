package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Sstructure;
import dsi.sea.sgess.repository.SstructureRepository;
import dsi.sea.sgess.repository.search.SstructureSearchRepository;
import dsi.sea.sgess.service.SstructureService;
import java.util.ArrayList;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
 * Integration tests for the {@link SstructureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SstructureResourceIT {

    private static final String DEFAULT_NOMSTRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_NOMSTRUCTURE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLESTRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLESTRUCTURE = "BBBBBBBBBB";

    private static final String DEFAULT_TELSTRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_TELSTRUCTURE = "BBBBBBBBBB";

    private static final String DEFAULT_BPSTRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_BPSTRUCTURE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAILSTRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_EMAILSTRUCTURE = "BBBBBBBBBB";

    private static final Long DEFAULT_PROFONDEUR = 1L;
    private static final Long UPDATED_PROFONDEUR = 2L;

    private static final String ENTITY_API_URL = "/api/sstructures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sstructures";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SstructureRepository sstructureRepository;

    @Mock
    private SstructureRepository sstructureRepositoryMock;

    @Mock
    private SstructureService sstructureServiceMock;

    @Autowired
    private SstructureSearchRepository sstructureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSstructureMockMvc;

    private Sstructure sstructure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sstructure createEntity(EntityManager em) {
        Sstructure sstructure = new Sstructure()
            .nomstructure(DEFAULT_NOMSTRUCTURE)
            .siglestructure(DEFAULT_SIGLESTRUCTURE)
            .telstructure(DEFAULT_TELSTRUCTURE)
            .bpstructure(DEFAULT_BPSTRUCTURE)
            .emailstructure(DEFAULT_EMAILSTRUCTURE)
            .profondeur(DEFAULT_PROFONDEUR);
        return sstructure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sstructure createUpdatedEntity(EntityManager em) {
        Sstructure sstructure = new Sstructure()
            .nomstructure(UPDATED_NOMSTRUCTURE)
            .siglestructure(UPDATED_SIGLESTRUCTURE)
            .telstructure(UPDATED_TELSTRUCTURE)
            .bpstructure(UPDATED_BPSTRUCTURE)
            .emailstructure(UPDATED_EMAILSTRUCTURE)
            .profondeur(UPDATED_PROFONDEUR);
        return sstructure;
    }

    @BeforeEach
    public void initTest() {
        sstructureSearchRepository.deleteAll();
        sstructure = createEntity(em);
    }

    @Test
    @Transactional
    void createSstructure() throws Exception {
        int databaseSizeBeforeCreate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        // Create the Sstructure
        restSstructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sstructure)))
            .andExpect(status().isCreated());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Sstructure testSstructure = sstructureList.get(sstructureList.size() - 1);
        assertThat(testSstructure.getNomstructure()).isEqualTo(DEFAULT_NOMSTRUCTURE);
        assertThat(testSstructure.getSiglestructure()).isEqualTo(DEFAULT_SIGLESTRUCTURE);
        assertThat(testSstructure.getTelstructure()).isEqualTo(DEFAULT_TELSTRUCTURE);
        assertThat(testSstructure.getBpstructure()).isEqualTo(DEFAULT_BPSTRUCTURE);
        assertThat(testSstructure.getEmailstructure()).isEqualTo(DEFAULT_EMAILSTRUCTURE);
        assertThat(testSstructure.getProfondeur()).isEqualTo(DEFAULT_PROFONDEUR);
    }

    @Test
    @Transactional
    void createSstructureWithExistingId() throws Exception {
        // Create the Sstructure with an existing ID
        sstructure.setId(1L);

        int databaseSizeBeforeCreate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSstructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sstructure)))
            .andExpect(status().isBadRequest());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSstructures() throws Exception {
        // Initialize the database
        sstructureRepository.saveAndFlush(sstructure);

        // Get all the sstructureList
        restSstructureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sstructure.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomstructure").value(hasItem(DEFAULT_NOMSTRUCTURE)))
            .andExpect(jsonPath("$.[*].siglestructure").value(hasItem(DEFAULT_SIGLESTRUCTURE)))
            .andExpect(jsonPath("$.[*].telstructure").value(hasItem(DEFAULT_TELSTRUCTURE)))
            .andExpect(jsonPath("$.[*].bpstructure").value(hasItem(DEFAULT_BPSTRUCTURE)))
            .andExpect(jsonPath("$.[*].emailstructure").value(hasItem(DEFAULT_EMAILSTRUCTURE)))
            .andExpect(jsonPath("$.[*].profondeur").value(hasItem(DEFAULT_PROFONDEUR.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSstructuresWithEagerRelationshipsIsEnabled() throws Exception {
        when(sstructureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSstructureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sstructureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSstructuresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sstructureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSstructureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sstructureRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSstructure() throws Exception {
        // Initialize the database
        sstructureRepository.saveAndFlush(sstructure);

        // Get the sstructure
        restSstructureMockMvc
            .perform(get(ENTITY_API_URL_ID, sstructure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sstructure.getId().intValue()))
            .andExpect(jsonPath("$.nomstructure").value(DEFAULT_NOMSTRUCTURE))
            .andExpect(jsonPath("$.siglestructure").value(DEFAULT_SIGLESTRUCTURE))
            .andExpect(jsonPath("$.telstructure").value(DEFAULT_TELSTRUCTURE))
            .andExpect(jsonPath("$.bpstructure").value(DEFAULT_BPSTRUCTURE))
            .andExpect(jsonPath("$.emailstructure").value(DEFAULT_EMAILSTRUCTURE))
            .andExpect(jsonPath("$.profondeur").value(DEFAULT_PROFONDEUR.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSstructure() throws Exception {
        // Get the sstructure
        restSstructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSstructure() throws Exception {
        // Initialize the database
        sstructureRepository.saveAndFlush(sstructure);

        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        sstructureSearchRepository.save(sstructure);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());

        // Update the sstructure
        Sstructure updatedSstructure = sstructureRepository.findById(sstructure.getId()).get();
        // Disconnect from session so that the updates on updatedSstructure are not directly saved in db
        em.detach(updatedSstructure);
        updatedSstructure
            .nomstructure(UPDATED_NOMSTRUCTURE)
            .siglestructure(UPDATED_SIGLESTRUCTURE)
            .telstructure(UPDATED_TELSTRUCTURE)
            .bpstructure(UPDATED_BPSTRUCTURE)
            .emailstructure(UPDATED_EMAILSTRUCTURE)
            .profondeur(UPDATED_PROFONDEUR);

        restSstructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSstructure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSstructure))
            )
            .andExpect(status().isOk());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        Sstructure testSstructure = sstructureList.get(sstructureList.size() - 1);
        assertThat(testSstructure.getNomstructure()).isEqualTo(UPDATED_NOMSTRUCTURE);
        assertThat(testSstructure.getSiglestructure()).isEqualTo(UPDATED_SIGLESTRUCTURE);
        assertThat(testSstructure.getTelstructure()).isEqualTo(UPDATED_TELSTRUCTURE);
        assertThat(testSstructure.getBpstructure()).isEqualTo(UPDATED_BPSTRUCTURE);
        assertThat(testSstructure.getEmailstructure()).isEqualTo(UPDATED_EMAILSTRUCTURE);
        assertThat(testSstructure.getProfondeur()).isEqualTo(UPDATED_PROFONDEUR);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Sstructure> sstructureSearchList = IterableUtils.toList(sstructureSearchRepository.findAll());
                Sstructure testSstructureSearch = sstructureSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSstructureSearch.getNomstructure()).isEqualTo(UPDATED_NOMSTRUCTURE);
                assertThat(testSstructureSearch.getSiglestructure()).isEqualTo(UPDATED_SIGLESTRUCTURE);
                assertThat(testSstructureSearch.getTelstructure()).isEqualTo(UPDATED_TELSTRUCTURE);
                assertThat(testSstructureSearch.getBpstructure()).isEqualTo(UPDATED_BPSTRUCTURE);
                assertThat(testSstructureSearch.getEmailstructure()).isEqualTo(UPDATED_EMAILSTRUCTURE);
                assertThat(testSstructureSearch.getProfondeur()).isEqualTo(UPDATED_PROFONDEUR);
            });
    }

    @Test
    @Transactional
    void putNonExistingSstructure() throws Exception {
        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        sstructure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSstructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sstructure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sstructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSstructure() throws Exception {
        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        sstructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSstructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sstructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSstructure() throws Exception {
        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        sstructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSstructureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sstructure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSstructureWithPatch() throws Exception {
        // Initialize the database
        sstructureRepository.saveAndFlush(sstructure);

        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();

        // Update the sstructure using partial update
        Sstructure partialUpdatedSstructure = new Sstructure();
        partialUpdatedSstructure.setId(sstructure.getId());

        partialUpdatedSstructure
            .nomstructure(UPDATED_NOMSTRUCTURE)
            .bpstructure(UPDATED_BPSTRUCTURE)
            .emailstructure(UPDATED_EMAILSTRUCTURE)
            .profondeur(UPDATED_PROFONDEUR);

        restSstructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSstructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSstructure))
            )
            .andExpect(status().isOk());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        Sstructure testSstructure = sstructureList.get(sstructureList.size() - 1);
        assertThat(testSstructure.getNomstructure()).isEqualTo(UPDATED_NOMSTRUCTURE);
        assertThat(testSstructure.getSiglestructure()).isEqualTo(DEFAULT_SIGLESTRUCTURE);
        assertThat(testSstructure.getTelstructure()).isEqualTo(DEFAULT_TELSTRUCTURE);
        assertThat(testSstructure.getBpstructure()).isEqualTo(UPDATED_BPSTRUCTURE);
        assertThat(testSstructure.getEmailstructure()).isEqualTo(UPDATED_EMAILSTRUCTURE);
        assertThat(testSstructure.getProfondeur()).isEqualTo(UPDATED_PROFONDEUR);
    }

    @Test
    @Transactional
    void fullUpdateSstructureWithPatch() throws Exception {
        // Initialize the database
        sstructureRepository.saveAndFlush(sstructure);

        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();

        // Update the sstructure using partial update
        Sstructure partialUpdatedSstructure = new Sstructure();
        partialUpdatedSstructure.setId(sstructure.getId());

        partialUpdatedSstructure
            .nomstructure(UPDATED_NOMSTRUCTURE)
            .siglestructure(UPDATED_SIGLESTRUCTURE)
            .telstructure(UPDATED_TELSTRUCTURE)
            .bpstructure(UPDATED_BPSTRUCTURE)
            .emailstructure(UPDATED_EMAILSTRUCTURE)
            .profondeur(UPDATED_PROFONDEUR);

        restSstructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSstructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSstructure))
            )
            .andExpect(status().isOk());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        Sstructure testSstructure = sstructureList.get(sstructureList.size() - 1);
        assertThat(testSstructure.getNomstructure()).isEqualTo(UPDATED_NOMSTRUCTURE);
        assertThat(testSstructure.getSiglestructure()).isEqualTo(UPDATED_SIGLESTRUCTURE);
        assertThat(testSstructure.getTelstructure()).isEqualTo(UPDATED_TELSTRUCTURE);
        assertThat(testSstructure.getBpstructure()).isEqualTo(UPDATED_BPSTRUCTURE);
        assertThat(testSstructure.getEmailstructure()).isEqualTo(UPDATED_EMAILSTRUCTURE);
        assertThat(testSstructure.getProfondeur()).isEqualTo(UPDATED_PROFONDEUR);
    }

    @Test
    @Transactional
    void patchNonExistingSstructure() throws Exception {
        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        sstructure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSstructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sstructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sstructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSstructure() throws Exception {
        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        sstructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSstructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sstructure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSstructure() throws Exception {
        int databaseSizeBeforeUpdate = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        sstructure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSstructureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sstructure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sstructure in the database
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSstructure() throws Exception {
        // Initialize the database
        sstructureRepository.saveAndFlush(sstructure);
        sstructureRepository.save(sstructure);
        sstructureSearchRepository.save(sstructure);

        int databaseSizeBeforeDelete = sstructureRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the sstructure
        restSstructureMockMvc
            .perform(delete(ENTITY_API_URL_ID, sstructure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sstructure> sstructureList = sstructureRepository.findAll();
        assertThat(sstructureList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sstructureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSstructure() throws Exception {
        // Initialize the database
        sstructure = sstructureRepository.saveAndFlush(sstructure);
        sstructureSearchRepository.save(sstructure);

        // Search the sstructure
        restSstructureMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sstructure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sstructure.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomstructure").value(hasItem(DEFAULT_NOMSTRUCTURE)))
            .andExpect(jsonPath("$.[*].siglestructure").value(hasItem(DEFAULT_SIGLESTRUCTURE)))
            .andExpect(jsonPath("$.[*].telstructure").value(hasItem(DEFAULT_TELSTRUCTURE)))
            .andExpect(jsonPath("$.[*].bpstructure").value(hasItem(DEFAULT_BPSTRUCTURE)))
            .andExpect(jsonPath("$.[*].emailstructure").value(hasItem(DEFAULT_EMAILSTRUCTURE)))
            .andExpect(jsonPath("$.[*].profondeur").value(hasItem(DEFAULT_PROFONDEUR.intValue())));
    }
}
