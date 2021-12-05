package com.kristovski.seaunitsmonitoring.utils.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service

@Slf4j
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Military Boat Warning");
            helper.setFrom("kristovski.dev@gmail.com");
            mailSender.send(mimeMessage);
            log.info("email send");
        }catch (MessagingException e){
            log.error("failed to send email", e);
            throw new IllegalStateException("failed to sedn email");
        }
    }
}
