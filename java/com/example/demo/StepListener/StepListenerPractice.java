package com.example.demo.StepListener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepListenerPractice implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {

		System.out.println("step执行前");

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		System.out.println("step执行后: " + stepExecution.getExitStatus());

		return stepExecution.getExitStatus();
	}

}
