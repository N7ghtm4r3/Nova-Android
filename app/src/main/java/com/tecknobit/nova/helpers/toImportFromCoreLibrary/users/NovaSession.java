package com.tecknobit.nova.helpers.toImportFromCoreLibrary.users;

import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.users.User.*;

import java.util.ArrayList;
import java.util.List;

public class NovaSession {

    // TODO: 20/03/2024 USE THE CORRECT SESSIONS LIST
    public static final List<NovaSession> mySessions = new ArrayList<>();

    static {
        mySessions.addAll(List.of(
                new NovaSession(
                        "http://192.168.1.9",
                        Role.Vendor
                ),
                new NovaSession(
                        "http://192.4.56.1",
                        Role.Customer
                ),
                new NovaSession(
                        "http://192.4.56.23",
                        Role.Customer
                ),
                new NovaSession(
                        "http://192.4.56.13",
                        Role.Customer
                ),
                new NovaSession(
                        "http://192.4.56.33",
                        Role.Customer
                ),
                new NovaSession(
                        "http://192.4.56.2",
                        Role.Customer
                ))
        );
        //mySessions.clear();
    }

    private final String profilePicUrl;

    private final String email;

    private final String hostAddress;

    private final Role role;

    // TODO: 20/03/2024 TO REMOVE
    public NovaSession(String hostAddress, Role role) {
        this("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
                "prova@gmail.com",
                hostAddress,
                role);
    }

    public NovaSession(String profilePicUrl, String email, String hostAddress, Role role) {
        this.profilePicUrl = profilePicUrl;
        this.email = email;
        this.hostAddress = hostAddress;
        this.role = role;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Role getRole() {
        return role;
    }

}
