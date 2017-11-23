package com.kshrd.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

		
@NamedQueries(value = {
		@NamedQuery(name = "findAllMessages", query = "select distinct m from Message m"),
		@NamedQuery(name = "findAllMessagesJoinFetch", query = "select distinct m from Message m join fetch m.comments"),
})
@Entity(name = "Message")
@Table(name = "kshrd_message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "m_id")
	private Integer id;

	@Column(name = "m_text")
	private String text;

	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	public Message() {
		super();
	}

	public Message(String text) {
		super();
		this.text = text; 
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void addComment(Comment comment){
		comments.add(comment);
		comment.setMessage(this);
	}
	public void removeComment(Comment comment){
		comments.remove(comment);
		comment.setMessage(null);
	}
	
	public List<Comment> getComments(){
		return this.comments;
	}
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", text=" + text + "]";
	}
}
