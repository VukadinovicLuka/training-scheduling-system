package nikolalukatrening.GUI2.service;

import nikolalukatrening.GUI2.customTable.CustomTable;
import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import org.springframework.web.client.RestTemplate;

import java.awt.*;

public interface AdminService {

    void odblokiraj(CustomTable clientsTable, RestTemplate activationServiceRestTemplate, Frame parentFrame);
    void zabrani(CustomTable clientsTable, RestTemplate activationServiceRestTemplate, Frame parentFrame);

    ClientProfileEditorDto getClientFromPanel(CustomTable clientsTable, int row);
}
