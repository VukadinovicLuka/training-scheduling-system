package nikolalukatrening.GUI2.service;

import org.springframework.web.client.RestTemplate;

public interface RestTemplateService {

    RestTemplate setupRestTemplate(RestTemplate restTemplate);
}
