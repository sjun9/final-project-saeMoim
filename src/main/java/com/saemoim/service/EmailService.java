package com.saemoim.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	void sendEmail(String email) throws MessagingException;

	void sendTempPasswordAndChangePassword(String email) throws MessagingException;
}
