package com.example.marketplace.entity;

import java.math.BigDecimal;
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
 * 出品商品（item）
 */
@Entity
@Table(name = "item")
public class Item {

	/**
	 * 商品ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 出品者
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User seller;

	/**
	 * 商品名
	 */
	@Column(nullable = false, length = 255)
	private String name;

	/**
	 * 商品説明
	 */
	@Column(columnDefinition = "TEXT")
	private String description;

	/**
	 * 価格
	 */
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	/**
	 * カテゴリ
	 */
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	/**
	 * 商品ステータス（出品中 / 売却済）
	 */
	@Column(nullable = false, length = 20)
	private String status = "出品中";

	/**
	 * 画像URL
	 */
	@Column(name = "image_url")
	private String imageUrl;

	/**
	 * 登録日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public Item() {
	}

	public Item(Long id, User seller, String name, String description, BigDecimal price, Category category, String status,
			String imageUrl, LocalDateTime createdAt) {
		this.id = id;
		this.seller = seller;
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.status = status;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
