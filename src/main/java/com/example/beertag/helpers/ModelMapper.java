package com.example.beertag.helpers;

import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final BeerRepository beerRepository;

    @Autowired
    public ModelMapper(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
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
        beer.setName(beerDTO.getName());
        beer.setAbv(beerDTO.getAbv());
    }
}
