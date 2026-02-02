package com.example.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * 購入後レビュー（review）
 */
@Entity
@Table(name = "review")
public class Review {

	/**
	 * レビューID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 注文（注文1件にレビュー1件）
	 */
	@OneToOne
	@JoinColumn(name = "order_id", nullable = false, unique = true)
	private AppOrder order;

	/**
	 * レビュー投稿者（購入者）
	 */
	@ManyToOne
	@JoinColumn(name = "reviewer_id", nullable = false)
	private User reviewer;

	/**
	 * 出品者
	 */
	@ManyToOne
	@JoinColumn(name = "seller_id", nullable = false)
	private User seller;

	/**
	 * 対象商品
	 */
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	/**
	 * 評価（1〜5）
	 */
	@Column(nullable = false)
	private Integer rating;

	/**
	 * コメント
	 */
	@Column(columnDefinition = "TEXT")
	private String comment;

	/**
	 * 作成日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public Review() {
	}

	public Review(Long id, AppOrder order, User reviewer, User seller, Item item, Integer rating, String comment,
			LocalDateTime createdAt) {
		this.id = id;
		this.order = order;
		this.reviewer = reviewer;
		this.seller = seller;
		this.item = item;
		this.rating = rating;
		this.comment = comment;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AppOrder getOrder() {
		return order;
	}

	public void setOrder(AppOrder order) {
		this.order = order;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
