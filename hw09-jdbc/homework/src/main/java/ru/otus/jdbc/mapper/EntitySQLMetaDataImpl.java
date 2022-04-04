package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("Select * from %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("Select * from %s where %s = ?",
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {

        String fieldNames = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        String questionMarks = entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> "?")
                .collect(Collectors.joining(","));
        return String.format("Insert into %s(%s) values(%s)",
                entityClassMetaData.getName(),
                fieldNames,
                questionMarks);
    }

    @Override
    public String getUpdateSql() {

        String updateFields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> String.format("%s = ?", field.getName()))
                .collect(Collectors.joining(","));
        return String.format("Update %s set %s where %s = ?",
                entityClassMetaData.getName(),
                updateFields,
                entityClassMetaData.getIdField().getName());
    }
}
