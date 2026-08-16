package com.example.demo.ProcedureControll;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class StepExecutionProcedureCustimizeControll {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step firstStepProcedure() {
		return stepBuilderFactory.get("firstStepProcedure").tasklet(stepListenerTasklet1()).build();
	}
    
    @Bean
    Step successStepProcedure() {
		return stepBuilderFactory.get("successStepProcedure").tasklet(stepListenerTasklet2()).build();
	}
    
    @Bean
    Step failStepProcedure() {
		return stepBuilderFactory.get("failStepProcedure").tasklet(stepListenerTasklet3()).build();
	}

	@Bean
	Tasklet stepListenerTasklet1() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				System.out.println("我是first");
				contribution.setExitStatus(ExitStatus.FAILED);
				return RepeatStatus.FINISHED;

			}
		};
	}
	
	@Bean
	Tasklet stepListenerTasklet2() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

                System.out.println("我是success");
				return RepeatStatus.FINISHED;

			}
		};
	}
	
	@Bean
	Tasklet stepListenerTasklet3() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

                System.out.println("我是fail");
				return RepeatStatus.FINISHED;

			}
		};
	}

	@Bean
	Job StepExecutionProcedureJob() throws Exception {
		return JobbuilderFactory.get("stepProcedure-job").start(firstStepProcedure())
				.on("FAILED").to(failStepProcedure())
				.from(firstStepProcedure()).on("*")
				.to(successStepProcedure())
				.end()
				.incrementer(new RunIdIncrementer())
				.build();
	}

}
