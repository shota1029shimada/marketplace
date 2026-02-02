package com.example.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// このクラスが JPA エンティティ（DB テーブルと対応）であることを示す
@Entity
// DB テーブル名を category に指定する
@Table(name = "category")
public class Category {
	// 主キーを表すフィールド
	@Id
	// DB の IDENTITY 方式（自動採番）で値を生成する
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// カテゴリ名のカラム設定。NULL 不可 & 重複不可
	@Column(nullable = false, unique = true)
	private String name;

	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}