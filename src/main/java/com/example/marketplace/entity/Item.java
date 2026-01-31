package com.example.marketplace.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 出品商品（item）
 */
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
