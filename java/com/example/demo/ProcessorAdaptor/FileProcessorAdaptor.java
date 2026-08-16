package com.example.demo.ProcessorAdaptor;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@EnableBatchProcessing
@SpringBootApplication
public class FileProcessorAdaptor {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	FlatFileItemReader<User> adaptorItemReader() {
		
		return new FlatFileItemReaderBuilder<User>()
				            .name("userItemReader1")
				            .resource(new ClassPathResource("user.txt"))
				            .delimited().delimiter("#")
				            .names("id","name","age")
				            .targetType(User.class)
				            .build();
	}
	
	@Bean
	UserToUpperCaseImpl userToUpperCaseImpl() {

		return new UserToUpperCaseImpl();
	}
	
	@Bean
	ItemProcessorAdapter<User,User> itemProcessorAdaptor(){
		
		ItemProcessorAdapter<User,User> iA = new ItemProcessorAdapter<>();
		
		iA.setTargetObject(userToUpperCaseImpl());
		iA.setTargetMethod("toUpperCase");
		
		return iA;

	}
	
	@Bean
	ItemWriter<User> adaptorItemWriter(){
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
	Step ProcessorAdaptor() {
		return stepBuilderFactory.get("fileProcessorAdaptor")
				.<User,User>chunk(1)
				.reader(adaptorItemReader())
				.processor(itemProcessorAdaptor())
				.writer(adaptorItemWriter())
				.build();
	}
	
	@Bean
	Job FileProcessorAdaptorJob() throws Exception {
		return JobbuilderFactory.get("ProcessorAdaptor-job").start(ProcessorAdaptor()).incrementer(new RunIdIncrementer()).build();
	}
}
