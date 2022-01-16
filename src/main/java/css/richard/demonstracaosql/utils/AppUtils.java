package css.richard.demonstracaosql.utils;

import css.richard.demonstracaosql.model.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppUtils
{

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private static void setUserFormattedRegisterDate(User user)
    {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

        time.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        date.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        String formattedRegisterDate = String.format("%s às %s",
                date.format(user.getRegisterDate()),
                time.format(user.getRegisterDate()));

        user.setFormattedRegisterDate(formattedRegisterDate);
    }

    private static void formatUserName(User user, User newUser)
    {
        if (newUser == null)
        {
            user.setFirstName(StringUtils.capitalize(user.getFirstName()));
            user.setLastName(StringUtils.capitalize(user.getLastName()));
        } else
        {
            user.setFirstName(StringUtils.capitalize(newUser.getFirstName()));
            user.setLastName(StringUtils.capitalize(newUser.getLastName()));
        }
    }

    public static void buildUser(User user)
    {

        formatUserName(user, null);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRegisterDate(new Date());

        setUserFormattedRegisterDate(user);
    }

    public static void updateUser(User user, User newUser)
    {

        formatUserName(user, newUser);

        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

//        user.setRegisterDate(new Date());

        setUserFormattedRegisterDate(user);

        user.setEmail(newUser.getEmail());
    }
}
