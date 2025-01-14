package com.example.beertag.repository;


import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;



import org.hibernate.query.Query;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BeerRepositoryImpl implements BeerRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Beer> getAllBeers() {
        try (Session session = sessionFactory.openSession()){
            Query<Beer> querry = session.createQuery("from Beer", Beer.class);
            return querry.list();
        }
    }



    @Override
    public Beer getById(int id){
        try (Session session = sessionFactory.openSession()) {
            Beer beer = session.get(Beer.class, id);
            if (beer == null) {
                throw new EntityNotFoundExeption("Beer", id);
            }

            return beer;
        }
    }

    @Override
    public Beer getByName(String name){
        try (Session session = sessionFactory.openSession()){
            Query<Beer> querry = session.createQuery("from Beer where name = :name", Beer.class);
            querry.setParameter("name", name);
            List<Beer> result = querry.list();

            if (result.size() == 0) {
                throw new EntityNotFoundExeption("Beer", "name", name);
            }

            return result.get(0);

        }
    }

    @Override
    public void createBeer(Beer beer){
        try (Session session = sessionFactory.openSession()){
            session.persist(beer);
        }
    }

    @Override
    public void updateBeer(Beer beer){
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteBeer(int id){
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    private static List<Beer> filterByName(List<Beer> beers, String name) {
        if (name != null && !name.isEmpty()) {
            beers = beers.stream()
                    .filter(beer -> containsIgnoreCase(beer.getName(), name))
                    .collect(Collectors.toList());
        }
        return beers;
    }

    private static List<Beer> filterByAbv(List<Beer> beers, Double minAbv, Double maxAbv) {
        if (minAbv != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getAbv() >= minAbv)
                    .collect(Collectors.toList());
        }

        if (maxAbv != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getAbv() <= maxAbv)
                    .collect(Collectors.toList());
        }

        return beers;
    }

    private static List<Beer> filterByStyle(List<Beer> beers, Integer styleId) {
        if (styleId != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getStyle().getId() == styleId)
                    .collect(Collectors.toList());
        }
        return beers;
    }

    private static List<Beer> sortBy(List<Beer> beers, String sortBy) {
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    beers.sort(Comparator.comparing(Beer::getName));
                    break;
                case "abv":
                    beers.sort(Comparator.comparing(Beer::getAbv));
                case "style":
                    beers.sort(Comparator.comparing(beer -> beer.getStyle().getName()));
                    break;
            }
        }
        return beers;
    }

    private static List<Beer> order(List<Beer> beers, String order) {
        if (order != null && !order.isEmpty()) {
            if (order.equals("desc")) {
                Collections.reverse(beers);
            }
        }
        return beers;
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
