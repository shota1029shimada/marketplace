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

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文情報（app_order）
 */
@Entity
@Table(name = "app_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
