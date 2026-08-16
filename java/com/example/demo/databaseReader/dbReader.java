package com.example.demo.databaseReader;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class dbReader {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;

	@Bean
	JdbcCursorItemReader<StudentInfo> jdbcCursorItemReader() {
		
		return new JdbcCursorItemReaderBuilder<StudentInfo>()
				.name("dbItemReader")
				.dataSource(dataSource)
				.sql("select * from student_information")
				.rowMapper(new StudentInfoRowmapper())
				.build();
	}
	
	@Bean
	ItemWriter<StudentInfo> dbItemWriter(){
		return new ItemWriter<StudentInfo>(){

			@Override
			public void write(List<? extends StudentInfo> items) throws Exception {

				for(StudentInfo item:items) {
					System.out.print(item.getId());
					System.out.print(" ");
					System.out.print(item.getName());
					System.out.print(" ");
					System.out.print(item.getAddress());
					System.out.print(" ");
					System.out.print(item.getEmail());
					
					System.out.println("");
				}
			}
		};
	}
	
	@Bean
	Step dbReaderStep() {
		return stepBuilderFactory.get("dbReaderStep")
				.<StudentInfo,StudentInfo>chunk(1)
				.reader(jdbcCursorItemReader())
				.writer(dbItemWriter())
				.build();
	}
	
	@Bean
	Job dbReaderjob() throws Exception {
		return JobbuilderFactory.get("dbReader-job").start(dbReaderStep()).incrementer(new RunIdIncrementer()).build();
	}
}
