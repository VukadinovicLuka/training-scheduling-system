package nikolalukatrening.korisnicki_servis.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nikolalukatrening.korisnicki_servis.security.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${oauth.jwt.secret}") // jwtSecret je definisan u application.properties vrednost: secret_key
    private String jwtSecret;

    @Override
    public String generate(Claims claims) { // generise token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // HS512 je algoritam za enkripciju
                .compact(); // compact() je metoda koja generise token
    }

    @Override
    public Claims parseToken(String jwt) { // parsira token
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }
}
