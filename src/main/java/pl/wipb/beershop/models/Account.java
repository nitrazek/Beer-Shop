package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import pl.wipb.beershop.models.utils.AccountRole;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();

    public Account() {}

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Account(String login, String password, String email, AccountRole role) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
