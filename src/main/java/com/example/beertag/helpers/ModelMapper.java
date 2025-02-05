package com.example.beertag.helpers;

import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.models.Style;
import com.example.beertag.models.User;
import com.example.beertag.repository.StyleRepository;
import com.example.beertag.service.BeerService;
import com.example.beertag.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final BeerService beerService;
    private final StyleRepository styleRepository;

    @Autowired
    public ModelMapper(BeerService beerService, StyleRepository styleRepository) {
        this.beerService = beerService;
        this.styleRepository = styleRepository;
    }

    public Beer fromDto(BeerDTO beerDTO, User user){
        Beer beer = new Beer();
        dtoToObject(beerDTO, beer);
        beer.setCreatedBy(user);
        return beer;
    }

    public Beer fromDto(BeerDTO beerDTO, int id){
        Beer beer = fromDto(beerDTO, null);
        dtoToObject(beerDTO, beer);
        return beer;
    }

    public BeerDTO toDto(Beer beer){
        BeerDTO beerDTO = new BeerDTO();
        beerDTO.setName(beer.getName());
        beerDTO.setAbv(beer.getAbv());
        beerDTO.setStyleId(beer.getStyle().getId());
        return beerDTO;
    }

    private void dtoToObject(BeerDTO beerDTO, Beer beer){
        Style style = styleRepository.getById(beerDTO.getStyleId());
        beer.setName(beerDTO.getName());
        beer.setAbv(beerDTO.getAbv());
        beer.setStyle(style);
    }
}
