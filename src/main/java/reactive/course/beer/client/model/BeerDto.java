package reactive.course.beer.client.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDto {
    
    @Null
    private UUID id;
    
    @NotBlank
    private String beerName;

    @NotBlank
    private String beerStyle;
    
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private Double version;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
}
