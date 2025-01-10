package com.example.beertag.helpers;

import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeerMapper {

    private final StyleService styleService;

    @Autowired
    public BeerMapper(StyleService styleService) {
        this.styleService = styleService;
    }

    public Beer fromDto(int id, BeerDTO dto) {
        Beer beer = fromDto(dto);
        beer.setId(id);
        return beer;
    }

    public Beer fromDto(BeerDTO dto) {
        Beer beer = new Beer();
        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setStyle(styleService.getById(dto.getStyleId()));
        return beer;
    }
}
