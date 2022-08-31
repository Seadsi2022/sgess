package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Etypechamp;
import dsi.sea.sgess.repository.EtypechampRepository;
import dsi.sea.sgess.repository.search.EtypechampSearchRepository;
import dsi.sea.sgess.service.EtypechampService;
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
 * Integration tests for the {@link EtypechampResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtypechampResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAYNAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAYNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/etypechamps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/etypechamps";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtypechampRepository etypechampRepository;

    @Mock
    private EtypechampRepository etypechampRepositoryMock;

    @Mock
    private EtypechampService etypechampServiceMock;

    @Autowired
    private EtypechampSearchRepository etypechampSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtypechampMockMvc;

    private Etypechamp etypechamp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etypechamp createEntity(EntityManager em) {
        Etypechamp etypechamp = new Etypechamp().name(DEFAULT_NAME).displayname(DEFAULT_DISPLAYNAME);
        return etypechamp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etypechamp createUpdatedEntity(EntityManager em) {
        Etypechamp etypechamp = new Etypechamp().name(UPDATED_NAME).displayname(UPDATED_DISPLAYNAME);
        return etypechamp;
    }

    @BeforeEach
    public void initTest() {
        etypechampSearchRepository.deleteAll();
        etypechamp = createEntity(em);
    }

    @Test
    @Transactional
    void createEtypechamp() throws Exception {
        int databaseSizeBeforeCreate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        // Create the Etypechamp
        restEtypechampMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etypechamp)))
            .andExpect(status().isCreated());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Etypechamp testEtypechamp = etypechampList.get(etypechampList.size() - 1);
        assertThat(testEtypechamp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEtypechamp.getDisplayname()).isEqualTo(DEFAULT_DISPLAYNAME);
    }

    @Test
    @Transactional
    void createEtypechampWithExistingId() throws Exception {
        // Create the Etypechamp with an existing ID
        etypechamp.setId(1L);

        int databaseSizeBeforeCreate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtypechampMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etypechamp)))
            .andExpect(status().isBadRequest());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEtypechamps() throws Exception {
        // Initialize the database
        etypechampRepository.saveAndFlush(etypechamp);

        // Get all the etypechampList
        restEtypechampMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etypechamp.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].displayname").value(hasItem(DEFAULT_DISPLAYNAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtypechampsWithEagerRelationshipsIsEnabled() throws Exception {
        when(etypechampServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtypechampMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(etypechampServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtypechampsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(etypechampServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtypechampMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(etypechampRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEtypechamp() throws Exception {
        // Initialize the database
        etypechampRepository.saveAndFlush(etypechamp);

        // Get the etypechamp
        restEtypechampMockMvc
            .perform(get(ENTITY_API_URL_ID, etypechamp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etypechamp.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.displayname").value(DEFAULT_DISPLAYNAME));
    }

    @Test
    @Transactional
    void getNonExistingEtypechamp() throws Exception {
        // Get the etypechamp
        restEtypechampMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEtypechamp() throws Exception {
        // Initialize the database
        etypechampRepository.saveAndFlush(etypechamp);

        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        etypechampSearchRepository.save(etypechamp);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());

        // Update the etypechamp
        Etypechamp updatedEtypechamp = etypechampRepository.findById(etypechamp.getId()).get();
        // Disconnect from session so that the updates on updatedEtypechamp are not directly saved in db
        em.detach(updatedEtypechamp);
        updatedEtypechamp.name(UPDATED_NAME).displayname(UPDATED_DISPLAYNAME);

        restEtypechampMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtypechamp.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtypechamp))
            )
            .andExpect(status().isOk());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        Etypechamp testEtypechamp = etypechampList.get(etypechampList.size() - 1);
        assertThat(testEtypechamp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEtypechamp.getDisplayname()).isEqualTo(UPDATED_DISPLAYNAME);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Etypechamp> etypechampSearchList = IterableUtils.toList(etypechampSearchRepository.findAll());
                Etypechamp testEtypechampSearch = etypechampSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEtypechampSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testEtypechampSearch.getDisplayname()).isEqualTo(UPDATED_DISPLAYNAME);
            });
    }

    @Test
    @Transactional
    void putNonExistingEtypechamp() throws Exception {
        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        etypechamp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtypechampMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etypechamp.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etypechamp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtypechamp() throws Exception {
        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        etypechamp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypechampMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etypechamp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtypechamp() throws Exception {
        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        etypechamp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypechampMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etypechamp)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEtypechampWithPatch() throws Exception {
        // Initialize the database
        etypechampRepository.saveAndFlush(etypechamp);

        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();

        // Update the etypechamp using partial update
        Etypechamp partialUpdatedEtypechamp = new Etypechamp();
        partialUpdatedEtypechamp.setId(etypechamp.getId());

        restEtypechampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtypechamp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtypechamp))
            )
            .andExpect(status().isOk());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        Etypechamp testEtypechamp = etypechampList.get(etypechampList.size() - 1);
        assertThat(testEtypechamp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEtypechamp.getDisplayname()).isEqualTo(DEFAULT_DISPLAYNAME);
    }

    @Test
    @Transactional
    void fullUpdateEtypechampWithPatch() throws Exception {
        // Initialize the database
        etypechampRepository.saveAndFlush(etypechamp);

        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();

        // Update the etypechamp using partial update
        Etypechamp partialUpdatedEtypechamp = new Etypechamp();
        partialUpdatedEtypechamp.setId(etypechamp.getId());

        partialUpdatedEtypechamp.name(UPDATED_NAME).displayname(UPDATED_DISPLAYNAME);

        restEtypechampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtypechamp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtypechamp))
            )
            .andExpect(status().isOk());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        Etypechamp testEtypechamp = etypechampList.get(etypechampList.size() - 1);
        assertThat(testEtypechamp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEtypechamp.getDisplayname()).isEqualTo(UPDATED_DISPLAYNAME);
    }

    @Test
    @Transactional
    void patchNonExistingEtypechamp() throws Exception {
        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        etypechamp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtypechampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etypechamp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etypechamp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtypechamp() throws Exception {
        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        etypechamp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypechampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etypechamp))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtypechamp() throws Exception {
        int databaseSizeBeforeUpdate = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        etypechamp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypechampMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etypechamp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etypechamp in the database
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEtypechamp() throws Exception {
        // Initialize the database
        etypechampRepository.saveAndFlush(etypechamp);
        etypechampRepository.save(etypechamp);
        etypechampSearchRepository.save(etypechamp);

        int databaseSizeBeforeDelete = etypechampRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the etypechamp
        restEtypechampMockMvc
            .perform(delete(ENTITY_API_URL_ID, etypechamp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etypechamp> etypechampList = etypechampRepository.findAll();
        assertThat(etypechampList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypechampSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEtypechamp() throws Exception {
        // Initialize the database
        etypechamp = etypechampRepository.saveAndFlush(etypechamp);
        etypechampSearchRepository.save(etypechamp);

        // Search the etypechamp
        restEtypechampMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + etypechamp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etypechamp.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].displayname").value(hasItem(DEFAULT_DISPLAYNAME)));
    }
}
