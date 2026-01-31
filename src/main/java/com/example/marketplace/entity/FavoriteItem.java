package com.example.marketplace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// このクラスが JPA のエンティティ（DB テーブルと対応）であることを示す
@Entity
// 対応する DB テーブル名を favorite
_ item に指定 @Table(name="favorite_item")
// Lombok により getter/setter/toString/equals/hashCode などを自動生成
@Data
// 引数なしコンストラクタを生成
@NoArgsConstructor
// 全フィールドを引数に持つコンストラクタを生成
@AllArgsConstructor
public class FavoriteItem {
	// 主キーの定義
	@Id
	// 主キーの採番方法として IDENTITY(オートインクリメント) を指定
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// 多対一リレーション：複数の「お気に入り」レコードが 1 人のユーザーに紐づく
@ManyToOne
// 外部キー列名を user
_
id とし、NULL 不可 @JoinColumn(name="user_id",nullable=false)
	private User user;
	// 多対一リレーション：複数の「お気に入り」レコードが 1 つの商品に紐づく
@ManyToOne
// 外部キー列名を item
_
id とし、NULL 不可 @JoinColumn(name="item_id",nullable=false)
	private Item item;
	// お気に入り登録日時。created
	_ at という列名で必須扱い @Column(name="created_at",nullable=false)
	private LocalDateTime createdAt = LocalDateTime.now();
}