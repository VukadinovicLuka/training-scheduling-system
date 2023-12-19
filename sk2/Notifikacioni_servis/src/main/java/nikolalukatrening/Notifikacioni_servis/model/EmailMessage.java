package nikolalukatrening.Notifikacioni_servis.model;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class EmailMessage implements Serializable { // klasa koja služi za transfer podataka preko JMS.
//    private String to;
//    private String subject;
//    private String type; // Dodato za identifikaciju tipa notifikacije
//    private String recipientName;
//    private String activationLink;
      private String to;
      private String subject;
      private String body;

}
