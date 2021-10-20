package com.example.springbatchjpajdbcdemo.config;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.springbatchjpajdbcdemo.model.Line;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JdbcLineWriterConfig {

	private static final String QUERY_INSERT_LINE = "INSERT INTO line (id, name, age, dob)"
			+ " VALUES (:id, :name, :age, :dob)";

	@Bean(name = "jdbcBatchLineWriter")
	public ItemWriter<Line> jdbcBatchLineWriter(@Autowired DataSource dataSource,
			@Autowired NamedParameterJdbcTemplate jdbcTemplate) {

		JdbcBatchItemWriter<Line> writer = new JdbcBatchItemWriter<>();
		writer.setSql(QUERY_INSERT_LINE);
		writer.setDataSource(dataSource);
		writer.setJdbcTemplate(jdbcTemplate);

		// Because the names of our named parameters are equal to the property names of
		// the StudentDTO class, we can use the
		// BeanPropertyItemSqlParameterSourceProvider class for this purpose.
		ItemSqlParameterSourceProvider<Line> paramProvider = new BeanPropertyItemSqlParameterSourceProvider<>();
		writer.setItemSqlParameterSourceProvider(paramProvider);

		return writer;
	}
}
