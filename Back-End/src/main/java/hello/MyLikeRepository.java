package hello;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MyLikeRepository extends CrudRepository<MyLike,Long> {
	public MyLike findByArticle(Article article);
	public MyLike findByArticleAndUser(Article article,User user);
	@Query("select l from Article a, MyLike l where (a.id = l.article.id and a.user = ?1)")
	public Iterable<MyLike> findAllLikesToUserArticles(User user);
}
