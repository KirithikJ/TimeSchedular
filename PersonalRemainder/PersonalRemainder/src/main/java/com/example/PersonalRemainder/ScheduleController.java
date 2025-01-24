package com.example.PersonalRemainder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ScheduleController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String showForm() {
        return "Message";
    }

    @PostMapping("/submit")
    public String submitNote(
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            @RequestParam("Month") int month,
            @RequestParam("Day") int day,
            Model model) {

       
        LocalDateTime deliveryDate = LocalDateTime.now().plusMonths(month).plusDays(day);

       
        Message newMessage = new Message();
        newMessage.setEmail(email);
        newMessage.setMessage(message);
        newMessage.setDeliveryDate(deliveryDate);
        newMessage.setStatus("Scheduled");

        messageRepository.save(newMessage);

        emailService.sendEmail(email, "Message from Your Future Self", message);

        newMessage.setStatus("Sent");
        messageRepository.save(newMessage);

        model.addAttribute("email", email);
        model.addAttribute("message", message);
        model.addAttribute("deliveryDate", deliveryDate);

        return "success";
    }
}
