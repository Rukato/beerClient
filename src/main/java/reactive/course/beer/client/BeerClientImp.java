package reactive.course.beer.client;

import java.util.UUID;

import org.springframework.web.reactive.function.client.ClientResponse;

import reactive.course.beer.client.model.BeerDto;
import reactive.course.beer.client.model.BeerPageList;
import reactor.core.publisher.Mono;

public class BeerClientImp implements BeerClient{

    @Override
    public Mono<BeerPageList> getAllBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<ClientResponse> createBeer(BeerDto beerDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<ClientResponse> updateBeerById(BeerDto beerDto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<ClientResponse> deleteBeerById(UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<BeerDto> getBeeryUpc(String upc) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
