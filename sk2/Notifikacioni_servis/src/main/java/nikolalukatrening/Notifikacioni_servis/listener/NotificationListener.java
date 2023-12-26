package nikolalukatrening.Notifikacioni_servis.listener;


import jakarta.jms.JMSException;
import jakarta.jms.Message;
import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import nikolalukatrening.Notifikacioni_servis.model.NotificationType;
import nikolalukatrening.Notifikacioni_servis.repository.EmailMessageRepository;
import nikolalukatrening.Notifikacioni_servis.service.EmailService;
import nikolalukatrening.Notifikacioni_servis.service.impl.EmailServiceImpl;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private EmailService emailService;
    private MessageHelper messageHelper;
    private EmailMessageRepository emailMessageRepository;

    public NotificationListener(EmailService emailService, MessageHelper messageHelper,EmailMessageRepository emailMessageRepository) {
        this.emailService = emailService;
        this.messageHelper = messageHelper;
        this.emailMessageRepository = emailMessageRepository;
    }

    @JmsListener(destination = "${destination.createActivation}", concurrency = "5-10")
    public void onActivationMessage(Message message) throws JMSException {
        EmailMessage emailMessage = messageHelper.getMessage(message, EmailMessage.class);
        System.out.println("Activation message received");
        System.out.println("Email message: " + emailMessage);

        NotificationType notificationType = new NotificationType();
        notificationType.setType(emailMessage.getType());
        notificationType.setReceiver(emailMessage.getTo());
        if (emailMessage.getParams().get("clientId") != null) notificationType.setClientId(Long.valueOf(emailMessage.getParams().get("clientId")));
        if (emailMessage.getParams().get("link") != null) notificationType.setLink(emailMessage.getParams().get("link"));
        notificationType.setFirstName(emailMessage.getParams().get("ime"));
        notificationType.setLastName(emailMessage.getParams().get("prezime"));
        if (emailMessage.getParams().get("password") != null) notificationType.setPassword(emailMessage.getParams().get("password"));
        emailMessageRepository.save(notificationType);

        // treba emailMessage dodati u h2 bazu za notifikacije

        emailService.sendEmail(emailMessage);
    }
}
