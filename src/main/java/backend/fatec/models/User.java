package backend.fatec.models;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID userId;
    private String username;
    private String password;
    private String email;
    private Boolean admin;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public UUID getUserId(){
        return userId;
    }
    public void setUserId(UUID userId){
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public Boolean getAdmin(){
        return admin;
    }

    public void setAdmin(Boolean isAdmin){
        this.admin = isAdmin == null ? false : isAdmin;
    }
}