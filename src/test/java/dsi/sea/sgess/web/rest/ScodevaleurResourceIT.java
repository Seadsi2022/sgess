package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Scodevaleur;
import dsi.sea.sgess.repository.ScodevaleurRepository;
import dsi.sea.sgess.repository.search.ScodevaleurSearchRepository;
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
 * Integration tests for the {@link ScodevaleurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScodevaleurResourceIT {

    private static final String DEFAULT_CODEVALEUR_LIB = "AAAAAAAAAA";
    private static final String UPDATED_CODEVALEUR_LIB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scodevaleurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scodevaleurs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScodevaleurRepository scodevaleurRepository;

    @Autowired
    private ScodevaleurSearchRepository scodevaleurSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScodevaleurMockMvc;

    private Scodevaleur scodevaleur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scodevaleur createEntity(EntityManager em) {
        Scodevaleur scodevaleur = new Scodevaleur().codevaleurLib(DEFAULT_CODEVALEUR_LIB);
        return scodevaleur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scodevaleur createUpdatedEntity(EntityManager em) {
        Scodevaleur scodevaleur = new Scodevaleur().codevaleurLib(UPDATED_CODEVALEUR_LIB);
        return scodevaleur;
    }

    @BeforeEach
    public void initTest() {
        scodevaleurSearchRepository.deleteAll();
        scodevaleur = createEntity(em);
    }

    @Test
    @Transactional
    void createScodevaleur() throws Exception {
        int databaseSizeBeforeCreate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        // Create the Scodevaleur
        restScodevaleurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scodevaleur)))
            .andExpect(status().isCreated());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Scodevaleur testScodevaleur = scodevaleurList.get(scodevaleurList.size() - 1);
        assertThat(testScodevaleur.getCodevaleurLib()).isEqualTo(DEFAULT_CODEVALEUR_LIB);
    }

    @Test
    @Transactional
    void createScodevaleurWithExistingId() throws Exception {
        // Create the Scodevaleur with an existing ID
        scodevaleur.setId(1L);

        int databaseSizeBeforeCreate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restScodevaleurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scodevaleur)))
            .andExpect(status().isBadRequest());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllScodevaleurs() throws Exception {
        // Initialize the database
        scodevaleurRepository.saveAndFlush(scodevaleur);

        // Get all the scodevaleurList
        restScodevaleurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scodevaleur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codevaleurLib").value(hasItem(DEFAULT_CODEVALEUR_LIB)));
    }

    @Test
    @Transactional
    void getScodevaleur() throws Exception {
        // Initialize the database
        scodevaleurRepository.saveAndFlush(scodevaleur);

        // Get the scodevaleur
        restScodevaleurMockMvc
            .perform(get(ENTITY_API_URL_ID, scodevaleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scodevaleur.getId().intValue()))
            .andExpect(jsonPath("$.codevaleurLib").value(DEFAULT_CODEVALEUR_LIB));
    }

    @Test
    @Transactional
    void getNonExistingScodevaleur() throws Exception {
        // Get the scodevaleur
        restScodevaleurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScodevaleur() throws Exception {
        // Initialize the database
        scodevaleurRepository.saveAndFlush(scodevaleur);

        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        scodevaleurSearchRepository.save(scodevaleur);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());

        // Update the scodevaleur
        Scodevaleur updatedScodevaleur = scodevaleurRepository.findById(scodevaleur.getId()).get();
        // Disconnect from session so that the updates on updatedScodevaleur are not directly saved in db
        em.detach(updatedScodevaleur);
        updatedScodevaleur.codevaleurLib(UPDATED_CODEVALEUR_LIB);

        restScodevaleurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScodevaleur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScodevaleur))
            )
            .andExpect(status().isOk());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        Scodevaleur testScodevaleur = scodevaleurList.get(scodevaleurList.size() - 1);
        assertThat(testScodevaleur.getCodevaleurLib()).isEqualTo(UPDATED_CODEVALEUR_LIB);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Scodevaleur> scodevaleurSearchList = IterableUtils.toList(scodevaleurSearchRepository.findAll());
                Scodevaleur testScodevaleurSearch = scodevaleurSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testScodevaleurSearch.getCodevaleurLib()).isEqualTo(UPDATED_CODEVALEUR_LIB);
            });
    }

    @Test
    @Transactional
    void putNonExistingScodevaleur() throws Exception {
        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        scodevaleur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScodevaleurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scodevaleur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scodevaleur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchScodevaleur() throws Exception {
        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        scodevaleur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodevaleurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scodevaleur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScodevaleur() throws Exception {
        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        scodevaleur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodevaleurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scodevaleur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateScodevaleurWithPatch() throws Exception {
        // Initialize the database
        scodevaleurRepository.saveAndFlush(scodevaleur);

        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();

        // Update the scodevaleur using partial update
        Scodevaleur partialUpdatedScodevaleur = new Scodevaleur();
        partialUpdatedScodevaleur.setId(scodevaleur.getId());

        partialUpdatedScodevaleur.codevaleurLib(UPDATED_CODEVALEUR_LIB);

        restScodevaleurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScodevaleur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScodevaleur))
            )
            .andExpect(status().isOk());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        Scodevaleur testScodevaleur = scodevaleurList.get(scodevaleurList.size() - 1);
        assertThat(testScodevaleur.getCodevaleurLib()).isEqualTo(UPDATED_CODEVALEUR_LIB);
    }

    @Test
    @Transactional
    void fullUpdateScodevaleurWithPatch() throws Exception {
        // Initialize the database
        scodevaleurRepository.saveAndFlush(scodevaleur);

        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();

        // Update the scodevaleur using partial update
        Scodevaleur partialUpdatedScodevaleur = new Scodevaleur();
        partialUpdatedScodevaleur.setId(scodevaleur.getId());

        partialUpdatedScodevaleur.codevaleurLib(UPDATED_CODEVALEUR_LIB);

        restScodevaleurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScodevaleur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScodevaleur))
            )
            .andExpect(status().isOk());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        Scodevaleur testScodevaleur = scodevaleurList.get(scodevaleurList.size() - 1);
        assertThat(testScodevaleur.getCodevaleurLib()).isEqualTo(UPDATED_CODEVALEUR_LIB);
    }

    @Test
    @Transactional
    void patchNonExistingScodevaleur() throws Exception {
        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        scodevaleur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScodevaleurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scodevaleur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scodevaleur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScodevaleur() throws Exception {
        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        scodevaleur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodevaleurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scodevaleur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScodevaleur() throws Exception {
        int databaseSizeBeforeUpdate = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        scodevaleur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodevaleurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(scodevaleur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scodevaleur in the database
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteScodevaleur() throws Exception {
        // Initialize the database
        scodevaleurRepository.saveAndFlush(scodevaleur);
        scodevaleurRepository.save(scodevaleur);
        scodevaleurSearchRepository.save(scodevaleur);

        int databaseSizeBeforeDelete = scodevaleurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the scodevaleur
        restScodevaleurMockMvc
            .perform(delete(ENTITY_API_URL_ID, scodevaleur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scodevaleur> scodevaleurList = scodevaleurRepository.findAll();
        assertThat(scodevaleurList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodevaleurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchScodevaleur() throws Exception {
        // Initialize the database
        scodevaleur = scodevaleurRepository.saveAndFlush(scodevaleur);
        scodevaleurSearchRepository.save(scodevaleur);

        // Search the scodevaleur
        restScodevaleurMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + scodevaleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scodevaleur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codevaleurLib").value(hasItem(DEFAULT_CODEVALEUR_LIB)));
    }
}
