package com.example.demo.jobListener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobListenerannoPractice{

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {

		System.out.print("作业开始前： " + jobExecution.getStatus());

	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {

		System.out.print("作业开始后： " + jobExecution.getStatus());

	}

}
