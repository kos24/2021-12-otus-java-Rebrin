package ru.otus.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.Set;

@Table("client")
public class Client {

    @Id
    private final Long id;

    @NonNull
    private final String name;

    @NonNull
    private final Long addressId;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    @PersistenceConstructor
    public Client(Long id, String name, Long addressId, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.phones = phones;
    }

    public Client(String name, Long addressId, Set<Phone> phones) {
        this(null, name, addressId, phones);
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Long getAddressId() {
        return addressId;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressId=" + addressId +
                ", phones=" + phones +
                '}';
    }
}
