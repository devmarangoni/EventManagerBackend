package backend.fatec.models;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID customerId;
    private String name; /* Nome do cliente REQUIRED */
    private String phone; /* Telefone do cliente */
    private String mobile; /* Celular do cliente REQUIRED */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_user_id")
    private User user;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public UUID getCustomerId(){
        return customerId;
    }
    public void setCustomerId(UUID customerId){
        this.customerId = customerId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getMobile(){
        return mobile;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
}