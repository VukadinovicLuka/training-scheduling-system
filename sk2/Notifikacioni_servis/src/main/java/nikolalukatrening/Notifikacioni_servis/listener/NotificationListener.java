package nikolalukatrening.Notifikacioni_servis.listener;


import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import nikolalukatrening.Notifikacioni_servis.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    private EmailService emailService;

    @JmsListener(destination = "activationQueue")
    public void receiveMessage(EmailMessage emailMessage) {
        emailService.sendEmail(emailMessage);
    }
}
