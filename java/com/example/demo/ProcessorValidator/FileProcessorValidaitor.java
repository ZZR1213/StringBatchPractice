package com.example.demo.ProcessorValidator;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@EnableBatchProcessing
@SpringBootApplication
public class FileProcessorValidaitor {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	BeanValidatingItemProcessor<User> beanValidatingItemProcessor(){
		BeanValidatingItemProcessor<User> beanValidatingItemProcessor = new BeanValidatingItemProcessor<>();
		beanValidatingItemProcessor.setFilter(true);

		return beanValidatingItemProcessor;
		
	}

	@Bean
	FlatFileItemReader<User> fileItemReader() {
		
		return new FlatFileItemReaderBuilder<User>()
				            .name("userItemReader")
				            .resource(new ClassPathResource("user3Validaotor.txt"))
				            .delimited().delimiter("#")
				            .names("id","name","age")
				            .targetType(User.class)
				            .build();
	}
	
	@Bean
	ItemWriter<User> fileItemWriter(){
		return new ItemWriter<User>(){

			@Override
			public void write(List<? extends User> items) throws Exception {

				for(User item:items) {
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
	Step fileProcessorValidator() {
		return stepBuilderFactory.get("fileProcessorValidator")
				.<User,User>chunk(1)
				.reader(fileItemReader())
				.processor(beanValidatingItemProcessor())
				.writer(fileItemWriter())
				.build();
	}
	
	@Bean
	Job FileProcessorValidatorJob() throws Exception {
		return JobbuilderFactory.get("fileProcessorValidator-job").start(fileProcessorValidator()).incrementer(new RunIdIncrementer()).build();
	}
}
