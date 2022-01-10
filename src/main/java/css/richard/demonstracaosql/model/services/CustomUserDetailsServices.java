package css.richard.demonstracaosql.model.services;

import css.richard.demonstracaosql.model.details.CustomUserDetails;
import css.richard.demonstracaosql.model.entities.User;
import css.richard.demonstracaosql.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServices implements UserDetailsService
{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {

        User user = userRepository.findByEmail(username);

        if (user == null)
        {
            throw new UsernameNotFoundException("User not found!");
        }

        return new CustomUserDetails(user);
    }
}
