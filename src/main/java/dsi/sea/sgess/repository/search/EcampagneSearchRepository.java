package dsi.sea.sgess.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import dsi.sea.sgess.domain.Ecampagne;
import dsi.sea.sgess.repository.EcampagneRepository;
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
 * Spring Data Elasticsearch repository for the {@link Ecampagne} entity.
 */
public interface EcampagneSearchRepository extends ElasticsearchRepository<Ecampagne, Long>, EcampagneSearchRepositoryInternal {}

interface EcampagneSearchRepositoryInternal {
    Page<Ecampagne> search(String query, Pageable pageable);

    Page<Ecampagne> search(Query query);

    void index(Ecampagne entity);
}

class EcampagneSearchRepositoryInternalImpl implements EcampagneSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EcampagneRepository repository;

    EcampagneSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EcampagneRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Ecampagne> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Ecampagne> search(Query query) {
        SearchHits<Ecampagne> searchHits = elasticsearchTemplate.search(query, Ecampagne.class);
        List<Ecampagne> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Ecampagne entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
