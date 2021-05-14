package br.com.rmf.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rmf.entities.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
