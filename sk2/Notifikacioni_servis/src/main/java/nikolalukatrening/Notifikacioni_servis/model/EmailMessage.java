package nikolalukatrening.Notifikacioni_servis.model;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class EmailMessage implements Serializable { // klasa koja služi za transfer podataka preko JMS.
      private String to;
      private String subject;
      private String body;
      private String type;
      private Map<String, String> params;


      @Override
      public String toString() {
            return "EmailMessage{" +
                    "to='" + to + '\'' +
                    ", subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", NotificationType='" + type + '\'' +
                    ", params=" + params +
                    '}';
      }
}
