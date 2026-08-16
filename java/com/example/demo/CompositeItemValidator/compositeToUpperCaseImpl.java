package com.example.demo.CompositeItemValidator;

public class compositeToUpperCaseImpl {

	public CompositeUser compositeToUpperCase(CompositeUser user) {

		user.setName(user.getName().toUpperCase());

		return user;
	}

}
