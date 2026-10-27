package com.qa.tests.e2e;

public record RegistrationData (
	    String password,
	    String day,
	    String month,
	    String year,
	    String firstName,
	    String lastName,
	    String company,
	    String address,
	    String country,
	    String state,
	    String city,
	    String zipcode,
	    String mobileNumber,
	    PaymentDetails paymentDetails 
	) {}
	record PaymentDetails(
	    String nameOnCard,
	    String cardNumber,
	    String cvc,
	    String expirationMonth,
	    String expirationYear
	) {}
