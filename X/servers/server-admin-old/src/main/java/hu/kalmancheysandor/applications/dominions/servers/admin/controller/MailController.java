package hu.kalmancheysandor.applications.dominions.servers.admin.controller;

import hu.kalmancheysandor.applications.dominions.servers.admin.service.email.EmailService;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.email.EmailService;

import hu.kalmancheysandor.applications.dominions.servers.admin.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/mail")
@Slf4j
public class MailController {


    @Autowired
    EmailService emailService;

    @GetMapping("/send/simple")
    @ResponseStatus(HttpStatus.OK)
    public void sendSimple() {
//        EmailDetails details = new EmailDetails();
//        details.setRecipient("kalmanczheysandor@gmail.com");
//        details.setMsgBody("Hello ica");
//        details.setSubject("Spring Mail Test");

        emailService.sendSimpleMessage("kalmanczheysandor@gmail.com","Test","Hello! itt geza!");
        System.out.println("Email sent");
    }


}
