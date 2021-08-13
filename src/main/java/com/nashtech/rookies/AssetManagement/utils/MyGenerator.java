package com.nashtech.rookies.AssetManagement.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class MyGenerator
        implements IdentifierGenerator, Configurable {

    private String prefix;
    private Integer length;
    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object obj)
            throws HibernateException {

        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Stream ids = session.createQuery(query).stream();

        Long max = ids.map(o -> String.valueOf(o).replace(prefix , ""))
                .mapToLong(s-> Long.parseLong(String.valueOf(s)))
                .max()
                .orElse(0L);

        return prefix  + String.format("%0"+length+"d", max+1);
    }

    @Override
    public void configure(Type type, Properties properties,
                          ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
        length = Integer.valueOf(properties.getProperty("length"));
    }
}