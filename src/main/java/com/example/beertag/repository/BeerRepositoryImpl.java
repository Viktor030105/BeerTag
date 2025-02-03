package com.example.beertag.repository;


import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import com.example.beertag.models.FilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



import org.hibernate.query.Query;

import java.util.*;

@Repository
public class BeerRepositoryImpl implements BeerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Beer> getAll(FilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getName().ifPresent(value -> {
                filters.add("name like :name");
                params.put("name", String.format("%%%s%%", value));
            });

            filterOptions.getMinAbv().ifPresent(value -> {
                filters.add("abv >= :minAbv");
                params.put("minAbv", value);
            });

            filterOptions.getMaxAbv().ifPresent(value -> {
                filters.add("abv <= :maxAbv");
                params.put("maxAbv", value);
            });

            filterOptions.getStyleId().ifPresent(value -> {
                filters.add("style.id = :styleId");
                params.put("styleId", value);
            });

            StringBuilder queryString = new StringBuilder("from Beer");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Beer> query = session.createQuery(queryString.toString(), Beer.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public Beer getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Beer beer = session.get(Beer.class, id);
            if (beer == null) {
                throw new EntityNotFoundExeption("Beer", id);
            }

            return beer;
        }
    }

    @Override
    public Beer getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Beer> querry = session.createQuery("from Beer where name = :name", Beer.class);
            querry.setParameter("name", name);
            List<Beer> result = querry.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundExeption("Beer", "name", name);
            }

            return result.get(0);

        }
    }

    @Override
    public void createBeer(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateBeer(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteBeer(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(FilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = switch (filterOptions.getSortBy().get()) {
            case "name" -> "name";
            case "abv" -> "abv";
            case "style" -> "style.name";
            default -> "";
        };

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}