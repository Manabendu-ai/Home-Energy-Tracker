package riku.spring.alert_service.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import riku.spring.alert_service.model.Alert;
import riku.spring.alert_service.repo.AlertRepo;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final AlertRepo repo;

    public MailService(JavaMailSender mailSender, AlertRepo repo){
        this.mailSender = mailSender;
        this.repo = repo;
    }

    public void sendEmail(
        String to,
        String subject,
        String body,
        Long userId
    ){
        boolean sent = false;
        log.info("Sending email to : {}, subject: {}",to, subject);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom("noreply@riku.com");
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        try{
            mailSender.send(mailMessage);
            sent = true;
        } catch (MailException e) {
            log.error("Failed to send email to : {}", to, e);
        } finally {
            final Alert alert = Alert.builder()
                    .sent(sent)
                    .createdAt(LocalDateTime.now())
                    .userId(userId)
                    .build();
            repo.saveAndFlush(alert);
        }
        log.info("email sent to : {}",to);
    }

}
