package nikolalukatrening.korisnicki_servis.service;

import nikolalukatrening.korisnicki_servis.dto.*;

public interface ClientService {

    ClientDto add(ClientCreateDto clientCreateDto);

    ClientDto update (ClientUpdateDto clientUpdateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    ClaimResponseDto getClaim(TokenResponseDto tokenResponseDto);
}
