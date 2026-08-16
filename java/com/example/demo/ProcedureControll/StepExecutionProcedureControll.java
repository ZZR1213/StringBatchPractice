package com.example.demo.ProcedureControll;

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
public class StepExecutionProcedureControll {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step firstStepProcedureCustimize() {
		return stepBuilderFactory.get("firstStepProcedureCustimize").tasklet(custimizeStepTasklet1()).build();
	}
    
    @Bean
    Step StepProcedureA() {
		return stepBuilderFactory.get("StepProcedureA").tasklet(custimizeStepTasklet2()).build();
	}
    
    @Bean
    Step StepProcedureB() {
		return stepBuilderFactory.get("StepProcedureB").tasklet(custimizeStepTasklet3()).build();
	}
    
    @Bean
    Step StepProcedureDefault() {
		return stepBuilderFactory.get("StepProcedureDefault").tasklet(custimizeStepTasklet4()).build();
	}

	@Bean
	Tasklet custimizeStepTasklet1() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				System.out.println("我是first");
				return RepeatStatus.FINISHED;

			}
		};
	}
	
	@Bean
	Tasklet custimizeStepTasklet2() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

                System.out.println("我是A step");
				return RepeatStatus.FINISHED;

			}
		};
	}
	
	@Bean
	Tasklet custimizeStepTasklet3() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

                System.out.println("我是B step");
				return RepeatStatus.FINISHED;

			}
		};
	}
	
	@Bean
	Tasklet custimizeStepTasklet4() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

                System.out.println("我是default step");
				return RepeatStatus.FINISHED;

			}
		};
	}

	@Bean
	StatusDecider statusDecider() {
		return new StatusDecider();
	}

	@Bean
	Job StepCustimizeProcedureJob() throws Exception {
		return JobbuilderFactory.get("stepCustimize-job").start(firstStepProcedureCustimize())
				.next(statusDecider())
				.from(statusDecider())
				.on("A").to(StepProcedureA())
				.from(statusDecider())
				.on("B").to(StepProcedureB())
				.from(statusDecider())
				.on("*").to(StepProcedureDefault())
				.end()
				.incrementer(new RunIdIncrementer())
				.build();
	}

}
