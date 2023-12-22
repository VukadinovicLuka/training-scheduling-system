package nikolalukatrening.Notifikacioni_servis.service.impl;

import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import nikolalukatrening.Notifikacioni_servis.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) emailSender;
        mailSenderImpl.getJavaMailProperties().put("mail.smtp.ssl.trust", "*");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nivancev02@gmail.com");
        message.setTo(emailMessage.getTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getBody());
        mailSenderImpl.send(message);
    }
}


