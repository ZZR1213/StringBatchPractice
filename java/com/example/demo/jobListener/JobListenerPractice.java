package com.example.demo.jobListener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListenerPractice implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {

		System.out.print("作业开始前： " + jobExecution.getStatus());

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		System.out.print("作业开始后： " + jobExecution.getStatus());

	}

}
