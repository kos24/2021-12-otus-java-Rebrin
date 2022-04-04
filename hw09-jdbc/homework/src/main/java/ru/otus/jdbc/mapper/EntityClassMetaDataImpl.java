package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplateException;
import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final Constructor<T> constructor;
    private final Field id;
    private final List<Field> allFields;
    private final List<Field> allFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.name = clazz.getSimpleName();
        this.constructor = getClassConstructor(clazz);
        this.id = getClassIdField(clazz);
        this.allFields = getAllClassFields(clazz);
        this.allFieldsWithoutId = getAllClassFieldsWithoutId(clazz);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.id;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.allFieldsWithoutId;
    }

    private Constructor<T> getClassConstructor(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Class<?>[] classes = Arrays.stream(fields)
                .map(Field::getType)
                .toArray(Class<?>[]::new);
        try {
            return clazz.getDeclaredConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(e);
        }
    }

    private Field getClassIdField(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new DataTemplateException("field with @Id is not defined"));
    }

    private List<Field> getAllClassFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).toList();
    }

    private List<Field> getAllClassFieldsWithoutId(Class<T> clazz) {
        return getAllClassFields(clazz).stream()
                .filter(field -> !field.equals(this.id))
                .toList();
    }
}
