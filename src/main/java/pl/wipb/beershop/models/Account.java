package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import pl.wipb.beershop.models.utils.AccountRole;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSeq")
    @SequenceGenerator(name = "accountSeq", sequenceName = "ACCOUNT_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role = AccountRole.CLIENT;

    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @ToString.Exclude
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
