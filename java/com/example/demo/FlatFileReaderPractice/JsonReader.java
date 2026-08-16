package com.example.demo.FlatFileReaderPractice;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableBatchProcessing
@SpringBootApplication
public class JsonReader {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	JsonItemReader<User3> jsonItemReader() {
		
		JacksonJsonObjectReader<User3> jor = new JacksonJsonObjectReader<>(User3.class);
		jor.setMapper(new ObjectMapper());

		return new JsonItemReaderBuilder<User3>()
				            .name("jsonItemReader")
				            .resource(new ClassPathResource("User3.json"))
				            .jsonObjectReader(jor)
				            .build();
	}
	
	@Bean
	ItemWriter<User3> jsonItemWriter(){
		return new ItemWriter<User3>(){

			@Override
			public void write(List<? extends User3> items) throws Exception {

				for(User3 item:items) {
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
	Step jsonfileReaderStep() {
		return stepBuilderFactory.get("fileReaderStep")
				.<User3,User3>chunk(1)
				.reader(jsonItemReader())
				.writer(jsonItemWriter())
				.build();
	}
	
	@Bean
	Job jsonFileReaderjob() throws Exception {
		return JobbuilderFactory.get("jsonFileReader-job").start(jsonfileReaderStep()).incrementer(new RunIdIncrementer()).build();
	}
}
