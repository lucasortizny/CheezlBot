package nyc.pikaboy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final CheezlbotConfiguration cheezlbotConfiguration;
    @Bean
    public RestTemplate cheezlApiRestTemplate(){
        return new RestTemplateBuilder()
                .rootUri(cheezlbotConfiguration.getApiUrl()).build();
    }
    @Bean
    public WebClient cheezlApiWebClient(ReactiveClientRegistrationRepository clientRegistrations,
                                        ReactiveOAuth2AuthorizedClientService authorizedClientService){
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, authorizedClientService);
        ServerOAuth2AuthorizedClientExchangeFilterFunction function =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        function.setDefaultClientRegistrationId("cheezlbot");
        return WebClient.builder()
                .baseUrl(cheezlbotConfiguration.getApiUrl())
                .filter(function)
                .build();
    }
}
