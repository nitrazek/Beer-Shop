package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.models.utils.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
})
public class Account extends BaseModel<Long> {
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
    private AccountRole role;

    @ToString.Exclude
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Account() {}
    public Account(String login, String password) {
        this(null, login, password, null, AccountRole.CLIENT);
    }
    public Account(String login, String password, String email) {
        this(null, login, password, email, AccountRole.CLIENT);
    }
    public Account(String login, String password, String email, AccountRole role) {
        this(null, login, password, email, role);
    }
    public Account(Long id, String login, String password, String email, AccountRole role) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    public void addCartProduct(CartProduct cartProduct) {
        cartProducts.add(cartProduct);
        cartProduct.setAccount(this);
    }
}
