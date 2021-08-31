package reactive.course.beer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
        .baseUrl(WebClientProperties.BASE_URL)
        .clientConnector(new ReactorClientHttpConnector(
            HttpClient.create().wiretap(
                "reacto.netty.client.HttpClient",
                LogLevel.DEBUG, 
                AdvancedByteBufFormat.TEXTUAL)))
        .build();
    }

}
