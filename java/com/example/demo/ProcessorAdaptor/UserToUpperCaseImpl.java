package com.example.demo.ProcessorAdaptor;

public class UserToUpperCaseImpl {

	public User toUpperCase(User user) {

		user.setName(user.getName().toUpperCase());

		return user;
	}

}
