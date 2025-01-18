package com.example.beertag.helpers;

import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.service.BeerService;
import com.example.beertag.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final BeerService beerService;
    private final StyleService styleService;

    @Autowired
    public ModelMapper(BeerService beerService, StyleService styleService) {
        this.beerService = beerService;
        this.styleService = styleService;
    }

    public Beer fromDto(BeerDTO beerDTO){
        Beer beer = new Beer();
        beer.setName(beerDTO.getName());
        beer.setAbv(beerDTO.getAbv());
        beer.setStyle(styleService.getById(beerDTO.getStyleId()));
        return beer;
    }

    public Beer fromDto(BeerDTO beerDTO, int id){
        Beer beer = fromDto(beerDTO);
        beer.setId(id);
        Beer repositoryBeer = beerService.getById(id);
        beer.setCreatedBy(repositoryBeer.getCreatedBy());
        return beer;
    }
}
