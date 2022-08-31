package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Eunite;
import dsi.sea.sgess.repository.EuniteRepository;
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
 * Spring Data Elasticsearch repository for the {@link Eunite} entity.
 */
public interface EuniteSearchRepository extends ElasticsearchRepository<Eunite, Long>, EuniteSearchRepositoryInternal {}

interface EuniteSearchRepositoryInternal {
    Page<Eunite> search(String query, Pageable pageable);

    Page<Eunite> search(Query query);

    void index(Eunite entity);
}

class EuniteSearchRepositoryInternalImpl implements EuniteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EuniteRepository repository;

    EuniteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EuniteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Eunite> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Eunite> search(Query query) {
        SearchHits<Eunite> searchHits = elasticsearchTemplate.search(query, Eunite.class);
        List<Eunite> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Eunite entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
