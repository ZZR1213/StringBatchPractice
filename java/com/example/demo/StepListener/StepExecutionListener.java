package com.example.demo.StepListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class StepExecutionListener {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step stepListener() {
		return stepBuilderFactory.get("stepListener").listener(new StepListenerPractice()).tasklet(stepListenerTasklet()).build();
	}

	@Bean
	Tasklet stepListenerTasklet() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

                System.out.println("step执行中");
				return RepeatStatus.FINISHED;

			}
		};
	}

	@Bean
	Job StepExecutionListenerJob() throws Exception {
		return JobbuilderFactory.get("stepListener-job").start(stepListener()).build();
	}

}
