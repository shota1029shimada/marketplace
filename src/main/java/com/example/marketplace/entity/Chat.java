package com.example.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * チャットメッセージ（chat）
 */
@Entity
@Table(name = "chat")
public class Chat {

	/**
	 * チャットID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 対象商品
	 */
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	/**
	 * 送信者
	 */
	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	/**
	 * メッセージ本文
	 */
	@Column(columnDefinition = "TEXT", nullable = false)
	private String message;

	/**
	 * 送信日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public Chat() {
	}

	public Chat(Long id, Item item, User sender, String message, LocalDateTime createdAt) {
		this.id = id;
		this.item = item;
		this.sender = sender;
		this.message = message;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
