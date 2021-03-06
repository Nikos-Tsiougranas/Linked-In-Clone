package hello;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.StringTokenizer;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import hello.User;
import hello.UserRepository;
import hello.Article;
import hello.ArticleRepository;
import hello.Relationship;
import hello.RelationshipRepository;

@CrossOrigin
@Controller    // This means that this class is a Controller
//@RequestMapping(path="/welcome") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	           // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private RelationshipRepository relationshipRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private MyLikeRepository mylikeRepository;
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private MessageRepository messageRepository;
	
	@GetMapping(path="/login") // Map ONLY GET Requests
	public @ResponseBody boolean loginUser (@RequestHeader("Authorization") String encoded) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		//encoded string is like that: "Basic YUASGDGHGNBAHJSDFG" and we need the second part
		StringTokenizer stk = new StringTokenizer(encoded," ");
		stk.nextToken();
		encoded = stk.nextToken();
		//decode the encoded part
		byte[] decodedBytes = Base64.getDecoder().decode(encoded);
		String decoded = new String(decodedBytes);
		stk = new StringTokenizer(decoded,":");
		//retrieve email and password
		String email = stk.nextToken();
		String password = stk.nextToken();
		User user = new User();
		user = userRepository.findByEmail(email);
		if(user!=null && user.getPassword().equals(password)) {
			System.out.println("User: " + email + " successfully logged in");
			return true;
		}
		else {
			System.out.println("ACCESS DENIED");
			return false;
		}
	}

	@RequestMapping(method=RequestMethod.POST,path="/signup") // Map ONLY GET Requests
	public @ResponseBody boolean addNewUser (@RequestHeader("Authorization") String encoded,@RequestBody MultipartFile file) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		//encoded string is like that: "Basic YUASGDGHGNBAHJSDFG" and we need the second part
		User user = new User();
		StringTokenizer stk = new StringTokenizer(encoded," ");
		stk.nextToken();
		encoded = stk.nextToken();
		//decode the encoded part
		byte[] decodedBytes = Base64.getDecoder().decode(encoded);
		String decoded = new String(decodedBytes);
		stk = new StringTokenizer(decoded,":");
		//retrieve user details
		String email = stk.nextToken();
		String password = stk.nextToken();
		String firstname = stk.nextToken();
		String lastname = stk.nextToken();
		String phone = stk.nextToken();
		if((user = userRepository.findByEmail(email)) == null) {
			User n = new User();
			n.setEmail(email);
			n.setPassword(password);
			n.setFirstname(firstname);
			n.setLastname(lastname);
			n.setPhone(phone);
			n.setWorkPrivacy("private");
			n.setEduPrivacy("private");
			n.setSkillsPrivacy("private");
			//store profile pic
			final Path rootLocation = Paths.get("C:/uploads");
			if(!Files.exists(rootLocation)) {
				System.out.println("Creating directory: C:/uploads");
				new File("C:/uploads").mkdir();
			}
			System.out.println(rootLocation.toAbsolutePath().toString());
			long count=0;
			String filename=null;
			if(file!=null) {
				try {
					try (Stream<Path> files = Files.list(Paths.get("C:/uploads"))) {
					    count = files.count();
					}
					System.out.println("count = "+count);
					System.out.println("Content type: "+file.getContentType());
					if(file.getContentType().contains("image")) {
						filename = Long.toString(count)+".image";
					}
					Files.copy(file.getInputStream(), rootLocation.resolve(filename),StandardCopyOption.REPLACE_EXISTING);
					user.setProfileImage(filename);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			userRepository.save(n);
			return true;
		}
		else {
			System.out.println("User with the same email already exits!");
			return false;
		}	
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/settings")
	public @ResponseBody boolean updateUser (@RequestHeader("Authorization") String encoded,@RequestBody String credentials ){
		if(loginUser(encoded)) {
			User user = new User();
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			user = userRepository.findByEmail(email);
			System.out.println("Credentials: " + credentials);
			stk = new StringTokenizer(credentials,":");
			email = stk.nextToken();
			String password = stk.nextToken();
			user.setEmail(email);
			user.setPassword(password);
			userRepository.save(user);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/post")
	public @ResponseBody boolean postArticle (@RequestHeader("Authorization") String encoded,@RequestBody MultipartFile file,@RequestParam("postText") String postText){
		if(loginUser(encoded)) {
			//store files
			final Path rootLocation = Paths.get("C:/uploads");
			if(!Files.exists(rootLocation)) {
				System.out.println("Creating directory: C:/uploads");
				new File("C:/uploads").mkdir();
			}
			
			
			System.out.println(rootLocation.toAbsolutePath().toString());
			long count=0;
			String filename=null;
			if(file!=null) {
				try {
					try (Stream<Path> files = Files.list(Paths.get("C:/uploads"))) {
					    count = files.count();
					}
					System.out.println("count = "+count);
					System.out.println("Content type: "+file.getContentType());
					if(file.getContentType().contains("image")) {
						filename = Long.toString(count)+".image";
					}
					else if(file.getContentType().contains("video")) {
						filename = Long.toString(count)+".video";
					}
					else if(file.getContentType().contains("audio")) {
						filename = Long.toString(count)+".audio";
					}
					Files.copy(file.getInputStream(), rootLocation.resolve(filename),StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    User user = new User();
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			user = userRepository.findByEmail(email);
			
			Article article = new Article();
			article.setText(postText);
			article.setUser(user);
			if(file!=null) {
				article.setFile(filename);
			}
			user.createArticle(article);
			userRepository.save(user);
			articleRepository.save(article);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getFile/{fileId}")
	public @ResponseBody byte[] getFile (@PathVariable String fileId){
		final Path rootLocation = Paths.get("C:/uploads/");
		System.out.println("rootLocation: "+rootLocation.toAbsolutePath().toString());
		String filepath = rootLocation.toAbsolutePath().toString()+"/"+fileId;
		Path path = Paths.get(filepath);
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    return data;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/articles")
	public @ResponseBody Iterable<Article> getArticles (@RequestHeader("Authorization") String encoded ){
		if(loginUser(encoded)) {
		    User user = new User();
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			user = userRepository.findByEmail(email);
			
			return articleRepository.findAllByOrderByIdDesc();
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/search")
	public @ResponseBody Iterable<User> searchUsers (@RequestHeader("Authorization") String encoded,@RequestParam("name") String name){
		if(loginUser(encoded)) {
			System.out.println("name: " + name);
			StringTokenizer stk = new StringTokenizer(name," ");
			String firstname,lastname;
			firstname = stk.nextToken();
			lastname = stk.nextToken();
			System.out.println("Firstname: " + firstname + "\nLastname: " + lastname);
			return userRepository.findAllByFirstnameAndLastname(firstname,lastname);
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/user")
	public @ResponseBody User getUser (@RequestHeader("Authorization") String encoded,@RequestParam("email") String email){
		if(loginUser(encoded)) {
			return userRepository.findByEmail(email);
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/addFriend")
	public @ResponseBody boolean addFriend (@RequestHeader("Authorization") String encoded,@RequestBody String email){
		System.out.println("got here");
		if(loginUser(encoded)) {
			User userOne,userTwo,temp;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String myemail = stk.nextToken();
			
			userTwo = userRepository.findByEmail(email);
			userOne = userRepository.findByEmail(myemail);//this is the user that makes the request
			System.out.println(email+ " "+ myemail);
			Relationship rel = new Relationship();
			if(userOne.getId() > userTwo.getId()) {//always userOne id must be less than userTwo id
				temp = userOne;
				userOne = userTwo;
				userTwo = temp;
				rel.setUserOne(userOne);
				rel.setUserTwo(userTwo);
				rel.setStatus("Pending");
				rel.setActionUserId(userTwo.getId());
			}
			else {
				rel.setUserOne(userOne);
				rel.setUserTwo(userTwo);
				rel.setStatus("Pending");
				rel.setActionUserId(userOne.getId());
			}
			System.out.println("saved");
			relationshipRepository.save(rel);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/friendshipStatus")
	public @ResponseBody String getFriendshipStatus (@RequestHeader("Authorization") String encoded,@RequestParam("email") String email){
		if(loginUser(encoded)) {
			User userOne,userTwo,temp;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String myemail = stk.nextToken();
			
			userTwo = userRepository.findByEmail(email);
			userOne = userRepository.findByEmail(myemail);//this is the user that makes the request
			Relationship rel;
			if(userOne.getId() > userTwo.getId()) {//always userOne id must be less than userTwo id
				temp = userOne;
				userOne = userTwo;
				userTwo = temp;
				rel = relationshipRepository.findByUserOneAndUserTwo(userOne, userTwo);
			}
			else {
				rel = relationshipRepository.findByUserOneAndUserTwo(userOne, userTwo);
			}
			System.out.println("status: " + rel.getStatus());
			return rel.getStatus();
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/friendRequests")
	public @ResponseBody Iterable<User> getFriendRequests (@RequestHeader("Authorization") String encoded){
		if(loginUser(encoded)) {
			User user;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			user = userRepository.findByEmail(email);
			Iterable<Relationship> rel;
			rel = relationshipRepository.findFriendRequests(user,"Pending",user.getId());
			List<User> list = new ArrayList<User>();
			Iterable<User> results;
			for(Relationship r: rel) {
				if(r.getUserOne().getId() == user.getId()) {
					list.add(r.getUserTwo());
				}
				else {
					list.add(r.getUserOne());
				}
			}
			results = list;
			return results;
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/accept")
	public @ResponseBody boolean accept (@RequestHeader("Authorization") String encoded,@RequestBody String email){
		if(loginUser(encoded)) {
			User userOne,userTwo,temp;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String myemail = stk.nextToken();
			
			userTwo = userRepository.findByEmail(email);
			userOne = userRepository.findByEmail(myemail);//this is the user that makes the request
			System.out.println(email+ " "+ myemail);
			Relationship rel = new Relationship();
			if(userOne.getId() > userTwo.getId()) {//always userOne id must be less than userTwo id
				temp = userOne;
				userOne = userTwo;
				userTwo = temp;
				rel = relationshipRepository.findByUserOneAndUserTwo(userOne,userTwo);
				rel.setStatus("Friends");
				rel.setActionUserId(userTwo.getId());
			}
			else {
				rel = relationshipRepository.findByUserOneAndUserTwo(userOne,userTwo);
				rel.setStatus("Friends");
				rel.setActionUserId(userOne.getId());
			}
			System.out.println("saved");
			relationshipRepository.save(rel);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/decline")
	public @ResponseBody boolean decline (@RequestHeader("Authorization") String encoded,@RequestBody String email){
		if(loginUser(encoded)) {
			User userOne,userTwo;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String myemail = stk.nextToken();
			
			userTwo = userRepository.findByEmail(email);
			userOne = userRepository.findByEmail(myemail);//this is the user that makes the request
			System.out.println(email+ " "+ myemail);
			Relationship rel = new Relationship();
			if(userOne.getId() > userTwo.getId()) {//always userOne id must be less than userTwo id
				rel = relationshipRepository.findByUserOneAndUserTwo(userTwo,userOne);
				relationshipRepository.delete(rel);
			}
			else {
				rel = relationshipRepository.findByUserOneAndUserTwo(userOne,userTwo);
				relationshipRepository.delete(rel);
			}
			System.out.println("deleted");
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/networkUsers")
	public @ResponseBody Iterable<User> getNetworkUsers (@RequestHeader("Authorization") String encoded,@RequestParam("email") String email){
		if(loginUser(encoded)) {
			User user;
			
			if(email.equals("null")) {
				//find email from encoded
				StringTokenizer stk = new StringTokenizer(encoded," ");
				stk.nextToken();
				encoded = stk.nextToken();
				//decode the encoded part
				byte[] decodedBytes = Base64.getDecoder().decode(encoded);
				String decoded = new String(decodedBytes);
				stk = new StringTokenizer(decoded,":");
				//retrieve user details
				email = stk.nextToken();
				user = userRepository.findByEmail(email);
				Iterable<Relationship> rel;
				rel = relationshipRepository.findFriends(user);
				List<User> list = new ArrayList<User>();
				Iterable<User> results;
				for(Relationship r: rel) {
					if(r.getUserOne().getId() == user.getId()) {
						list.add(r.getUserTwo());
					}
					else {
						list.add(r.getUserOne());
					}
				}
//				for(int i=0;i<18;i++) {//test design
//					list.add(list.get(1));
//				}
				results = list;
				return results;
			}
			else if(getFriendshipStatus(encoded,email).equals("Friends")) {
				user = userRepository.findByEmail(email);
				Iterable<Relationship> rel;
				rel = relationshipRepository.findFriends(user);
				List<User> list = new ArrayList<User>();
				Iterable<User> results;
				for(Relationship r: rel) {
					if(r.getUserOne().getId() == user.getId()) {
						list.add(r.getUserTwo());
					}
					else {
						list.add(r.getUserOne());
					}
				}
//				for(int i=0;i<18;i++) {//test design
//					list.add(list.get(1));
//				}
				results = list;
				return results;
			}
			return null;
			
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/comment")
	public @ResponseBody boolean postComment(@RequestHeader("Authorization") String encoded,@RequestParam("articleId") int articleId,@RequestParam("commentText") String commentText){
		if(loginUser(encoded)) {
			User user;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			user = userRepository.findByEmail(email);
			Article article = articleRepository.findById(articleId);
			Comment comment = new Comment();
			comment.setUser(user);
			comment.setArticle(article);
			comment.setText(commentText);
			
			commentRepository.save(comment);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getComments")
	public @ResponseBody Iterable<Comment> getComments(@RequestHeader("Authorization") String encoded,@RequestParam("articleId") int articleId){
		if(loginUser(encoded)) {
			Article article = articleRepository.findById(articleId);
			return commentRepository.findAllByArticle(article);
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/like")
	public @ResponseBody boolean like(@RequestHeader("Authorization") String encoded,@RequestParam("articleId") int articleId,@RequestParam("status") String status){
		if(loginUser(encoded)) {
			User user;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			user = userRepository.findByEmail(email);
			Article article = articleRepository.findById(articleId);
			MyLike like = mylikeRepository.findByArticle(article);
			if(like == null) {
				like = new MyLike();
			}
			like.setUser(user);
			like.setArticle(article);
			if(status.equals("Like")) {
				like.setStatus("Like");
				mylikeRepository.save(like);
			}
			else {
				mylikeRepository.delete(like);
			}
			
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getLike")
	public @ResponseBody MyLike getLike(@RequestHeader("Authorization") String encoded,@RequestParam("articleId") int articleId){
		if(loginUser(encoded)) {
			User user;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			user = userRepository.findByEmail(email);
			
			Article article = articleRepository.findById(articleId);
			return mylikeRepository.findByArticleAndUser(article,user);
		}
		return null;
	}

	@RequestMapping(method=RequestMethod.POST,path="/editProfile")
	public @ResponseBody boolean editProfile(@RequestHeader("Authorization") String encoded,@RequestBody MultipartFile file,@RequestParam("name") String name,@RequestParam("phone") String phone,@RequestParam("workExp") String workExp,@RequestParam("eduExp") String eduExp,@RequestParam("skillsExp") String skillsExp,@RequestParam("workPrivacy") String workPrivacy, @RequestParam("eduPrivacy") String eduPrivacy,@RequestParam("skillsPrivacy") String skillsPrivacy){
		if(loginUser(encoded)) {
			User user;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			user = userRepository.findByEmail(email);
			stk = new StringTokenizer(name," ");
			String first,last;
			first = stk.nextToken();
			last = stk.nextToken();
			user.setFirstname(first);
			user.setLastname(last);
			user.setPhone(phone);
			user.setWorkExp(workExp);
			user.setEduExp(eduExp);
			user.setSkillsExp(skillsExp);
			user.setWorkPrivacy(workPrivacy);
			user.setEduPrivacy(eduPrivacy);
			user.setSkillsPrivacy(skillsPrivacy);
			//store profile pic
			final Path rootLocation = Paths.get("C:/uploads");
			if(!Files.exists(rootLocation)) {
				System.out.println("Creating directory: C:/uploads");
				new File("C:/uploads").mkdir();
			}
			System.out.println(rootLocation.toAbsolutePath().toString());
			long count=0;
			String filename=null;
			if(file!=null) {
				try {
					if(file.getContentType().contains("image")) {
						//delete previous profile pic from disc and update with new
						if(user.getProfileImage() != null && Files.deleteIfExists(rootLocation.resolve(user.getProfileImage()))) {
							stk = new StringTokenizer(user.getProfileImage(),".");
							String imageId = stk.nextToken();
							System.out.println("imageId: "+imageId);
							filename = imageId+".image";
							System.out.println("filename: "+filename);
						}
						else {
							try (Stream<Path> files = Files.list(Paths.get("C:/uploads"))) {
							    count = files.count();
							    filename = Long.toString(count)+".image";
							}
						}
						System.out.println("Writing file to disc: "+filename);
						Files.copy(file.getInputStream(), rootLocation.resolve(filename),StandardCopyOption.REPLACE_EXISTING);
						user.setProfileImage(filename);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			userRepository.save(user);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getLastChat")
	public @ResponseBody String getLastChat(@RequestHeader("Authorization") String encoded){
		if(loginUser(encoded)) {
			User user;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			user = userRepository.findByEmail(email);
			
			return user.getLastChatUser();
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/sendMessage")
	public @ResponseBody boolean sendMessage(@RequestHeader("Authorization") String encoded,@RequestParam("email") String friendEmail,@RequestBody String message){
		if(loginUser(encoded)) {
			User userOne,userTwo,temp,actionUser;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			userOne = userRepository.findByEmail(email);
			userTwo = userRepository.findByEmail(friendEmail);
			
			actionUser = userOne;
			
			if(userOne.getId() > userTwo.getId()) {//always userOne id must be less than userTwo id
				temp = userOne;
				userOne = userTwo;
				userTwo = temp;
			}
			Chat chat = chatRepository.findByUserOneAndUserTwo(userOne, userTwo);
			if(chat == null) {//first time users exchange messages
				chat = new Chat();
				chat.setUserOne(userOne);
				chat.setUserTwo(userTwo);
			}
			Clock clock = Clock.systemDefaultZone();
			Message mes = new Message(actionUser,message,clock.millis(),chat);
			chatRepository.save(chat);
			messageRepository.save(mes);
			userOne.setLastChatUser(userTwo.getEmail());
			userTwo.setLastChatUser(userOne.getEmail());
			userRepository.save(userOne);
			userRepository.save(userTwo);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getChat")
	public @ResponseBody Iterable<Message> getChat(@RequestHeader("Authorization") String encoded,@RequestParam("email") String friendEmail){
		if(loginUser(encoded)) {
			User userOne,userTwo,temp;
			
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			
			userOne = userRepository.findByEmail(email);
			userTwo = userRepository.findByEmail(friendEmail);
			
			if(userOne.getId() > userTwo.getId()) {//always userOne id must be less than userTwo id
				temp = userOne;
				userOne = userTwo;
				userTwo = temp;
			}
			
			Chat chat = chatRepository.findByUserOneAndUserTwo(userOne, userTwo);
			return messageRepository.findAllByChatOrderByIdAsc(chat);
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getLatestChats")
	public @ResponseBody Iterable<User> getLatestChats (@RequestHeader("Authorization") String encoded){
		if(loginUser(encoded)) {
			User user;
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			user = userRepository.findByEmail(email);
			Iterable<Integer> chatIds = messageRepository.findLatestChats();
			ArrayList<Integer> uniqChatIds = new ArrayList<Integer>();
			Chat chat;
			List<User> userlist = new ArrayList<User>();
			for(Integer id: chatIds) {
				if(!uniqChatIds.contains(id)) {
					uniqChatIds.add(id);
					chat = chatRepository.findById((int)id);
					if(chat.getUserOne().getId() == user.getId()) {
						userlist.add(chat.getUserTwo());
					}
					else {
						userlist.add(chat.getUserOne());
					}
				}
			}
			return userlist;
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getAllComments")
	public @ResponseBody Iterable<Comment> getAllComments(@RequestHeader("Authorization") String encoded){
		if(loginUser(encoded)) {
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			User user = userRepository.findByEmail(email);
			//ArrayList<Article> articles = (ArrayList<Article>) articleRepository.findAllByUser(user);
			ArrayList<Comment> comments = (ArrayList<Comment>) commentRepository.findAllCommentsToUserArticles(user);
//			for(Article a: articles) {
//				comments.addAll((ArrayList<Comment>) commentRepository.findAllByArticle(a));
//			}
//			comments.sort(Comparator.comparing(Comment::id()).reversed());
			return comments;
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getAllLikes")
	public @ResponseBody Iterable<MyLike> getAllLikes(@RequestHeader("Authorization") String encoded){
		if(loginUser(encoded)) {
			//find email from encoded
			StringTokenizer stk = new StringTokenizer(encoded," ");
			stk.nextToken();
			encoded = stk.nextToken();
			//decode the encoded part
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String decoded = new String(decodedBytes);
			stk = new StringTokenizer(decoded,":");
			//retrieve user details
			String email = stk.nextToken();
			User user = userRepository.findByEmail(email);
			ArrayList<MyLike> likes = (ArrayList<MyLike>) mylikeRepository.findAllLikesToUserArticles(user);	
			return likes;
		}
		return null;
	}
	
}