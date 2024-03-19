package com.tecknobit.nova.helpers.toImportFromCoreLibrary.users;

import java.util.List;
import java.util.Random;
import java.util.UUID;

// TODO: 17/03/2024 TRANSFER THE LANGUAGE PROPERTY ON SERVER ALSO   
public class User extends PublicUser {

    public static final String USER_KEY = "user";

    public static final String USER_IDENTIFIER_KEY = "userId";

    public static final String TOKEN_KEY = "token";

    public static final String PASSWORD_KEY = "password";

    public static final String USER_LANGUAGE_KEY = "language";

    public enum Role {

        Customer,

        Vendor

    }

    private final String token;

    private final String password;
    
    private final String language;

    private final Role role;

    // TODO: 06/03/2024 TO REMOVE
    public User(String name, String surname) {
        super(name, surname);
        this.token = UUID.randomUUID().toString().replace("-", "");
        this.password = UUID.randomUUID().toString().replace("-", "");
        this.language = "ITALIAN";
        this.role = List.of(Role.Vendor, Role.Customer).get(new Random().nextInt(2));
    }

    public User(String id, String name, String surname, String email, String profilePicUrl,
                String token, String password, String language, Role role) {
        super(id, name, surname, email, profilePicUrl);
        this.token = token;
        this.password = password;
        this.language = language;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public String getLanguage() {
        return language;
    }

    public Role getRole() {
        return role;
    }

    public boolean isVendor() {
        return role == Role.Vendor;
    }

    public boolean isCustomer() {
        return role == Role.Customer;
    }
    
}
