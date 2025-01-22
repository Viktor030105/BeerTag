package com.example.beertag.controllers;

import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.helpers.ModelMapper;
import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.models.FilterOptions;
import com.example.beertag.models.User;
import com.example.beertag.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeerRestControllerTest {

    @Mock
    private BeerService service;

    @Mock
    private AuthenticationHelper authenticationHelper;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BeerRestController controller;

    private Beer testBeer;
    private BeerDTO testBeerDTO;
    private User normalUser;
    private User normalUser2;
    private User adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testBeerDTO = new BeerDTO();
        testBeerDTO.setName("Test Beer");

        testBeer = new Beer();
        testBeer.setId(1);
        testBeer.setName("Test Beer");

        adminUser = new User();
        adminUser.setAdmin(true);

        normalUser = new User();
        normalUser.setUsername("Test1");
        normalUser.setId(1);
        normalUser.setAdmin(false);

        normalUser2 = new User();
        normalUser2.setUsername("Test2");
        normalUser2.setId(2);
        normalUser2.setAdmin(false);
    }

    @Test
    void getAllBeers_ShouldReturnListOfBeers() {
        List<Beer> beers = Collections.singletonList(testBeer);

        when(service.getAll(any(FilterOptions.class)))
                .thenReturn(beers);

        List<Beer> result = controller.getAllBeers(null, null, null, null, null, null);

        assertEquals(beers, result);
        verify(service, times(1))
                .getAll(any(FilterOptions.class));
    }

    @Test
    void getBeerById_ShouldReturnBeer() {
        when(service.getById(1))
                .thenReturn(testBeer);

        Beer result = controller.getBeerById(1);

        assertEquals(testBeer, result);
        verify(service, times(1)).getById(1);
    }

    @Test
    void getBeerById_ShouldThrowResponseStatusException_WhenNotFound() {
        when(service.getById(1))
                .thenThrow(new EntityNotFoundExeption("Beer", "id", "1"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.getBeerById(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createBeer_ShouldCreateBeer() {
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers))
                .thenReturn(normalUser2);

        when(modelMapper.fromDto(testBeerDTO))
                .thenReturn(testBeer);

        Beer result = controller.createBeer(headers, testBeerDTO);

        verify(service, times(1)).createBeer(testBeer, normalUser2);
        assertEquals(testBeer, result);
    }

    @Test
    void createBeer_ShouldThrowResponseStatusException_WhenDuplicateExists() {
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers))
                .thenReturn(normalUser2);

        when(modelMapper.fromDto(testBeerDTO))
                .thenReturn(testBeer);

        doThrow(new DublicateEntityExeption("Beer", "name", testBeer.getName()))
                .when(service).createBeer(testBeer, normalUser2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.createBeer(headers, testBeerDTO));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void updateBeer_ShouldUpdateBeer() {
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers))
                .thenReturn(normalUser2);

        when(modelMapper.fromDto(testBeerDTO, 1))
                .thenReturn(testBeer);

        Beer result = controller.updateBeer(headers, 1, testBeerDTO);

        verify(service, times(1)).updateBeer(testBeer, normalUser2);
        assertEquals(testBeer, result);
    }

    @Test
    void updateBeer_ShouldThrowResponseStatusException_WhenUnauthorized() {
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers))
                .thenReturn(normalUser2);

        when(modelMapper.fromDto(testBeerDTO, 1))
                .thenReturn(testBeer);

        doThrow(new UnauthorizedOperationException("Unauthorized"))
                .when(service).updateBeer(testBeer, normalUser2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.updateBeer(headers, 1, testBeerDTO));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void deleteBeer_ShouldDeleteBeer() {
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers))
                .thenReturn(normalUser2);

        controller.deleteBeer(headers, 1);

        verify(service, times(1)).deleteBeer(1, normalUser2);
    }
}
