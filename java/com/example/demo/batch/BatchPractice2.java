package com.example.demo.batch;

import java.util.Map;

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
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//获取参数另一种方式
@EnableBatchProcessing
@SpringBootApplication
public class BatchPractice2 {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step step5() {
		return stepBuilderFactory.get("step5").tasklet(tasklet4()).build();
	}

    @Bean
    @StepScope
    Tasklet tasklet4() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				Map<String,Object> jobPara = context.getStepContext().getJobParameters();
				System.out.println(jobPara.get("bizDate"));
				System.out.println(jobPara.get("fileName"));

				return RepeatStatus.FINISHED;

			}
		};
	}

    @Bean
    Job job2() {

		return JobbuilderFactory.get("para-job4").start(step5()).build();
	}

}
