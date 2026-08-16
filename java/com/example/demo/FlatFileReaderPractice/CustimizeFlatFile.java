package com.example.demo.FlatFileReaderPractice;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustimizeFlatFile implements FieldSetMapper<User2>{

	@Override
	public User2 mapFieldSet(FieldSet fieldSet) throws BindException {

		User2 user2 = new User2();

		user2.setId(fieldSet.readString("id"));
		user2.setName(fieldSet.readString("name"));
		user2.setAge(fieldSet.readString("age"));
		user2.setAddress(fieldSet.readString("city") + fieldSet.readString("district") + fieldSet.readString("street"));

		return user2;
	}
}
