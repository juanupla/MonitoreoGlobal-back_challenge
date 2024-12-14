package com.techforb.challenge.Configs;


import com.techforb.challenge.DTOs.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;



@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    @Value("${db.secretKey}")
    private static String SECRET_KEY;

    private static final String[] EXCLUDED_PATHS = {"/login", "/create-user"}; //endponits que voy a excluir del filtro


    public static TokenDTO generateToken(String firstName, String lastName, String email) {
        long expirationTime = 86400000; //  expira en milisegundos: 1 dia

        String token = Jwts.builder()
                .setSubject(email)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis() + expirationTime);
        LocalDateTime expiration = LocalDateTime.ofInstant(instant, ZoneId.of( "America/Argentina/Buenos_Aires"));

        return new TokenDTO(token, expiration);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  ruta actual de la solicitud
        String requestURI = request.getRequestURI();


        //ver si la ruta comienza con alguno de los prefijos excluidos
        boolean excludedPath = Arrays.stream(EXCLUDED_PATHS).anyMatch(requestURI::startsWith);

        if (excludedPath) {
           // no verificamos el token y continuamos con el flujo
            filterChain.doFilter(request, response);
            return;
        }

        // di no es el login o la creación de usuarios, entonces continuamos con la verificacion del token
        //obtenemos el token del encabezado "Authorization"
        String token = extractTokenFromHeader(request);


        boolean isTokenValid = isTokenValid(token,requestURI,request);

        if (isTokenValid) {

            filterChain.doFilter(request, response);
        } else {
            //si el token no es valido, respondemos con un error de autenticacion
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido, caducado o permiso no autorizado");
        }
    }


    private String extractTokenFromHeader(HttpServletRequest request) {
        //  valor del encabezado "Authorization"
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // extraemos el token eliminando el prefijo "Bearer "
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String token,String requestURI,HttpServletRequest request) {

        try {
            if (token != null) {
                // verifico la clave secreta
                Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);


                // para verificar si el token ha expirado
                Date expirationDate = claimsJws.getBody().getExpiration();
                Date now = new Date();
                if (expirationDate != null && expirationDate.before(now)) {
                    return false;
                }

                return true;
            }

        } catch (JwtException e) {
            //si ocurre una excepcion el token no es valido
            return false;
        }
        return false;
    }

}
