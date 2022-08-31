package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Scodevaleur;
import dsi.sea.sgess.repository.ScodevaleurRepository;
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
 * Spring Data Elasticsearch repository for the {@link Scodevaleur} entity.
 */
public interface ScodevaleurSearchRepository extends ElasticsearchRepository<Scodevaleur, Long>, ScodevaleurSearchRepositoryInternal {}

interface ScodevaleurSearchRepositoryInternal {
    Page<Scodevaleur> search(String query, Pageable pageable);

    Page<Scodevaleur> search(Query query);

    void index(Scodevaleur entity);
}

class ScodevaleurSearchRepositoryInternalImpl implements ScodevaleurSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ScodevaleurRepository repository;

    ScodevaleurSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, ScodevaleurRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Scodevaleur> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Scodevaleur> search(Query query) {
        SearchHits<Scodevaleur> searchHits = elasticsearchTemplate.search(query, Scodevaleur.class);
        List<Scodevaleur> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Scodevaleur entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
