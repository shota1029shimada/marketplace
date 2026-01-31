package com.example.marketplace.repository;

//Optional を返すために利用
import java.util.Optional;

//Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
//リポジトリアノテーション
import org.springframework.stereotype.Repository;

//エンティティのインポート
import com.example.marketplace.entity.User;

//User エンティティのリポジトリ
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	//メールアドレスでユーザーを検索（ログイン/認可で使用）
	Optional<User> findByEmail(String email);
}