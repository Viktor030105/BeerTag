package com.example.beertag.helpers;

import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.models.Style;
import com.example.beertag.repository.BeerRepository;
import com.example.beertag.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final BeerRepository beerRepository;
    private final StyleRepository styleRepository;

    @Autowired
    public ModelMapper(BeerRepository beerRepository, StyleRepository styleRepository) {
        this.beerRepository = beerRepository;
        this.styleRepository = styleRepository;
    }

    public Beer fromDto(BeerDTO beerDTO){
        Beer beer = new Beer();
        dtoToObject(beerDTO, beer);
        return beer;
    }

    public Beer fromDto(BeerDTO beerDTO, int id){
        Beer beer = beerRepository.getById(id);
        dtoToObject(beerDTO, beer);
        return beer;
    }

    private void dtoToObject(BeerDTO beerDTO, Beer beer) {
        Style style = styleRepository.getById(beerDTO.getStyleId());
        beer.setName(beerDTO.getName());
        beer.setAbv(beerDTO.getAbv());
        beer.setStyle(style);
    }
}
