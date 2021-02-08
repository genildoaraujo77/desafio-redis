package demo.spring.jedis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.spring.jedis.model.Gasto;

public interface GastosRepository extends JpaRepository<Gasto, Integer> {

}
