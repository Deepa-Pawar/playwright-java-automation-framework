package com.qa.tests.e2e;

public record UserLoginData (
	String name,
	String username,
	String emailID,
	String password,
	String invalid_emailID,
	String invalid_password
){} 