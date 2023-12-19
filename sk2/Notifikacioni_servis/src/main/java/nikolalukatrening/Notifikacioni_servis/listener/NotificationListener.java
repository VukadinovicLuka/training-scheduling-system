package nikolalukatrening.Notifikacioni_servis.listener;


import jakarta.jms.JMSException;
import jakarta.jms.Message;
import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import nikolalukatrening.Notifikacioni_servis.service.impl.EmailServiceImpl;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private EmailServiceImpl emailServiceImpl;
    private MessageHelper messageHelper;

    public NotificationListener(EmailServiceImpl emailServiceImpl, MessageHelper messageHelper) {
        this.emailServiceImpl = emailServiceImpl;
        this.messageHelper = messageHelper;
    }

    @JmsListener(destination = "${destination.createActivation}", concurrency = "5-10")
    public void onActivationMessage(Message message) throws JMSException {
        EmailMessage emailMessage = messageHelper.getMessage(message, EmailMessage.class);
        System.out.println("Activation message received");
        System.out.println("Email message: " + emailMessage);

        // treba emailMessage dodati u h2 bazu za notifikacije


//        emailService.sendEmail(emailMessage);

    }
}
