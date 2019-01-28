package hello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RelationshipRepository extends CrudRepository<Relationship, Long>, JpaRepository<Relationship,Long> {
	public Relationship findByUserOneAndUserTwo(User userOne,User userTwo);
	@Query("select r from Relationship r where (r.userOne = ?1 or r.userTwo = ?1) and r.status = ?2 and r.actionUserid != ?3")
	public Iterable<Relationship> findFriendRequests(User user,String status,int id);
	@Query("select r from Relationship r where (r.userOne = ?1 or r.userTwo = ?1) and r.status = 'Friends'")
	public Iterable<Relationship> findFriends(User user);
}