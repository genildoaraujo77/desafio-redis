package demo.spring.jedis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.spring.jedis.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
