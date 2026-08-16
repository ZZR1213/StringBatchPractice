package com.example.demo.chunkPractice;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class ChunkTaskletPractice {

	@Autowired
	private JobBuilderFactory JobbuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	// 待处理数据的条数
	private int timer = 9;

	// 一次处理数据的条数
	private int chunkSize = 3;
	
	@Bean
	ItemReader<String> itemReader() {

		return new ItemReader<String>() {

			@Override
			public String read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				if (timer > 0) {
					timer--;
					System.out.println("正在读取数据...");
					return "读取成功，交给processor处理";

				} else {
					return null;
				}
			}
		};
	}
	
	@Bean
	ItemProcessor<String,String> itemProcessor() {

		return new ItemProcessor<String,String>() {

			@Override
			public String process(String item) throws Exception {

				System.out.println("processor拿到数据了！！！");
				
				return "数据加工完成，交给writer输出";
			}
		};
	}
	
	@Bean
	ItemWriter<String> itemWriter() {

		return new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {

				System.out.println("数据输出成功！！！");
			}
	};
	}

	@Bean
	Step chunkStep() {
		return stepBuilderFactory.get("step").<String,String>chunk(chunkSize).reader(itemReader()).processor(itemProcessor()).writer(itemWriter()).build();
	}

	@Bean
	Job jobChunkTasklet() throws Exception {
		return JobbuilderFactory.get("chunk-job").start(chunkStep()).incrementer(new RunIdIncrementer()).build();
	}

}
