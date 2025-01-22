package com.example.beertag.service;

import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.models.Beer;
import com.example.beertag.models.FilterOptions;
import com.example.beertag.models.User;
import com.example.beertag.repository.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeerServiceImplTest {

    @Mock
    private BeerRepository repository;

    @InjectMocks
    private BeerServiceImpl service;

    private Beer testBeer;
    private User adminUser;
    private User normalUser;
    private User normalUser2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void getAll_ShouldReturnListOfBeers() {
        FilterOptions filterOptions = new FilterOptions();
        List<Beer> beers = Arrays.asList(testBeer);

        when(repository.getAll(filterOptions))
                .thenReturn(beers);

        List<Beer> result = service.getAll(filterOptions);

        assertEquals(beers, result);
        verify(repository, times(1))
                .getAll(filterOptions);
    }

    @Test
    void getById_ShouldReturnBeer() {
        when(repository.getById(1))
                .thenReturn(testBeer);

        Beer result = service.getById(1);

        assertEquals(testBeer, result);
        verify(repository, times(1))
                .getById(1);
    }

    @Test
    void createBeer_ShouldThrowExceptionWhenDuplicateExists() {
        when(repository.getByName(testBeer.getName()))
                .thenReturn(testBeer);

        assertThrows(EntityNotFoundExeption.class, () -> service.createBeer(testBeer, normalUser));
    }

    @Test
    void createBeer_ShouldCreateBeer() {
        when(repository.getByName(testBeer.getName()))
                .thenThrow(EntityNotFoundExeption.class);

        service.createBeer(testBeer, normalUser);

        verify(repository, times(1))
                .createBeer(testBeer);
        assertEquals(normalUser, testBeer.getCreatedBy());
    }

    @Test
    void updateBeer_ShouldThrowUnauthorizedExceptionWhenUserIsNotAdminOrCreator() {
        testBeer.setCreatedBy(normalUser2);

        Mockito.when(repository.getById(1))
                .thenReturn(testBeer);

        assertThrows(UnauthorizedOperationException.class, () -> service.updateBeer(testBeer, normalUser));
    }

    @Test
    void updateBeer_ShouldUpdateBeer() {
        testBeer.setCreatedBy(normalUser);
        when(repository.getById(1))
                .thenReturn(testBeer);
        when(repository.getByName(testBeer.getName()))
                .thenThrow(EntityNotFoundExeption.class);

        service.updateBeer(testBeer, normalUser);

        verify(repository, times(1)).updateBeer(testBeer);
    }

    @Test
    void deleteBeer_ShouldThrowUnauthorizedExceptionWhenUserIsNotAdminOrCreator() {
        testBeer.setCreatedBy(normalUser2);

        when(repository.getById(1))
                .thenReturn(testBeer);

        assertThrows(UnauthorizedOperationException.class, () -> service.deleteBeer(1, normalUser));
    }

    @Test
    void deleteBeer_ShouldDeleteBeer() {
        testBeer.setCreatedBy(normalUser);
        when(repository.getById(1))
                .thenReturn(testBeer);

        service.deleteBeer(1, normalUser);

        verify(repository, times(1)).deleteBeer(1);
    }
}
