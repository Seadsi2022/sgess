package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Egroupe;
import dsi.sea.sgess.repository.EgroupeRepository;
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
 * Spring Data Elasticsearch repository for the {@link Egroupe} entity.
 */
public interface EgroupeSearchRepository extends ElasticsearchRepository<Egroupe, Long>, EgroupeSearchRepositoryInternal {}

interface EgroupeSearchRepositoryInternal {
    Page<Egroupe> search(String query, Pageable pageable);

    Page<Egroupe> search(Query query);

    void index(Egroupe entity);
}

class EgroupeSearchRepositoryInternalImpl implements EgroupeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EgroupeRepository repository;

    EgroupeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EgroupeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Egroupe> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Egroupe> search(Query query) {
        SearchHits<Egroupe> searchHits = elasticsearchTemplate.search(query, Egroupe.class);
        List<Egroupe> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Egroupe entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
