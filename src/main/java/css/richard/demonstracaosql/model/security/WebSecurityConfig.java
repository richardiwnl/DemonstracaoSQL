package css.richard.demonstracaosql.model.security;

import css.richard.demonstracaosql.model.services.CustomUserDetailsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Bean
    SessionRegistry sessionRegistry()
    {
        return new SessionRegistryImpl();
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return new CustomUserDetailsServices();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }

    @Override
    public void configure(WebSecurity security)
    {

        security
                .ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    public void configure(HttpSecurity security) throws Exception
    {
        security
                .csrf().disable()
                .sessionManagement()
                .maximumSessions(-1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/")
                .and()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/registrar").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/usuarios/1", true)
                .failureUrl("/login?erro").permitAll()
                .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/").permitAll()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");

    }
}
