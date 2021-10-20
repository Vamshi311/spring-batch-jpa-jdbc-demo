package com.example.springbatchjpajdbcdemo.processor;

import java.time.LocalDate;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.springbatchjpajdbcdemo.model.Line;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LinesProcessor implements ItemProcessor<Line, Line> {

	@Override
	public Line process(Line line) throws Exception {
		line.setAge(new Long(LocalDate.now().getYear() - line.getDob().getYear()));
		log.info("processed {}", line);
		return line;
	}

}
