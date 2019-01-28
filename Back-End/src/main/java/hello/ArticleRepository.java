package hello;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ArticleRepository extends CrudRepository<Article, Long> {
	public Iterable<Article> findAllByOrderByIdDesc();
	public Article findById(int id);
	public Iterable<Article> findAllByUser(User user);
}
