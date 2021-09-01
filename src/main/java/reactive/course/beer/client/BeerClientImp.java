package reactive.course.beer.client;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactive.course.beer.client.model.BeerDto;
import reactive.course.beer.client.model.BeerPageList;
import reactive.course.beer.config.WebClientProperties;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerClientImp implements BeerClient{

    private final WebClient webClient;

    @Override
    public Mono<BeerPageList> getAllBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        return webClient.get()
        .uri(uriBuilder -> 
        uriBuilder.path(WebClientProperties.BASE_BEER_URL)
        .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
        .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
        .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
        .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
        .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnhand))  
        .build())
        .retrieve()
        .bodyToMono(BeerPageList.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> createBeer(BeerDto beerDto) {
        return webClient.post().uri(uriBuilder -> 
        uriBuilder.path(WebClientProperties.BASE_BEER_URL).build())
        .body(BodyInserters.fromValue(beerDto))
        .retrieve()
        .toBodilessEntity();

    }

    @Override
    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand) {
        return webClient.get()
        .uri(uriBuilder -> 
            uriBuilder.path(WebClientProperties.BASE_BEER_URL + "/{id}")
            .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
            .build(id)
        ).retrieve()
        .bodyToMono(BeerDto.class);

    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeerById(UUID id, BeerDto beerDto) {
        return webClient.put()
        .uri(uriBuilder -> 
        uriBuilder.path(WebClientProperties.BASE_BEER_URL + "/{id}").build(id))
        .body(BodyInserters.fromValue(beerDto))
        .retrieve()
        .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeerById(UUID id) {
        return webClient.delete()
        .uri(uriBuilder -> 
        uriBuilder.path(WebClientProperties.BASE_BEER_URL + "/{id}").build(id))
        .retrieve()
        .toBodilessEntity();
    }

    @Override
    public Mono<BeerDto> getBeeryUpc(String upc) {
        return webClient.get()
        .uri(uriBuilder -> 
            uriBuilder.path(WebClientProperties.BASE_BEER_UPC_URL + "/{upc}")
            .build(upc)
        ).retrieve()
        .bodyToMono(BeerDto.class);

    }
    
}
