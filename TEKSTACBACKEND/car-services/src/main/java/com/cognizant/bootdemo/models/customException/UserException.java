package com.cognizant.bootdemo.models.customException;

@SuppressWarnings("serial")
public class UserException extends RuntimeException {
	public UserException(String str)
	{
		super(str);
	}
}
