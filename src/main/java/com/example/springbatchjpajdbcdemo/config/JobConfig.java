package com.example.springbatchjpajdbcdemo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.example.springbatchjpajdbcdemo.listener.JobCompletionNotificationListener;
import com.example.springbatchjpajdbcdemo.model.Line;
import com.example.springbatchjpajdbcdemo.processor.LinesProcessor;
import com.example.springbatchjpajdbcdemo.reader.CompositeJdbcPagingItemReader;
import com.example.springbatchjpajdbcdemo.reader.JdbcCursorLineReader;
import com.example.springbatchjpajdbcdemo.repository.LineRepository;
import com.example.springbatchjpajdbcdemo.writer.JpaLinesWriter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private LinesProcessor linesProcessor;

	@Autowired
	private JdbcCursorLineReader jdbcCursorLineReader;

	@Qualifier("pagingItemReader")
	@Autowired
	private ItemReader<Line> pagingItemReader;

	@Autowired
	private JpaLinesWriter jpaLinesWriter;

	@Qualifier("compositeJdbcPagingItemReader")
	@Autowired
	private CompositeJdbcPagingItemReader<Line> compositeJdbcPagingItemReader;

	@Qualifier("jdbcBatchLineWriter")
	@Autowired
	private ItemWriter<Line> jdbcBatchLineWriter;

	@Bean
	public RepositoryItemReader<Line> reader() {
		
		RepositoryItemReader<Line> reader = new RepositoryItemReader<>();
		reader.setRepository(lineRepository);
		reader.setMethodName("findByName");

		List<Object> queryMethodArguments = new ArrayList<>();
		// for name
		queryMethodArguments.add("John");

		reader.setArguments(queryMethodArguments);
		// Even though the page size is 100, reader will read only 2 records because
		// chunk size is 2.
		reader.setPageSize(100);
		Map<String, Direction> sorts = new HashMap<>();
		sorts.put("id", Direction.ASC);
		reader.setSort(sorts);

		return reader;
	}

	@Bean(name = "jdbcCursorAlternateReader")
	public ItemReader<Line> jdbcCursorAlternateReader() {
		return new JdbcCursorItemReaderBuilder<Line>().name("jdbcCursorAlternateReader")
				.sql("SELECT id, name, dob FROM line where name = 'John'")
				.dataSource(dataSource)
				.fetchSize(100).saveState(true)
				.rowMapper(new BeanPropertyRowMapper<>(Line.class))// dto and table column names match. So, we can use
																	// BeanPropertyRowMapper
				.build();
	}

	@Bean
	public RepositoryItemWriter<Line> writer() {
		RepositoryItemWriter<Line> writer = new RepositoryItemWriter<>();
		writer.setRepository(lineRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step lineStep() {
		// chunk size is 2.
		return stepBuilderFactory.get("lineStep").<Line, Line>chunk(2)
				// we can use different DB readers
				// .reader(reader()).processor(linesProcessor)
				// .reader(jdbcCursorLineReader)
				// .reader(jdbcCursorAlternateReader())
				// .reader(pagingItemReader)
				.reader(compositeJdbcPagingItemReader)
				// below are the options for line writer. we can use RepositoryItemWriter or our
				// custom linesWriter
				// .writer(writer())
				// .writer(jdbcBatchLineWriter) // for inserting the entity to database
				.writer(jpaLinesWriter).build();
	}

	@Bean
	public Job linesJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("linesJob").incrementer(new RunIdIncrementer()).listener(listener)
				.start(lineStep()).build();
	}

}
