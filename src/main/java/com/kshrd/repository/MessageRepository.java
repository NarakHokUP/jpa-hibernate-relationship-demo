package com.kshrd.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kshrd.model.Comment;
import com.kshrd.model.Message;

@Repository
@Transactional
public class MessageRepository {

	@PersistenceContext
	EntityManager em;
	
	public void saveMessageAndComments(Message message){
		em.persist(message);
	}
	
	public Message findOne(Integer id){
		return em.find(Message.class, id);
	}
	
	public void removeMessage(Integer id){
		em.remove(this.findOne(id));
	}
	
	public void removeComment(Integer mId){
		Message message = this.findOne(mId);
		Comment c = message.getComments().get(0);
		message.removeComment(c);
	}
	
}
