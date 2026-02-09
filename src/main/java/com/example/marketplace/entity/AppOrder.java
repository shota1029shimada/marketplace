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

/**s
 * 注文情報（app_order）
 */
@Entity
@Table(name = "app_order")
public class AppOrder {

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
	 * 購入者
	 */
	@ManyToOne
	@JoinColumn(name = "buyer_id", nullable = false)
	private User buyer;

	/**
	 * 購入価格
	 */
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	/**
	 * 購入状態（DBのデフォルトは '購入済' 想定）
	 */
	@Column(nullable = false, length = 20)
	private String status = "購入済";

	/**
	 * 購入日時（DB側 default CURRENT_TIMESTAMP と併用するなら、
	 * アプリ側で入れるならこのままでOK）
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	/**
	 * Stripe PaymentIntent ID
	 */
	@Column(name = "payment_intent_id", length = 128, unique = true)
	private String paymentIntentId;

	public AppOrder() {
	}

	public AppOrder(Long id, Item item, User buyer, BigDecimal price, String status, LocalDateTime createdAt,
			String paymentIntentId) {
		this.id = id;
		this.item = item;
		this.buyer = buyer;
		this.price = price;
		this.status = status;
		this.createdAt = createdAt;
		this.paymentIntentId = paymentIntentId;
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

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getPaymentIntentId() {
		return paymentIntentId;
	}

	public void setPaymentIntentId(String paymentIntentId) {
		this.paymentIntentId = paymentIntentId;
	}
}
