package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;


@Table("phone")
public class Phone {

    @Id
    private final Long id;

    @NonNull
    private final String number;

    @NonNull
    private final Long clientId;

    @PersistenceConstructor
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone(String number, Long clientId) {
        this(null, number, clientId);
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    @NonNull
    public Long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", client_id=" + clientId +
                '}';
    }
}