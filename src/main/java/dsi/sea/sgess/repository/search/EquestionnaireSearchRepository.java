package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Equestionnaire;
import dsi.sea.sgess.repository.EquestionnaireRepository;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Equestionnaire} entity.
 */
public interface EquestionnaireSearchRepository
    extends ElasticsearchRepository<Equestionnaire, Long>, EquestionnaireSearchRepositoryInternal {}

interface EquestionnaireSearchRepositoryInternal {
    Page<Equestionnaire> search(String query, Pageable pageable);

    Page<Equestionnaire> search(Query query);

    void index(Equestionnaire entity);
}

class EquestionnaireSearchRepositoryInternalImpl implements EquestionnaireSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EquestionnaireRepository repository;

    EquestionnaireSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EquestionnaireRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Equestionnaire> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Equestionnaire> search(Query query) {
        SearchHits<Equestionnaire> searchHits = elasticsearchTemplate.search(query, Equestionnaire.class);
        List<Equestionnaire> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Equestionnaire entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
