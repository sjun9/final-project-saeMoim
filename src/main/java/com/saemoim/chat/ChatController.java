package com.saemoim.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ChatController {

	@GetMapping("/chat")
	public String chatGET(HttpSession session) {
		System.out.println(session);
		System.out.println("@ChatController, chat GET()");

		return "chat";
	}
}
