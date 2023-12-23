package nikolalukatrening.GUI2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponseDto {

    private String token;

    public TokenResponseDto() {
    }
    public TokenResponseDto(String token) {
        this.token = token;
    }
}