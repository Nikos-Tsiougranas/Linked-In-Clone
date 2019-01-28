package hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import hello.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long>,JpaRepository<User,Long> {
	public User findByEmail(String email);
	public Iterable<User> findAllByFirstnameAndLastname(String firstname,String lastname);
}