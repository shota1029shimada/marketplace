package com.example.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * users テーブルに対応するエンティティ
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // ユーザーID

	@Column(nullable = false, length = 50)
	private String name; // 表示名

	@Column(nullable = false, unique = true, length = 255)
	private String email; // ログインID（メール）

	@Column(nullable = false, length = 255)
	private String password; // ハッシュ化済パスワード

	@Column(nullable = false, length = 20)
	private String role; // 'USER' / 'ADMIN'

	@Column(name = "line_notify_token", length = 255)
	private String lineNotifyToken; // LINE通知用トークン（任意）

	@Column(nullable = false)
	private boolean enabled = true; // 有効/無効

	// BAN 管理項目
	@Column(nullable = false)
	private boolean banned = false; // BAN状態

	@Column(name = "ban_reason")
	private String banReason; // BAN理由

	@Column(name = "banned_at")
	private LocalDateTime bannedAt; // BAN日時

	@Column(name = "banned_by_admin_id")
	private Long bannedByAdminId; // BAN実行管理者ID（users.id想定）
}
