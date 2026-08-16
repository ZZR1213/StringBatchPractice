package com.example.demo.ContextPractice;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class ContextPractice {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step stepContext1() {
		return stepBuilderFactory.get("step1").tasklet(taskletContext1()).build();
	}
    
    @Bean
    Step stepContext2() {
		return stepBuilderFactory.get("step2").tasklet(taskletContext2()).build();
	}

    @Bean
    Tasklet taskletContext1() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				// 设置job上下文
				ExecutionContext ecJob = context.getStepContext().getStepExecution().getJobExecution()
						.getExecutionContext();
				ecJob.put("name", "zzr");
				ecJob.put("age", "35");

				// 设置step3上下文
				ExecutionContext ecStep = context.getStepContext().getStepExecution().getExecutionContext();
				ecStep.put("name", "dl");
				ecStep.put("age", "36");

				return RepeatStatus.FINISHED;

			}
		};
	}
    
    @Bean
    Tasklet taskletContext2() {

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				
				//获取job上下文
				ExecutionContext ecJob = context.getStepContext().getStepExecution().getJobExecution()
						.getExecutionContext();
                System.out.println("我是job上下文的name: "+ ecJob.get("name"));
                System.out.println("我是job上下文的age: "+ ecJob.get("age"));
                
              //获取step上下文
				ExecutionContext ecStep = context.getStepContext().getStepExecution().getExecutionContext();
                System.out.println("我是step上下文的name: "+ ecStep.get("name"));
                System.out.println("我是step上下文的age: "+ ecStep.get("age"));

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
	Job jobContext() throws Exception {
		return JobbuilderFactory.get("Context-job").start(stepContext1()).next(stepContext2()).incrementer(new RunIdIncrementer()).build();
	}

}
