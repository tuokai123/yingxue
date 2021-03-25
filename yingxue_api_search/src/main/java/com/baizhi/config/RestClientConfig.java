package com.baizhi.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean  //网络osi   物理层最高  数据链路层  网络层 传输层 tcp 回话层 表示层 应用层 http  最低  //dubbo tcp性能
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("192.168.127.155:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}