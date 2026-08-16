package com.example.demo.batch.validator;

import java.util.Arrays;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//获取参数另一种方式
@EnableBatchProcessing
@SpringBootApplication
public class BatchValidatorPractice {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    @Bean
    Step step6() {
		return stepBuilderFactory.get("step6").tasklet(tasklet5()).build();
	}

    @Bean
    Tasklet tasklet5() {

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

    //自定义参数校验器
	@Bean
	ValidatorPractice validatorPractice() {
		return new ValidatorPractice();
	}
	
	// 默认校验器
	@Bean
	DefaultJobParametersValidator defaultJobParamterValidator() {

		DefaultJobParametersValidator defaultJobParamterValidator = new DefaultJobParametersValidator();

		//必须参数名设定
		defaultJobParamterValidator.setRequiredKeys(new String[] {"name"});

		//可选参数名设定
		defaultJobParamterValidator.setOptionalKeys(new String[] {"age","bizDate","fileName","run.id"});

		return defaultJobParamterValidator;
	}
	
	// 组合校验器
	@Bean
	CompositeJobParametersValidator compositeJobParamterValidator() throws Exception {

		CompositeJobParametersValidator compositeJobParametersValidator = new CompositeJobParametersValidator();

		compositeJobParametersValidator
				.setValidators(Arrays.asList(validatorPractice(), defaultJobParamterValidator()));

		compositeJobParametersValidator.afterPropertiesSet();

		return compositeJobParametersValidator;
	}

	@Bean
	Job job4() throws Exception {
		return JobbuilderFactory.get("para-job5").validator(compositeJobParamterValidator()).start(step6()).incrementer(new RunIdIncrementer()).build();
	}

}
