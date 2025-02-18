package com.backend.allreva.concert.infra.elasticsearch;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.backend.allreva.concert.exception.search.ElasticSearchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class CustomConcertSearchRepoImpl implements CustomConcertSearchRepo {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchHits<ConcertDocument> searchMainConcerts(
            final String address,
            final List<Object> searchAfter,
            final int size,
            final SortDirection sortDirection) {
        try {
            NativeQuery searchQuery = getNativeQuery(SearchField.ADDRESS, address, searchAfter, size, sortDirection, false);
            //System.out.println("searchQuery.getQuery() = " + searchQuery.getQuery());
            return elasticsearchOperations.search(searchQuery, ConcertDocument.class);
        }catch (ElasticSearchException e){
            throw new ElasticSearchException();
        }
    }

    @Override
    public SearchHits<ConcertDocument> searchByTitleList(
            final String query,
            final List<Object> searchAfter,
            final int size) {
        return searchByTitleList(query, searchAfter, size, false);
    }

    @Override
    public SearchHits<ConcertDocument> searchByTitleListAll(
            String query,
            List<Object> searchAfter,
            int size) {
        return searchByTitleList(query, searchAfter, size, false);
    }

    private SearchHits<ConcertDocument> searchByTitleList(
            final String query,
            final List<Object> searchAfter,
            final int size,
            final boolean filterByDate) {
        try{
            NativeQuery searchQuery = getNativeQuery(SearchField.TITLE, query, searchAfter, size, SortDirection.SCORE, filterByDate);
            //System.out.println("searchQuery.getQuery() = " + searchQuery.getQuery());
            return elasticsearchOperations.search(searchQuery, ConcertDocument.class);
        }catch (ElasticSearchException e){
            throw new ElasticSearchException();
        }
    }

    private NativeQuery getNativeQuery(
            final SearchField searchField,
            final String searchTerm,
            final List<Object> searchAfter,
            final int size,
            final SortDirection sortDirection,
            final boolean filterByDate) {

        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
                .withQuery(buildSearchQuery(searchField, searchTerm, filterByDate))
                .withSort(buildPrimarySort(sortDirection))
                .withSort(buildSecondarySort())
                .withPageable(PageRequest.of(0, size));

        if(searchAfter != null && !searchAfter.isEmpty()){
            nativeQueryBuilder.withSearchAfter(searchAfter);
        }
        return nativeQueryBuilder.build();
    }

    private Query buildSearchQuery(final SearchField searchField, final String searchTerm, final boolean filterByDate) {
        if (!StringUtils.hasText(searchTerm)) {
            return filterByDate ? buildDateFilterQuery() : Query.of(q -> q.matchAll(m -> m));
        }

        return Query.of(q -> q
                .bool(b -> {
                    BoolQuery.Builder builder = b
                            .must(m -> m
                                    .match(mt -> mt
                                            .field(searchField.getFieldName())
                                            .query(searchTerm)
                                            .fuzziness("AUTO")
                                    )
                            );
                    if (filterByDate) {
                        builder.filter(f -> f
                                .range(r -> r
                                        .field("eddate")
                                        .gte(JsonData.of("now"))
                                )
                        );
                    }
                    return builder;
                })
        );
    }

    private Query buildDateFilterQuery() {
        return Query.of(q -> q
                .bool(b -> b
                        .filter(f -> f
                                .range(r -> r
                                        .field("eddate")
                                        .gte(JsonData.of("now/d"))
                                        .format("strict_date_optional_time")
                                )
                        )
                )
        );
    }




    private SortOptions buildPrimarySort(final SortDirection sortDirection) {
        return SortOptions.of(s -> s
                .field(f -> f
                        .field(getSortField(sortDirection))
                        .order(SortOrder.Desc)
                )
        );
    }

    private SortOptions buildSecondarySort() {
        return SortOptions.of(s -> s
                .field(f -> f
                        .field("id")
                        .order(SortOrder.Asc)
                )
        );
    }

    private String getSortField(final SortDirection sortDirection) {
        return switch (sortDirection) {
            case DATE -> "stdate";
            case VIEWS -> "view_count";
            case SCORE -> "_score";
        };
    }
}
