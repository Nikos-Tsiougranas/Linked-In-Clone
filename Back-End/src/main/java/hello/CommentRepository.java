package hello;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,Long> {
	public Iterable<Comment> findAllByArticle(Article article);
	public Iterable<Comment> findAllByUserOrderByIdDesc(User user);
	@Query("select c from Article a, Comment c where (a.id = c.article.id and a.user = ?1)")
	public Iterable<Comment> findAllCommentsToUserArticles(User user);
}
