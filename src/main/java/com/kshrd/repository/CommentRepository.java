package com.kshrd.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kshrd.model.Comment;
import com.kshrd.model.Message;

@Repository
@Transactional
public class CommentRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MessageRepository mRepo;
	
	public void addComment(Comment comment, Integer messageId){
		Message message = em.getReference(Message.class, messageId);
		//Message message = mRepo.findOne(messageId);
		//Message message = em.find(Message.class, messageId);
		
		System.out.println(message);
		comment.setMessage(message);
		em.persist(comment);
	}
	
	public void removeComment(Integer mId, Integer commentId){
		Message message = mRepo.findOne(mId);
		message.removeComment(this.findOne(commentId));
	}
	
	public Comment findOne(Integer commentId){
		return em.find(Comment.class, commentId);
	}
}
