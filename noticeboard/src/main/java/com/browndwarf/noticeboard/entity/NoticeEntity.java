package com.browndwarf.noticeboard.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity(name="notice")
@Getter @Setter
public class NoticeEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4480640205570342256L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String	title;
	
	private String	contents;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserEntity	user;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private	Timestamp	createdTime;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private	Timestamp	updateTime;
	
	@Builder
	public	NoticeEntity(long id, String title, String contents, UserEntity user) {
		this.id = id;
		this.title = title;
		this.contents = contents;
		this.user = user;
	}
	
}
