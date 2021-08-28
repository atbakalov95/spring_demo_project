package com.example.demo.authserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth-server")
public class ServerProperties {
    private OAuth2Settings oAuth2;

    public OAuth2Settings getoAuth2() {
        return oAuth2;
    }

    public void setoAuth2(OAuth2Settings oAuth2Settings) {
        this.oAuth2 = oAuth2Settings;
    }

    public static class OAuth2Settings {
        private String clientId;
        private String clientSecret;
        private String redirectUrl;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }
    }
}
