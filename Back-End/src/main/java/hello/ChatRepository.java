package hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, Long>, JpaRepository<Chat,Long>{
	public Chat findByUserOneAndUserTwo(User userOne,User userTwo);
	public Chat findById(int id);
}
