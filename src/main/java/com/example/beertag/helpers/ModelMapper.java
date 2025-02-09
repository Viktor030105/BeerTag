package com.example.beertag.helpers;

import com.example.beertag.models.*;
import com.example.beertag.repository.StyleRepository;
import com.example.beertag.repository.UserRepository;
import com.example.beertag.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final UserRepository userRepository;
    private final BeerService beerService;
    private final StyleRepository styleRepository;

    @Autowired
    public ModelMapper(UserRepository userRepository, BeerService beerService, StyleRepository styleRepository) {
        this.userRepository = userRepository;
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
        Beer beer = beerService.getById(id);
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

    public User fromDto(RegisterDTO registerDTO){
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());

        return user;
    }
}
