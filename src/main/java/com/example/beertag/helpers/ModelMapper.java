package com.example.beertag.helpers;

import com.example.beertag.models.*;
import com.example.beertag.repository.StyleRepository;
import com.example.beertag.repository.UserRepository;
import com.example.beertag.service.BeerService;
import com.example.beertag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final UserRepository userRepository;
    private final BeerService beerService;
    private final StyleRepository styleRepository;
    private final UserService userService;

    @Autowired
    public ModelMapper(UserRepository userRepository, BeerService beerService, StyleRepository styleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.beerService = beerService;
        this.styleRepository = styleRepository;
        this.userService = userService;
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

    public UserDTO toDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());

        return userDTO;
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

    public User fromDto(UserDTO userDTO, int id){
        User user = userService.getById(id);
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        return user;
    }
}
