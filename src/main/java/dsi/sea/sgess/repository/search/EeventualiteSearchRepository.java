package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Eeventualite;
import dsi.sea.sgess.repository.EeventualiteRepository;
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
 * Spring Data Elasticsearch repository for the {@link Eeventualite} entity.
 */
public interface EeventualiteSearchRepository extends ElasticsearchRepository<Eeventualite, Long>, EeventualiteSearchRepositoryInternal {}

interface EeventualiteSearchRepositoryInternal {
    Page<Eeventualite> search(String query, Pageable pageable);

    Page<Eeventualite> search(Query query);

    void index(Eeventualite entity);
}

class EeventualiteSearchRepositoryInternalImpl implements EeventualiteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EeventualiteRepository repository;

    EeventualiteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EeventualiteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Eeventualite> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Eeventualite> search(Query query) {
        SearchHits<Eeventualite> searchHits = elasticsearchTemplate.search(query, Eeventualite.class);
        List<Eeventualite> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Eeventualite entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
