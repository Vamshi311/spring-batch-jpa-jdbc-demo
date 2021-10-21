package com.example.springbatchjpajdbcdemo.reader;

import java.util.List;

public interface PageProcessor<T> {

	void process(List<T> page);
}
