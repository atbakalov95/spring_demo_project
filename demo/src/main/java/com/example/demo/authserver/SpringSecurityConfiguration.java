//package com.example.demo.authserver;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.UUID;
//
//@EnableWebSecurity
//public class SpringSecurityConfiguration {
//    private final ServerProperties serverProperties;
//
//    public SpringSecurityConfiguration(
//            ServerProperties serverProperties
//    ) {
//        this.serverProperties = serverProperties;
//    }
//
//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests.anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }
//
//
//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId(serverProperties.getoAuth2().getClientId())
//                .clientSecret(serverProperties.getoAuth2().getClientSecret())
//                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri(serverProperties.getoAuth2().getRedirectUrl())
//                .scope(OidcScopes.OPENID)
//                .build();
//
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }
//}
