package com.example.springbatchjpajdbcdemo.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springbatchjpajdbcdemo.model.Line;
import com.example.springbatchjpajdbcdemo.repository.LineRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JpaLinesWriter implements ItemWriter<Line> {

	@Autowired
	private LineRepository lineRepository;

	@Override
	public void write(List<? extends Line> lines) throws Exception {
		log.info("saving lines to database {}", lines);
		lineRepository.saveAll(lines);
		log.info("Saved lines to database");
	}

}
