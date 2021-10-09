package com.example.demo.authserver.services;

import com.example.demo.authserver.enums.Permission;
import com.example.demo.authserver.model.OAuth2UserImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final Logger logger = Logger.getLogger(CustomOAuth2UserService.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final UserDetailsService userDetailsService;
    private final RestOperations restOperations;
    private final Converter<OAuth2UserRequest, RequestEntity<?>> requestRequestEntityConverter = new OAuth2UserRequestEntityConverter();
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    public CustomOAuth2UserService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.restOperations = createRestTemplate();
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        return restTemplate;
    }

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ResponseEntity<Map<String, Object>> response;
        try{
            response = this.restOperations
                    .exchange(
                            Objects.requireNonNull(
                                    requestRequestEntityConverter.convert(userRequest)
                            ),
                            PARAMETERIZED_RESPONSE_TYPE);
        } catch (Exception exception) {
            throw new OAuth2AuthenticationException(exception.getMessage());
        }

        Map<String, Object> userAttributes = response.getBody();
        if (userAttributes == null)
            userAttributes = new HashMap<>();

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));

        for (String authority : userRequest.getAccessToken().getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }
        authorities.add(new SimpleGrantedAuthority(Permission.READ_ANIMAL.name()));

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        userAttributes.put(userNameAttributeName, "user");
        userAttributes.put(OAuth2UserImpl.ID_ATTR, 2);
        logger.info("UsernameAttributeName="+userNameAttributeName);
        logger.info("Authorities="+objectMapper.writeValueAsString(authorities));
        logger.info("UserAttributes="+objectMapper.writeValueAsString(userAttributes));
        return new OAuth2UserImpl(userNameAttributeName, userAttributes, authorities);
    }
}
