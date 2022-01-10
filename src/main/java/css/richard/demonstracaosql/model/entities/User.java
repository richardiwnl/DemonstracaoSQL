package css.richard.demonstracaosql.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "register_date", nullable = false)
    private Date registerDate;

    @Column(name = "formatted_register_date", nullable = false)
    private String formattedRegisterDate;

    public User()
    {
    }

    public User(String firstName, String lastName, String email, String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Date getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate)
    {
        this.registerDate = registerDate;
    }

    public String getFormattedRegisterDate()
    {
        return formattedRegisterDate;
    }

    public void setFormattedRegisterDate(String formattedRegisterDate)
    {
        this.formattedRegisterDate = formattedRegisterDate;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
