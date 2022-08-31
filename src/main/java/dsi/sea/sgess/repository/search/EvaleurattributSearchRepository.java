package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Evaleurattribut;
import dsi.sea.sgess.repository.EvaleurattributRepository;
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
 * Spring Data Elasticsearch repository for the {@link Evaleurattribut} entity.
 */
public interface EvaleurattributSearchRepository
    extends ElasticsearchRepository<Evaleurattribut, Long>, EvaleurattributSearchRepositoryInternal {}

interface EvaleurattributSearchRepositoryInternal {
    Page<Evaleurattribut> search(String query, Pageable pageable);

    Page<Evaleurattribut> search(Query query);

    void index(Evaleurattribut entity);
}

class EvaleurattributSearchRepositoryInternalImpl implements EvaleurattributSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EvaleurattributRepository repository;

    EvaleurattributSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EvaleurattributRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Evaleurattribut> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Evaleurattribut> search(Query query) {
        SearchHits<Evaleurattribut> searchHits = elasticsearchTemplate.search(query, Evaleurattribut.class);
        List<Evaleurattribut> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Evaleurattribut entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
