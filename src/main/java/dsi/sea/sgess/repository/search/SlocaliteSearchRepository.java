package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Slocalite;
import dsi.sea.sgess.repository.SlocaliteRepository;
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
 * Spring Data Elasticsearch repository for the {@link Slocalite} entity.
 */
public interface SlocaliteSearchRepository extends ElasticsearchRepository<Slocalite, Long>, SlocaliteSearchRepositoryInternal {}

interface SlocaliteSearchRepositoryInternal {
    Page<Slocalite> search(String query, Pageable pageable);

    Page<Slocalite> search(Query query);

    void index(Slocalite entity);
}

class SlocaliteSearchRepositoryInternalImpl implements SlocaliteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SlocaliteRepository repository;

    SlocaliteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, SlocaliteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Slocalite> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Slocalite> search(Query query) {
        SearchHits<Slocalite> searchHits = elasticsearchTemplate.search(query, Slocalite.class);
        List<Slocalite> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Slocalite entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
