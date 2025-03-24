package org.whiletrue.ordermanagementsystem.Services.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Service responsible for generating and validating JSON Web Tokens (JWT).
 * <p>
 * Used in the authentication system to issue tokens for authenticated users
 * and verify their identity in subsequent requests.
 */
@Service
public class JwtService {

    /**
     * Secret key used for signing and verifying JWT tokens.
     * It should be stored securely and configured in application properties.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Generates a JWT token for the given authenticated user.
     *
     * @param userDetails The authenticated user's details.
     * @return A signed JWT token valid for 3 hours.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusHours(3)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token The JWT token.
     * @return The username encoded in the token.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired.
     */
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * Validates the token against the provided user's details.
     * Ensures that the username in the token matches the expected user.
     *
     * @param token        The JWT token to validate.
     * @param userDetails  The expected user's details.
     * @return {@code true} if the token is valid and matches the user, {@code false} otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }
}
