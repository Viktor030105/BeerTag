package com.example.beertag.repository;

import com.example.beertag.exeptions.EntityNotFoundException;
import com.example.beertag.models.Style;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StyleRepositoryImpl implements StyleRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public StyleRepositoryImpl( SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Style> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Query<Style> query = session.createQuery("from Style", Style.class);
            return query.list();
        }
    }

    @Override
    public Style getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Style style = session.get(Style.class, id);

            if (style == null) {
                throw new EntityNotFoundException("Style", id);
            }

            return style;
        }
    }
}
