package css.richard.demonstracaosql.model.repositories;

import css.richard.demonstracaosql.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

    User findByEmail(String email);
}
