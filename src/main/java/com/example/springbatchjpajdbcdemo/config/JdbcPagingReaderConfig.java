package com.example.springbatchjpajdbcdemo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.example.springbatchjpajdbcdemo.model.Line;
import com.example.springbatchjpajdbcdemo.reader.CompositeJdbcPagingItemReader;
import com.example.springbatchjpajdbcdemo.reader.LinePageProcessor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JdbcPagingReaderConfig {

	@Autowired
	private LinePageProcessor linePageProcessor;

	@Bean(name = "pagingItemReader")
	public ItemReader<Line> pagingItemReader(DataSource dataSource, PagingQueryProvider queryProvider) {
		return new JdbcPagingItemReaderBuilder<Line>().name("pagingItemReader").dataSource(dataSource).pageSize(3)
				.queryProvider(queryProvider).rowMapper(new BeanPropertyRowMapper<>(Line.class)).build();
	}

	@Bean(name = "compositeJdbcPagingItemReader")
	public CompositeJdbcPagingItemReader<Line> compositeJdbcPagingItemReader(DataSource dataSource,
			PagingQueryProvider queryProvider) {

		CompositeJdbcPagingItemReader<Line> compositeJdbcPagingItemReader = new CompositeJdbcPagingItemReader<>();
		compositeJdbcPagingItemReader.setDataSource(dataSource);
		compositeJdbcPagingItemReader.setQueryProvider(queryProvider);
		compositeJdbcPagingItemReader.setFetchSize(3);
		compositeJdbcPagingItemReader.setPageSize(3);
		compositeJdbcPagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(Line.class));
		compositeJdbcPagingItemReader.setPageProcessor(linePageProcessor);

		return compositeJdbcPagingItemReader;
	}

	@Bean
	public SqlPagingQueryProviderFactoryBean queryProvider(DataSource dataSource) {
		SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();

		provider.setDataSource(dataSource);
		provider.setSelectClause("SELECT id, name, dob");
		provider.setFromClause("FROM line");
		provider.setWhereClause("where name = 'John'");
		provider.setSortKeys(sortByEmailAddressAsc());

		return provider;
	}

	private Map<String, Order> sortByEmailAddressAsc() {
		Map<String, Order> sortConfiguration = new HashMap<>();
		sortConfiguration.put("id", Order.ASCENDING);
		return sortConfiguration;
	}

}
