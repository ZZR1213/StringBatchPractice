package com.example.demo.batch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class ValidatorPractice implements JobParametersValidator{

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		
		//待校验参数获取
		String exeDate = parameters.getString("bizDate");

		if(!StringUtils.hasText(exeDate)) {
			throw new JobParametersInvalidException("bizDate不能为空");
		}
	}

}
