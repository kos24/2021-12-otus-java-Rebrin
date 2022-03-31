package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return getEntity(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }

        });
    }

    private T getEntity(ResultSet rs) throws SQLException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        Object[] args = new Object[entityClassMetaData.getAllFields().size()];
        int i = 0;
        for (Field field : entityClassMetaData.getAllFields()) {

            if (field.getType().equals(Long.class)) {
                args[i] = rs.getLong(field.getName());
                i++;
            } else if (field.getType().equals(String.class)) {
                args[i] = rs.getString(field.getName());
                i++;
            } else {
                throw new SQLException();
            }
        }
        return entityClassMetaData.getConstructor().newInstance(args);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    clientList.add(getEntity(rs));
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }

        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> params = entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
                try {
                    field.setAccessible(true);
                    return field.get(client);
                } catch (IllegalAccessException e) {
                    throw new DataTemplateException(e);
                }
            }).toList();
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> params = entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
                try {
                    field.setAccessible(true);
                    return field.get(client);
                } catch (IllegalAccessException e) {
                    throw new DataTemplateException(e);
                }
            }).toList();
            List<Object> paramsWithId = new ArrayList<>(params);
            entityClassMetaData.getIdField().setAccessible(true);
            paramsWithId.add(entityClassMetaData.getIdField().get(client));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    paramsWithId);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
