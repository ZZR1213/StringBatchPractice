package com.example.demo.ProcedureControll;

import java.util.Random;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class StatusDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		
		int ret = new Random().nextInt(3);
		
		if(ret==1) {
			
			return new FlowExecutionStatus("A");
			
		} 
		else if(ret==2){
			
			return new FlowExecutionStatus("B");
			
		} else {
			
			return new FlowExecutionStatus("C");
		}
	}
}