package com.backend.apigateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.File; 
import java.io.FileReader;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.KeyFactory; 

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges ->
                exchanges
                    .pathMatchers("/public/**").permitAll()
                    .pathMatchers("/secure/**").authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2
                    .jwt(jwt -> {})
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        String publicKeyFilePath = "C:\\workspace2\\BackEnd\\chave publica do JWT\\publickey.txt";
        FileReader fileReader = new FileReader(publicKeyFilePath);
        char[] publicKeyChars = new char[(int) new File(publicKeyFilePath).length()];
        fileReader.read(publicKeyChars);
        fileReader.close();

        String publicKeyPEM = new String(publicKeyChars);
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);

        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}


