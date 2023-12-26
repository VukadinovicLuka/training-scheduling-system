package nikolalukatrening.Zakazivanje_servis.service.impl;


import nikolalukatrening.Zakazivanje_servis.service.RestTemplateService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestTemplateServiceImpl implements RestTemplateService {


    @Override
    public RestTemplate setupRestTemplate(RestTemplate restTempl) {
        restTempl = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        restTempl.setMessageConverters(messageConverters);
        return restTempl;
    }
}
