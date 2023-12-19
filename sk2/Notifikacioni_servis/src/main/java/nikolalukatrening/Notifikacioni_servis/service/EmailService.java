package nikolalukatrening.Notifikacioni_servis.service;

import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;

public interface EmailService {

    public void sendEmail(EmailMessage emailMessage);

}
