package css.richard.demonstracaosql.controller;

import css.richard.demonstracaosql.model.details.CustomUserDetails;
import css.richard.demonstracaosql.model.entities.User;
import css.richard.demonstracaosql.model.repositories.UserRepository;
import css.richard.demonstracaosql.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class WebController
{

    @Autowired
    public SessionRegistry sessionRegistry;

    @ModelAttribute("newUser")
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
        Authentication authentication = AppUtils.getAuthentication();

        if (authentication.isAuthenticated())
        {
            response.sendRedirect("/usuarios/1");
        } else
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
    public String register(@ModelAttribute("newUser") User user,
                           HttpServletResponse response) throws Exception
    {

        if (userRepository.findByEmail(user.getEmail()) != null)
        {

            response.sendRedirect("/registrar?erro");
        } else
        {

            AppUtils.buildUser(user);

            userRepository.save(user);
        }


        return "home";
    }

    @GetMapping("/usuarios/{pageNumber}")
    public String users(Model model, @PathVariable Integer pageNumber)
    {

        List<User> users = userRepository.findAll(Sort.by("registerDate").descending());

        model.addAttribute("users",
                users.stream()
                        .skip((long) (pageNumber - 1) * 6)
                        .limit(pageNumber * 6)
                        .collect(Collectors.toList()));

        Authentication authentication = AppUtils.getAuthentication();

        Optional<User> userOptional = userRepository.findById(Long.parseLong(authentication.getName()));

        User user = userOptional.orElseThrow(() -> new NullPointerException("User is null"));

        model.addAttribute("currentIndex", pageNumber);
        model.addAttribute("recordsAmount", userRepository.count());
        model.addAttribute("firstName", user.getFirstName());

        return "success";
    }

    @GetMapping("/apagar")
    public String delete()
    {

        return "deleteAccount";
    }

    @GetMapping("/destruir") // Isso era pra ser um DeleteMapping (AJAX) :(
    public void deleteAccount(@RequestParam String valid, HttpServletResponse response) throws IOException
    {

        if (valid.equalsIgnoreCase("true"))
        {
            Authentication authentication = AppUtils.getAuthentication();

            Optional<User> userOptional = userRepository.findById(Long.parseLong(authentication.getName()));

            User user = userOptional.orElseThrow(() -> new NullPointerException("User is null"));

            List<Object> principals =
                    sessionRegistry
                            .getAllPrincipals();

            for (Object principal : principals)
            {
                if (principal instanceof CustomUserDetails
                        && ((CustomUserDetails) principal).getUsername().equals(AppUtils.getAuthentication().getName())
                )
                {
                    for (SessionInformation session : sessionRegistry
                            .getAllSessions(principal, false))
                    {
                        session.expireNow();
                    }
                }
            }

            response.sendRedirect("/logout");

            userRepository.delete(user);
        }
    }

    @GetMapping("/atualizar")
    public String update(Model model)
    {

        Authentication authentication = AppUtils.getAuthentication();

        Optional<User> userOptional = userRepository.findById(Long.parseLong(authentication.getName()));

        User user = userOptional.orElseThrow(() -> new NullPointerException("User is null"));

        model.addAttribute("user", user);

        return "editAccount";
    }

    @PostMapping("/atualizar")
    @Transactional
    public void update(HttpServletResponse response, HttpServletRequest request, @ModelAttribute("newUser") User newUser) throws IOException
    {

        User formUser = userRepository.findByEmail(newUser.getEmail());

        SecurityContext context = SecurityContextHolder.getContext();

        Authentication auth = context.getAuthentication();

        Optional<User> userOptional = userRepository.findById(Long.parseLong(auth.getName()));

        User user = userOptional.orElseThrow(() -> new NullPointerException("User is null"));

        if (formUser != null && !formUser.getEmail().equals(user.getEmail()))
        {

            response.sendRedirect("/atualizar?erro");
        } else
        {

            AppUtils.updateUser(user, newUser);

            Authentication newAuthentication = new
                    PreAuthenticatedAuthenticationToken(user.getId(),
                    user.getPassword(), auth.getAuthorities());

            context.setAuthentication(newAuthentication);

            response.sendRedirect("/");
        }
    }
}
