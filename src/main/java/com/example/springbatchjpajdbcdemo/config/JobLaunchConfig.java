package com.example.springbatchjpajdbcdemo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JobLaunchConfig {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job linesJob;

	@Scheduled(fixedRate = 5000000)
	public void run() throws Exception {
		JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		JobExecution execution = jobLauncher.run(linesJob, params);
		log.info("Exit status: {}", execution.getStatus());
	}
}
