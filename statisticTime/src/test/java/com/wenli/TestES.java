package com.wenli;

import com.alibaba.fastjson.JSON;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.entity.po.StatisticsTimeDoc;
import com.wenli.mapper.StatisticsTimeMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @date: 7/4/23 3:26 PM
 * @author: lzh
 */
//@Ignore
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestES {

//    @Autowired
//    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private RestHighLevelClient client;

    @Resource
    private StatisticsTimeMapper statisticsTimeMapper;

//    @Test
//    public void testWrite() {
//        List<StatisticsTime> statisticsTimes = statisticsTimeMapper.selectList(null);
//        for (StatisticsTime statisticsTime : statisticsTimes) {
//            elasticsearchOperations.save(statisticsTime);
//        }
//
//    }

    @Test
    public void testAddDocument() throws IOException {
        StatisticsTime statisticsTime = statisticsTimeMapper.selectById(117440664);
        StatisticsTimeDoc statisticsTimeDoc = new StatisticsTimeDoc(statisticsTime);
        String json = JSON.toJSONString(statisticsTimeDoc);

        IndexRequest request = new IndexRequest("statistics").id(statisticsTimeDoc.getId().toString());
        request.source(json, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @Test
    public void testQueryDocument() throws IOException {
        GetRequest request = new GetRequest("statistics", "117440664");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        StatisticsTimeDoc statisticsTimeDoc = JSON.parseObject(json, StatisticsTimeDoc.class);
        System.out.println(statisticsTimeDoc);
    }

    @Test
    public void testMatchDocument() throws IOException {
        SearchRequest request = new SearchRequest("statistics");
        request.source().query(QueryBuilders.termsQuery("title", "数"));
        request.source().size(10000);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        List<StatisticsTimeDoc> statisticsTimeDocs = handleResponse(response, StatisticsTimeDoc.class);
        System.out.println(statisticsTimeDocs.size());
        System.out.println(statisticsTimeDocs);
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


//    @Test
//    public void testQueryTerm(){
//        String term = "google";
//
//        TermsQueryBuilder query = QueryBuilders.termsQuery("title", term);
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .withQuery(query)
//                .build();
//        org.springframework.data.elasticsearch.core.SearchHits<StatisticsTime> search = elasticsearchOperations.search(nativeSearchQuery, StatisticsTime.class);
//        System.out.println(search);
//    }

//    @Test
//    public void createIndex() throws IOException {
//        String MAPPING_TEMPLATE = "{\n" +
//                "    \"properties\": {\n" +
//                "      \"id\": {\n" +
//                "        \"type\": \"keyword\"\n" +
//                "      },\n" +
//                "      \"app\": {\n" +
//                "        \"type\": \"text\",\n" +
//                "        \"analyzer\": \"ik_max_word\",\n" +
//                "        \"copy_to\": \"all\"\n" +
//                "      },\n" +
//                "      \"title\": {\n" +
//                "        \"type\": \"text\",\n" +
//                "        \"analyzer\": \"ik_max_word\",\n" +
//                "        \"copy_to\": \"all\"\n" +
//                "      },\n" +
//                "      \"begin\": {\n" +
//                "        \"type\": \"long\",\n" +
//                "        \"copy_to\": \"all\"\n" +
//                "      },\n" +
//                "      \"end\": {\n" +
//                "        \"type\": \"long\",\n" +
//                "        \"copy_to\": \"all\"\n" +
//                "      },\n" +
//                "      \"running\": {\n" +
//                "        \"type\": \"long\",\n" +
//                "        \"copy_to\": \"all\"\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }";
//        CreateIndexRequest request = new CreateIndexRequest("statistics");
//        // 2.准备请求的参数：DSL语句
//        request.mapping(MAPPING_TEMPLATE, XContentType.JSON);
//        // 3.发送请求
//        client.indices().create(request, RequestOptions.DEFAULT);
//        client.close();
//    }
}
