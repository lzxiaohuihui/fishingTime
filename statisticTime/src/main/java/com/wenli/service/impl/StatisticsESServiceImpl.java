package com.wenli.service.impl;

import com.alibaba.fastjson.JSON;
import com.wenli.entity.po.StatisticsTimeDoc;
import com.wenli.service.StatisticsESService;
import org.apache.lucene.search.MultiTermQuery;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @date: 7/5/23 10:24 AM
 * @author: lzh
 */

@Service
public class StatisticsESServiceImpl implements StatisticsESService {

    @Autowired
    private RestHighLevelClient client;



    @Override
    public List<StatisticsTimeDoc> findByTermQuery(String indices, String[] args) {
        if (args == null || args.length < 2) return null;
        SearchRequest request = new SearchRequest(indices);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (!args[0].equals("")) boolQuery.should(QueryBuilders.termsQuery("app", args[0]));
        if (!args[1].equals("")) boolQuery.should(QueryBuilders.termsQuery("title", args[1]));
        request.source().query(boolQuery).size(10000);

        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response == null) return null;
        return handleResponse(response, StatisticsTimeDoc.class);
    }

    @Override
    public void addTerm(StatisticsTimeDoc statisticsTimeDoc) {
        String json = JSON.toJSONString(statisticsTimeDoc);

        IndexRequest request = new IndexRequest("statistics").id(statisticsTimeDoc.getId().toString());
        request.source(json, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());
    }

    private <T> List<T> handleResponse(SearchResponse response, Class<?> clazz) {
        List<T> res = new ArrayList<>();
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            res.add(JSON.parseObject(json, (Type) clazz));
        }
        return res;
    }
}
