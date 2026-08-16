package com.example.demo.CompositeItemValidator;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;



@EnableBatchProcessing
@SpringBootApplication
public class CompositeProcessorValidator {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	FlatFileItemReader<CompositeUser> compositeItemReader() {
		
		return new FlatFileItemReaderBuilder<CompositeUser>()
				            .name("userItemReader1")
				            .resource(new ClassPathResource("CompositeValidator.txt"))
				            .delimited().delimiter("#")
				            .names("id","name","age")
				            .targetType(CompositeUser.class)
				            .build();
	}
	

	
	@Bean
	ItemProcessorAdapter<CompositeUser,CompositeUser> adaptorItemProcessor(){
		
		ItemProcessorAdapter<CompositeUser,CompositeUser> iA = new ItemProcessorAdapter<>();
		
		iA.setTargetObject(new compositeToUpperCaseImpl());
		iA.setTargetMethod("compositeToUpperCase");
		
		return iA;

	}
	
	@Bean
	BeanValidatingItemProcessor<CompositeUser> compositeValidatingItemProcessor(){
		BeanValidatingItemProcessor<CompositeUser> beanValidatingItemProcessor = new BeanValidatingItemProcessor<>();
		beanValidatingItemProcessor.setFilter(true);

		return beanValidatingItemProcessor;
		
	}
	
	@Bean
	CompositeItemProcessor<CompositeUser,CompositeUser> compositeItemProcessor(){
		
		CompositeItemProcessor<CompositeUser,CompositeUser> cip = new CompositeItemProcessor<>();
		cip.setDelegates(Arrays.asList(compositeValidatingItemProcessor(),adaptorItemProcessor()));
		
		return cip;
		
	}

	@Bean
	ItemWriter<CompositeUser> compositeItemWriter(){
		return new ItemWriter<CompositeUser>(){

			@Override
			public void write(List<? extends CompositeUser> items) throws Exception {

				for(CompositeUser item:items) {
					System.out.print(item.getId());
					System.out.print(" ");
					System.out.print(item.getName());
					System.out.print(" ");
					System.out.print(item.getAge());
					
					System.out.println("");
				}
			}
		};
	}
	
	@Bean
	Step CompositeProcessorStep() {
		return stepBuilderFactory.get("CompositeProcessorStep")
				.<CompositeUser,CompositeUser>chunk(1)
				.reader(compositeItemReader())
				.processor(compositeItemProcessor())
				.writer(compositeItemWriter())
				.build();
	}
	
	@Bean
	Job CompositeProcessorJob() throws Exception {
		return JobbuilderFactory.get("CompositeProcessor-job").start(CompositeProcessorStep()).incrementer(new RunIdIncrementer()).build();
	}
}
