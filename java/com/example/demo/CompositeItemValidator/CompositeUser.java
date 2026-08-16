package com.example.demo.CompositeItemValidator;

import javax.validation.constraints.NotBlank;

public class CompositeUser {

	private String id;

    @NotBlank(message = "name不能为空！！")
	private String name;

	private String age;
	
	public CompositeUser() {

		super();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

}
