package nikolalukatrening.Notifikacioni_servis.service;

import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService { // servis koji će zapravo slati email poruke.

    public void sendEmail(EmailMessage emailMessage) {
        // Implementacija slanja e-maila koristeći JavaMailSender ili neki drugi mail client
    }
}
