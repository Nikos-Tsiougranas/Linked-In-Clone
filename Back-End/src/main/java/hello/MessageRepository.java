package hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long>, JpaRepository<Message,Long>{
	public Iterable<Message> findAllByChatOrderByIdAsc(Chat chat);
	@Query("select m.chat.id from Message m order by m.id desc")
	public Iterable<Integer> findLatestChats();
}
