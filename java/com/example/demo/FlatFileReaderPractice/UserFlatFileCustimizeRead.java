package com.example.demo.FlatFileReaderPractice;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@EnableBatchProcessing
@SpringBootApplication
public class UserFlatFileCustimizeRead {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	FlatFileItemReader<User2> flatFileCustimizeItemReader() {
		
		return new FlatFileItemReaderBuilder<User2>()
				            .name("userItemCusitimizeReader")
				            .resource(new ClassPathResource("user2.txt"))
				            .delimited().delimiter("#")
				            .names("id","name","age","city","district","street")
				            .fieldSetMapper(new CustimizeFlatFile())
				            .build();
	}
	
	@Bean
	ItemWriter<User2> CustimizeflatItemWriter(){

		return new ItemWriter<User2>(){

			@Override
			public void write(List<? extends User2> items) throws Exception {

				for(User2 item:items) {
					System.out.print(item.getId());
					System.out.print(" ");
					System.out.print(item.getName());
					System.out.print(" ");
					System.out.print(item.getAge());
					System.out.print(" ");
					System.out.print(item.getAddress());

					System.out.println("");
				}
			}
		};
	}
	
	@Bean
	Step fileCustimizeReaderStep() {
		return stepBuilderFactory.get("fileCustimizeReaderStep")
				.<User2,User2>chunk(1)
				.reader(flatFileCustimizeItemReader())
				.writer(CustimizeflatItemWriter())
				.build();
	}
	
	@Bean
	Job flatFileCustimizeReaderjob() throws Exception {
		return JobbuilderFactory.get("flatFileCustimizeReader-job").start(fileCustimizeReaderStep()).incrementer(new RunIdIncrementer()).build();
	}
}
