package com.example.demo.defaultapp.configs;

import com.example.demo.defaultapp.properties.LocalAuth2ServerProperties;
import com.example.demo.defaultapp.security.JwtConfigurer;
import com.example.demo.defaultapp.enums.Permission;
import com.example.demo.defaultapp.properties.BitBucketOAuth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConfigurer jwtConfigurer;
    private final BitBucketOAuth2Properties bitBucketOAuth2Properties;
    private final LocalAuth2ServerProperties localAuth2ServerProperties;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    public SecurityConfig(JwtConfigurer jwtConfigurer, BitBucketOAuth2Properties bitBucketOAuth2Properties, LocalAuth2ServerProperties localAuth2ServerProperties, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) {
        this.jwtConfigurer = jwtConfigurer;
        this.bitBucketOAuth2Properties = bitBucketOAuth2Properties;
        this.localAuth2ServerProperties = localAuth2ServerProperties;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // disable cross script forgery filter
                .apply(jwtConfigurer) // attach jwt custom authorization filter
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests(customizer -> customizer
                        .antMatchers("/hello").permitAll()
                        .antMatchers("/auth/jwt/login").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/deleteAnimal/**").hasAuthority(Permission.DELETE_ANIMAL.name())
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oAuth2UserService))// attach oauth2 login filter
                );
    }

    @Bean
    public ClientRegistration bitbucketClientRegistration() {
        return ClientRegistration
                .withRegistrationId("bitbucket")
                .clientId(bitBucketOAuth2Properties.getKey())
                .clientSecret(bitBucketOAuth2Properties.getSecret())
                .userNameAttributeName("username")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userInfoUri("https://api.bitbucket.org/2.0/user")
                .tokenUri("https://bitbucket.org/site/oauth2/access_token")
                .authorizationUri("https://bitbucket.org/site/oauth2/authorize")
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .build();
    }

    @Bean
    public ClientRegistration localoauth2ServerClientRegistration() {
        return ClientRegistration
                .withRegistrationId("customClient")
                .clientId(localAuth2ServerProperties.getClientId())
                .clientSecret(localAuth2ServerProperties.getClientSecret())
                .clientName("Auth Sever")
                .scope("user_info")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri("http://my-oauth2-server:9000/oauth/token")
                .authorizationUri("http://my-oauth2-server:9000/oauth/authorize")
                .userInfoUri("http://my-oauth2-server:9000/userInfo")
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .userNameAttributeName("name")
                .build();
    }

    @Bean
    @Autowired
    public ClientRegistrationRepository clientRegistrationRepository(List<ClientRegistration> registrations){
        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
