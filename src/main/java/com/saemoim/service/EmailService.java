package com.saemoim.service;

import jakarta.mail.MessagingException;

public interface EmailService {
	String sendEmail(String email) throws MessagingException;
}
