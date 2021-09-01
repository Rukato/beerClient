package reactive.course.beer.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactive.course.beer.client.model.BeerDto;
import reactive.course.beer.client.model.BeerPageList;
import reactive.course.beer.config.WebClientConfig;
import reactor.core.publisher.Mono;

public class BeerClientImpTests {

    BeerClientImp beerClientImp;

    @BeforeEach
    void setUp() {
        beerClientImp = new BeerClientImp(new WebClientConfig().webClient());
    }
    

    @Test
    void testCreateBeer() {
        BeerDto beerDto = BeerDto.builder()
        .beerName("Medalla")
        .beerStyle("PILSNER")
        .upc("73737276823")
        .price(new BigDecimal("0.99"))
        .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClientImp.createBeer(beerDto);

        ResponseEntity<Void> response = responseEntityMono.block();
        assertEquals(201, response.getStatusCode().value());

    }

    @Test
    void testDeleteBeerByIdNotFound() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClientImp.deleteBeerById(UUID.randomUUID());

        assertThrows(WebClientResponseException.class, () -> {
            ResponseEntity<Void> response = responseEntityMono.block();
            assertEquals(204, response.getStatusCode().value());
        });
    }

    @Test
    void testGetAllBeers() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(null, null, null, null, null);

        BeerPageList pagedList = beerPageLstMono.block();

        assertNotNull(pagedList);
        assertNotNull(pagedList.getContent().size());
    }

    @Test
    void testGetAllBeersPageSize10() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(1, 10, null, null, null);

        BeerPageList pagedList = beerPageLstMono.block();

        assertNotNull(pagedList);
        assertEquals(10, pagedList.getContent().size());
    }

    @Test
    void testGetAllBeersNoRecords() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(10, 20, null, null, null);

        BeerPageList pagedList = beerPageLstMono.block();

        assertNotNull(pagedList);
        assertEquals(0, pagedList.getContent().size());
    }

    @Test
    void functionTestGetBeerById() throws InterruptedException {
        AtomicReference<String> beerName = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        beerClientImp.getAllBeers(null, null, null, null, null)
        .map(beerPagedList -> beerPagedList.getContent().get(0).getId())
        .map(beerId -> beerClientImp.getBeerById(beerId, false))
        .flatMap(mono -> mono)
        .subscribe(beerDto -> {
            beerName.set(beerDto.getBeerName());
            assertEquals("My new beer", beerDto.getBeerName());
            countDownLatch.countDown();
        });

        countDownLatch.await();
        assertEquals("My new beer", beerName.get());
    }



    @Test
    void testGetBeerByIdInInventory() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(null, null, null, null, null);

        Optional<BeerDto> firstInList = beerPageLstMono.block().get().findFirst();

        Mono<BeerDto> beerMono = beerClientImp.getBeerById(firstInList.get().getId(), true);

        assertEquals(firstInList.get(), beerMono.block());

    }

    @Test
    void testGetBeerByIdNotInInventory() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(null, null, null, null, null);

        Optional<BeerDto> firstInList = beerPageLstMono.block().get().findFirst();

        Mono<BeerDto> beerMono = beerClientImp.getBeerById(firstInList.get().getId(), false);

        assertEquals(firstInList.get(), beerMono.block());

    }

    @Test
    void testGetBeeryUpc() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(null, null, null, null, null);

        Optional<BeerDto> firstInList = beerPageLstMono.block().get().findFirst();

        Mono<BeerDto> beerMono = beerClientImp.getBeeryUpc(firstInList.get().getUpc());

        assertEquals(firstInList.get(), beerMono.block());
    }

    @Test
    void testUpdateBeerById() {
        Mono<BeerPageList> beerPageLstMono = beerClientImp.getAllBeers(null, null, null, null, null);

        Optional<BeerDto> firstInList = beerPageLstMono.block().get().findFirst();

        BeerDto beerMono = beerClientImp.getBeerById(firstInList.get().getId(), true).block();

        BeerDto newBeer = BeerDto.builder()
                            .beerName("My new beer")
                            .beerStyle(beerMono.getBeerStyle())
                            .price(beerMono.getPrice())
                            .upc(beerMono.getUpc())
                            .build();

        Mono<ResponseEntity<Void>> responseMono = beerClientImp.updateBeerById(beerMono.getId(), newBeer);
        ResponseEntity<Void> response = responseMono.block();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }
}
