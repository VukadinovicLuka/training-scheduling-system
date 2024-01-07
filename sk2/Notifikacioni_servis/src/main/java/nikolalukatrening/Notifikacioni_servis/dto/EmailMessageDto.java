package nikolalukatrening.Notifikacioni_servis.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class EmailMessageDto implements Serializable {
    private String to;
    private String subject;
    private String body;

    private String type;

    private Map<String, String> params;

    public EmailMessageDto(String to, String subject, String body, String type, Map<String, String> params) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.type = type;
        this.params = params;
    }
}
