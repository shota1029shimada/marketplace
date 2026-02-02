package com.example.marketplace.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// このクラスが JPA エンティティ（DB テーブルと対応）であることを示す
@Entity
// DB テーブル名を category に指定する
@Table(name = "category")
// Lombok によって getter/setter/toString/equals/hashCode を自動生成
@Data
// 引数なしコンストラクタを生成
@NoArgsConstructor
// 全フィールドを引数にとるコンストラクタを生成
@AllArgsConstructor
public class Category {
	// 主キーを表すフィールド
	@Id
	// DB の IDENTITY 方式（自動採番）で値を生成する
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// カテゴリ名のカラム設定。NULL 不可 & 重複不可
	@Column(nullable = false, unique = true)
	private String name;
}