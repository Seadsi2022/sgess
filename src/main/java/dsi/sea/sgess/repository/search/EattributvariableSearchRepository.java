package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Eattributvariable;
import dsi.sea.sgess.repository.EattributvariableRepository;
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
 * Spring Data Elasticsearch repository for the {@link Eattributvariable} entity.
 */
public interface EattributvariableSearchRepository
    extends ElasticsearchRepository<Eattributvariable, Long>, EattributvariableSearchRepositoryInternal {}

interface EattributvariableSearchRepositoryInternal {
    Page<Eattributvariable> search(String query, Pageable pageable);

    Page<Eattributvariable> search(Query query);

    void index(Eattributvariable entity);
}

class EattributvariableSearchRepositoryInternalImpl implements EattributvariableSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EattributvariableRepository repository;

    EattributvariableSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EattributvariableRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Eattributvariable> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Eattributvariable> search(Query query) {
        SearchHits<Eattributvariable> searchHits = elasticsearchTemplate.search(query, Eattributvariable.class);
        List<Eattributvariable> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Eattributvariable entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
