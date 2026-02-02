package com.example.marketplace.repository;

//Optional を返すために利用
import java.util.Optional;

//Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//リポジトリアノテーション
import org.springframework.stereotype.Repository;

//エンティティのインポート
import com.example.marketplace.entity.User;

//User エンティティのリポジトリ
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	//メールアドレスでユーザーを検索（ログイン/認可で使用）
	Optional<User> findByEmail(String email);

	// 追加：対象ユーザーの平均評価を取得（レビューが無い場合は null）
	@Query("select avg(r.rating) from Review r where r.seller.id = :userId")
	Double averageRatingForUser(@Param("userId") Long userId);

	//追加
	Optional<User> findByEmailIgnoreCase(String email);
}