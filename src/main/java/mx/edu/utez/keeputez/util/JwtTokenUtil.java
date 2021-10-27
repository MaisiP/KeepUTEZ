package mx.edu.utez.keeputez.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import mx.edu.utez.keeputez.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 7200000; // 2 hours

    private final SecretKey key;

    public JwtTokenUtil(@Value("${key.secret}")
                                byte[] keySecret) {
        this.key = new SecretKeySpec(keySecret, "HmacSHA512");
        //toGenerate the secure byte array
        //this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        //get the byte array
        //this.key.getEncoded();
    }

    public User parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token).getBody();
            User user = new User();
            user.setUsername(claims.getSubject());
            user.setRole(claims.get("role", String.class));
            return user;
        } catch (JwtException e) {
            return null;
        }
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .compact();
    }

}

