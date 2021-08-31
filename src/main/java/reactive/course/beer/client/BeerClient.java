package reactive.course.beer.client;

import java.util.UUID;

import org.springframework.web.reactive.function.client.ClientResponse;

import reactive.course.beer.client.model.BeerDto;
import reactive.course.beer.client.model.BeerPageList;
import reactor.core.publisher.Mono;

public interface BeerClient {

    public Mono<BeerPageList> getAllBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand);

    public Mono<ClientResponse> createBeer(BeerDto beerDto);

    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand);

    public Mono<ClientResponse> updateBeerById(BeerDto beerDto);

    public Mono<ClientResponse> deleteBeerById(UUID id);

    public Mono<BeerDto> getBeeryUpc(String upc);

}
