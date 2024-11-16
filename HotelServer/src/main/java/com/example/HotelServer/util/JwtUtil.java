package com.example.HotelServer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtUtil {

    // Méthode pour générer un token JWT avec des informations supplémentaires
    private String generateToken(Map<String, Object> extraClaims, UserDetails details) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Durée de validité: 24 heures
                .signWith(SignatureAlgorithm.HS256, getSigningKey()) // Utilisation de la clé secrète pour la signature
                .compact();
    }

    // Méthode pour générer un token JWT sans informations supplémentaires
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Méthode pour vérifier si le token est valide
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Méthode pour extraire toutes les informations (claims) du token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()) // Utilisation de la clé secrète pour vérifier la signature
                .parseClaimsJws(token)
                .getBody();
    }

    // Méthode pour extraire le nom d'utilisateur (subject) du token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Méthode pour extraire la date d'expiration du token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Méthode pour vérifier si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Méthode générique pour extraire une valeur de claim spécifique
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Méthode pour récupérer la clé secrète utilisée pour la signature du token
    private Key getSigningKey() {
        // Exemple de clé secrète, elle doit être définie dans un fichier sécurisé (par exemple, .env)
        String secretKey = "your-256-bit-secret"; // Il est conseillé de la stocker en toute sécurité
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
