package com.example.demo.jobListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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
public class StatusListenerJob {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step step8() {
		return stepBuilderFactory.get("step8").tasklet(tasklet7()).build();
	}

	@Bean
	Tasklet tasklet7() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				JobExecution je = contribution.getStepExecution().getJobExecution();
				System.out.print("作业执行中： " + je.getStatus());

				return RepeatStatus.FINISHED;

			}
		};
	}

    //自定义参数校验器
//	@Bean
//	ValidatorPractice validatorPractice() {
//		return new ValidatorPractice();
//	}
	
	// 默认校验器
//	@Bean
//	DefaultJobParametersValidator defaultJobParamterValidator() {
//
//		DefaultJobParametersValidator defaultJobParamterValidator = new DefaultJobParametersValidator();
//
//		//必须参数名设定
//		defaultJobParamterValidator.setRequiredKeys(new String[] {"name"});
//
//		//可选参数名设定
//		defaultJobParamterValidator.setOptionalKeys(new String[] {"age","bizDate","fileName","run.id"});
//
//		return defaultJobParamterValidator;
//	}
	
	// 组合校验器
//	@Bean
//	CompositeJobParametersValidator compositeJobParamterValidator() throws Exception {
//
//		CompositeJobParametersValidator compositeJobParametersValidator = new CompositeJobParametersValidator();
//
//		compositeJobParametersValidator
//				.setValidators(Arrays.asList(defaultJobParamterValidator()));
//
//		compositeJobParametersValidator.afterPropertiesSet();
//
//		return compositeJobParametersValidator;
//	}

	@Bean
	Job job6() throws Exception {
		return JobbuilderFactory.get("status-job7").start(step8()).listener(new JobListenerannoPractice()).build();
	}

}
