package css.richard.demonstracaosql.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class WebController
{

    @GetMapping
    public void root(HttpServletResponse response) throws IOException
    {

        response.sendRedirect("/login");
    }


    @GetMapping("/login")
    public String home(Model model)
    {

        return "home";
    }
}
