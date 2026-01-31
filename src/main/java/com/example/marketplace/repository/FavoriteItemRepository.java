package com.example.marketplace.repository;

//コレクション/Optional
import java.util.List;
import java.util.Optional;

//Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
//リポジトリアノテーション
import org.springframework.stereotype.Repository;

import com.example.marketplace.entity.FavoriteItem;
import com.example.marketplace.entity.Item;
import com.example.marketplace.entity.User;

//FavoriteItem エンティティ用のリポジトリ
@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
	//ユーザーと商品で一意に検索（ユニーク制約と対応）
	Optional<FavoriteItem> findByUserAndItem(User user, Item item);

	//ユーザーのお気に入り商品を一覧取得
	List<FavoriteItem> findByUser(User user);

	//既にお気に入り済みか存在チェック（二重登録防止用）
	boolean existsByUserAndItem(User user, Item item);
}