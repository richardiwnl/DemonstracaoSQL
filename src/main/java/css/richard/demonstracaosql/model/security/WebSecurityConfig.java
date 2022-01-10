package css.richard.demonstracaosql.model.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.io.IOException;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    public void configure(WebSecurity security)
    {

        security
                .ignoring()
                .antMatchers("/css/**", "/js/**");
    }

    @Override
    public void configure(HttpSecurity security) throws Exception
    {

        security
                .authorizeRequests()
                .antMatchers("/", "/login", "/registrar").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/usuarios/1")
                .failureUrl("/login?erro")
                .usernameParameter("email")
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");
    }
}
