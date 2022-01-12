package css.richard.demonstracaosql.controller;

import css.richard.demonstracaosql.model.entities.User;
import css.richard.demonstracaosql.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Controller
@RequestMapping("/")
public class WebController
{

    @ModelAttribute("user")
    public User user()
    {
        return new User();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public void root(HttpServletResponse response) throws IOException
    {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication.isAuthenticated())
        {
            response.sendRedirect("/usuarios/1");
        }
        else
        {
            response.sendRedirect("/login");
        }
    }


    @GetMapping("/login")
    public String home(Model model)
    {

        return "home";
    }

    @GetMapping("/registrar")
    public String register(Model model)
    {

        return "register";
    }

    @PostMapping("/registrar")
    public String register(Model model, @ModelAttribute("user") User user,
                           HttpServletResponse response) throws Exception
    {

        if (userRepository.findByEmail(user.getEmail()) != null)
        {

            response.sendRedirect("/registrar?erro");
        }
        else
        {

            user.setFirstName(StringUtils.capitalize(user.getFirstName()));
            user.setLastName(StringUtils.capitalize(user.getLastName()));

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setRegisterDate(new Date());

            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

            time.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            date.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

            String formattedRegisterDate = String.format(
                    "%s às %s", date.format(user.getRegisterDate()),
                    time.format(user.getRegisterDate())
            );

            user.setFormattedRegisterDate(formattedRegisterDate);

            userRepository.save(user);

        }



        return "home";
    }

    @GetMapping("/usuarios/{pageNumber}")
    public String users(Model model, @PathVariable Integer pageNumber)
    {

        Authentication auth = SecurityContextHolder.getContext()
                        .getAuthentication();

        User user = userRepository.findByEmail(auth.getName());


        model.addAttribute("currentIndex", pageNumber);
        model.addAttribute("recordsAmount", userRepository.count());
        model.addAttribute("firstName", user.getFirstName());

        Pageable page = PageRequest.of(pageNumber - 1, 6);

        Iterable<User> users = userRepository.findAll(page);

        model.addAttribute("users", users);


        return "success";
    }

    @GetMapping("/apagar")
    public String delete()
    {

        return "deleteAccout";
    }
}
