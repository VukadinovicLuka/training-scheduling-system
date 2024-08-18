package nikolalukatrening.GUI2.service.impl;

import nikolalukatrening.GUI2.customTable.CustomTable;
import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import nikolalukatrening.GUI2.dto.UserDto;
import nikolalukatrening.GUI2.service.AdminService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminServiceImpl implements AdminService {


    @Override
    public void odblokiraj(CustomTable clientsTable, RestTemplate activationServiceRestTemplate, Frame parentFrame) {
        String username = JOptionPane.showInputDialog(parentFrame, "Unesite username:");
        if (username == null) {
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Morate uneti username!");
            return;
        }


        // izvlacenje reda iz tabele na osnovu username-a
        int row = -1;
        for (int i = 0; i < clientsTable.getRowCount(); i++) {
            if (clientsTable.getValueAt(i, 1).equals(username)) {
                row = i;
                break;
            }
        }
        if (row == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Ne postoji korisnik sa unetim username-om!");
            return;
        }

        if (clientsTable.getValueAt(row, 8).equals(true)) {
            JOptionPane.showMessageDialog(parentFrame, "Korisnik je vec aktivan!");
            return;
        }

        // Kreirajte header-e za zahtev
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<ClientProfileEditorDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8080/api/client/activationUpdate")).headers(headers).body(getClientFromPanel(clientsTable, row));


        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity = activationServiceRestTemplate.exchange(requestEntity, ClientProfileEditorDto.class);
        Boolean isActivated1 = responseEntity.getBody().getIsActivated(); // ovo je true

        // hocu da isActivated u tabeli bude vrednost isActivated1
        clientsTable.setValueAt(isActivated1, row, 8);
        // refreshujem tabelu
        clientsTable.repaint();
    }

    @Override
    public void zabrani(CustomTable clientsTable, RestTemplate activationServiceRestTemplate, Frame parentFrame) {
        String username = JOptionPane.showInputDialog(parentFrame, "Unesite username:");
        if (username == null) {
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Morate uneti username!");
            return;
        }

        // izvlacenje reda iz tabele na osnovu username-a
        int row = -1;
        for (int i = 0; i < clientsTable.getRowCount(); i++) {
            if (clientsTable.getValueAt(i, 1).equals(username)) {
                row = i;
                break;
            }
        }
        if (row == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Ne postoji korisnik sa unetim username-om!");
            return;
        }

        if (clientsTable.getValueAt(row, 8).equals(false)) {
            JOptionPane.showMessageDialog(parentFrame, "Korisnik je vec blokiran!");
            return;
        }

        // Kreirajte header-e za zahtev
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<ClientProfileEditorDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8080/api/client/activationUpdate")).headers(headers).body(getClientFromPanel(clientsTable, row));


        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity = activationServiceRestTemplate.exchange(requestEntity, ClientProfileEditorDto.class);
        Boolean isActivated1 = responseEntity.getBody().getIsActivated(); // ovo je false

        // hocu da isActivated u tabeli bude vrednost isActivated1
        clientsTable.setValueAt(isActivated1, row, 8);
        // refreshujem tabelu
        clientsTable.repaint();

    }

    @Override
    public ClientProfileEditorDto getClientFromPanel(CustomTable clientsTable, int row) {
        // izvuci mi sve podatke iz tabele na osnovu reda
        String id = clientsTable.getValueAt(row, 0).toString();
        String username1 = clientsTable.getValueAt(row, 1).toString();
        String email = clientsTable.getValueAt(row, 2).toString();
        String firstName = clientsTable.getValueAt(row, 3).toString();
        String lastName = clientsTable.getValueAt(row, 4).toString();
        String dateOfBirth = clientsTable.getValueAt(row, 5).toString();
        String reservedTraining = clientsTable.getValueAt(row, 6).toString();
        String cardNumber = clientsTable.getValueAt(row, 7).toString();
        String isActivated = clientsTable.getValueAt(row, 8).toString();
        String password = clientsTable.getValueAt(row, 9).toString();
        String activationToken = clientsTable.getValueAt(row, 10).toString();
        String role = clientsTable.getValueAt(row, 11).toString();

        UserDto user = new UserDto();
        user.setUsername(username1);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setPassword(password);
        user.setRole(role);


        // napravi objekat od tih podataka
        ClientProfileEditorDto client = new ClientProfileEditorDto();
        client.setId(Long.parseLong(id));
        client.setReservedTraining(Integer.valueOf(reservedTraining));
        client.setCardNumber(Integer.valueOf(cardNumber));
        client.setIsActivated(Boolean.valueOf(isActivated));
        client.setActivationToken(activationToken);
        client.setUser(user);
        return client;
    }


}
