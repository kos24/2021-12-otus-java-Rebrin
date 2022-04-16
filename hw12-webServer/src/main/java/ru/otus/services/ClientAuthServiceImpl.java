package ru.otus.services;


import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final Properties properties = new Properties();

    @Override
    public boolean authenticate(String login, String password) {
        loadProperties();
        String userLogin = properties.getProperty("login");
        String userPassword = properties.getProperty("password");
        return Objects.equals(login, userLogin) && Objects.equals(password, userPassword);
    }

    private Properties loadProperties() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (inputStream == null) {
                throw new IOException();
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("can't read app.properties file");
        }
        return properties;
    }

}
