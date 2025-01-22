package org.example.authservice.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.authservice.models.Prototype;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest implements Prototype<TokenRefreshRequest> {
    private String refreshToken;

    public TokenRefreshRequest(TokenRefreshRequest tokenRefreshRequest) {
        this.refreshToken = tokenRefreshRequest.getRefreshToken();
    }

    @Override
    public TokenRefreshRequest clone() {
        return new TokenRefreshRequest(this);
    }
}
