package com.example.demo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//获取参数
@EnableBatchProcessing
@SpringBootApplication
public class BatchPractice {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step step4() {
		return stepBuilderFactory.get("step4").tasklet(tasklet3(null,null)).build();
	}

    @Bean
    @StepScope
    Tasklet tasklet3(@Value("#{jobParameters['bizDate']}") String bizDate,
            @Value("#{jobParameters['fileName']}") String fileName) {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				System.out.println(bizDate);
				System.out.println(fileName);

				return RepeatStatus.FINISHED;

			}
		};
	}

    @Bean
    Job job1() {

		return JobbuilderFactory.get("hello-job4").start(step4()).build();
	}
}
