package com.kshrd;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kshrd.model.Comment;
import com.kshrd.model.Message;
import com.kshrd.repository.CommentRepository;
import com.kshrd.repository.MessageRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("unchecked")
public class JpaExamApplicationTests {

	@Autowired
	MessageRepository mRepo;
	
	@PersistenceContext
	EntityManager em;
	
	@Test  //JPQL
	public void nPlusOneQueryProblem(){
		List<Message> messages = em.createQuery("select m from Message m", Message.class).getResultList();
		for(Message m: messages){
			System.out.println(m);
			System.out.println(m.getComments());
		}
	}
	
	@Test  //JPQL
	public void queryWithParameter(){
		em.createQuery("select m from Message m where m.id=:id", Message.class)
				.setParameter("id", 72)
				.getResultList()
				.forEach(m->{
					System.out.println(m);
				});
	}
	
	@Test  //JPQL
	public void queryWithParameter1(){
		em.createQuery("select m from Message m where m.id=?1", Message.class)
				.setParameter(1, 72)
				.getResultList()
				.forEach(m->{
					System.out.println(m);
				});
	}
	
	@Test  //JPQL
	public void queryWithParameter2(){
		Message message = em.createQuery("select m from Message m where m.id=:id", Message.class)
				.setParameter("id", 72)
				.getSingleResult();
		System.out.println(message);
	}
	
	@Test  //NativeQuery
	public void nativeQueryWithParameter(){
		em.createNativeQuery("select * from kshrd_message m where m.m_id=?", Message.class)
			.setParameter(1, 72)
			.getResultList()
			.forEach(m->{
				System.out.println(m);
			});
	}
	
	@Test  //NativeQuery
	public void nativeQueryWithParameterSingle(){
		Message message = (Message) em.createNativeQuery("select * from kshrd_message m where m.m_id=?", Message.class)
			.setParameter(1, 72)
			.getSingleResult();
		System.out.println(message);
	}
	
	@Test  //JPQL
	public void nPlusOneQueryProblemNameQuery(){
		List<Message> messages = em.createNamedQuery("findAllMessages", Message.class).getResultList();
		for(Message m: messages){
			System.out.println(m);
			System.out.println(m.getComments());
		}
	}
	
	@Test  //JPQL
	public void solvingNPlusOneJoinFetch(){
		List<Message> messages = em.createNamedQuery("findAllMessagesJoinFetch", Message.class).getResultList();;
		for(Message m: messages){
			System.out.println(m);
			System.out.println(m.getComments());
		}
	}
	
	@Test  //JPQL
	public void solvingNPlusOneEntityGraph(){
		EntityGraph<Message> entityGraph = em.createEntityGraph(Message.class);
		entityGraph.addSubgraph("comments");
		
		List<Message> messages = em.createQuery("select distinct m from Message m", Message.class)
			.setHint("javax.persistence.loadgraph", entityGraph)
			.getResultList();
		
		for(Message m: messages){
			System.out.println(m);
			System.out.println(m.getComments());
		}
	}
	
	@Test
	public void nativeQuery(){
		List<Message> messages = em.createNativeQuery("select * from kshrd_message", Message.class).getResultList();
		for(Message m: messages){
			System.out.println(m);
			System.out.println(m.getComments());
		}
	}

	@Test
	public void criteriaQuery() {
		
		//Build Query: select m from Message m
		
		// 1. Use Criteria Builder to create a Criteria Query returning the expected result object 
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);

		// 2. Define roots for tables which are involved in the query
		Root<Message> messageRoot = cq.from(Message.class);
		
		// 3. Define predicates etc using Criteria Builder
		// 4. Add predicates etc to the Criteria Query

		TypedQuery<Message> query = em.createQuery(cq.select(messageRoot));
		// 5. Build the TypedQuery using the entity manager and criteria query
		List<Message> messages = query.getResultList();

		for (Message m : messages) {
			System.out.println(m);
			System.out.println(m.getComments());
		}
	}
	
	@Autowired
	CommentRepository cRepo;
	
	@Test
	public void saveCommentOnMessage(){
		Comment c1 = new Comment("Hi@!");
		cRepo.addComment(c1, 72);
	}
	
	@Test
	public void removeComment(){
		cRepo.removeComment(72, 82);
	}
	
	@Test
	public void addMessageAndComments() {
		Message m = new Message("Sad!!!");
		m.addComment(new Comment("So Sad!!!"));
		m.addComment(new Comment("Me too :'("));
		mRepo.saveMessageAndComments(m);
	}

	@Test
	public void removeMessageComment() {
		mRepo.removeComment(72);
	}
}
