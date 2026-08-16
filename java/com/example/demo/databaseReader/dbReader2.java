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
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

@EnableBatchProcessing
@SpringBootApplication
public class dbReader2 {
	
	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;

	@Bean
	JdbcCursorItemReader<StudentInfo> jdbcCursorItemReader2() {
		System.out.println("i am here!");
		
		return new JdbcCursorItemReaderBuilder<StudentInfo>()
				.name("dbItemReader2")
				.dataSource(dataSource)
				.sql("select * from student_information where student_id=?")
				.preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[] {1}))
				.rowMapper(new StudentInfoRowmapper())
				.build();
	}
	
	@Bean
	ItemWriter<StudentInfo> dbItemWriter2(){
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
	Step dbReaderStep2() {
		return stepBuilderFactory.get("dbReaderStep2")
				.<StudentInfo,StudentInfo>chunk(1)
				.reader(jdbcCursorItemReader2())
				.writer(dbItemWriter2())
				.build();
	}
	
	@Bean
	Job dbReaderjob2() throws Exception {
		return JobbuilderFactory.get("dbReader-job2").start(dbReaderStep2()).incrementer(new RunIdIncrementer()).build();
	}
}
