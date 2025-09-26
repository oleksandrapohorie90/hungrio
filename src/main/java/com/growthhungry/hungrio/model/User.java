package com.growthhungry.hungrio.model;


import jakarta.persistence.*;

/**
 * Why everything is coming from jakarta?
 * What is JPA in simple words
 */
//JPA = Java Persistence API.
//It’s just a specification (a set of rules/interfaces) in Java that tells how to store Java objects in a database and read them back.JPA is like a translator between your Java classes and the database tables.
//@Entity - marks the class as a JPA entity.
@Entity
//@Table(name = "users") - optional but avoids issues if user is a reserved word in some DBs.
@Table(name = "users")
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) - makes id auto-increment in most SQL DBs.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    // JPA requires a no-args constructor
    public User() {

    }

    public User(long id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
