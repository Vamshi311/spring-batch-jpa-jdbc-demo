package com.example.springbatchjpajdbcdemo.reader;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springbatchjpajdbcdemo.model.Line;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LinePageProcessor implements PageProcessor<Line> {

	@Override
	public void process(List<Line> page) {

		List<Long> ids = Arrays.asList(2l, 5l);
		log.info("Read line page {}", page);

		// update age in each line. Able to update the data inside the page but content
		// remove items from the page.
		page.stream().forEach(line -> line.setAge(25l));

		log.info("Processed line page {}", page);

	}

}
