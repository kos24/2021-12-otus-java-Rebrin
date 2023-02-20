package ru.otus.domain;


import org.springframework.lang.NonNull;


public class Phone {

    private Long id;

    private String number;

    private Long clientId;

    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone() {
    }

    public Phone(String number, Long clientId) {
        this(null, number, clientId);
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

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