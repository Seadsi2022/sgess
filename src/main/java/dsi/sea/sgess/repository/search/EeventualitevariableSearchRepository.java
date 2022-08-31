package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Eeventualitevariable;
import dsi.sea.sgess.repository.EeventualitevariableRepository;
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
 * Spring Data Elasticsearch repository for the {@link Eeventualitevariable} entity.
 */
public interface EeventualitevariableSearchRepository
    extends ElasticsearchRepository<Eeventualitevariable, Long>, EeventualitevariableSearchRepositoryInternal {}

interface EeventualitevariableSearchRepositoryInternal {
    Page<Eeventualitevariable> search(String query, Pageable pageable);

    Page<Eeventualitevariable> search(Query query);

    void index(Eeventualitevariable entity);
}

class EeventualitevariableSearchRepositoryInternalImpl implements EeventualitevariableSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EeventualitevariableRepository repository;

    EeventualitevariableSearchRepositoryInternalImpl(
        ElasticsearchRestTemplate elasticsearchTemplate,
        EeventualitevariableRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Eeventualitevariable> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Eeventualitevariable> search(Query query) {
        SearchHits<Eeventualitevariable> searchHits = elasticsearchTemplate.search(query, Eeventualitevariable.class);
        List<Eeventualitevariable> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Eeventualitevariable entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
