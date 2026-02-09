package com.example.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * users テーブルに対応するエンティティ
 */
@Entity
@Table(name = "users")
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

	public User() {
	}

	public User(Long id, String name, String email, String password, String role, String lineNotifyToken, boolean enabled,
			boolean banned, String banReason, LocalDateTime bannedAt, Long bannedByAdminId) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.lineNotifyToken = lineNotifyToken;
		this.enabled = enabled;
		this.banned = banned;
		this.banReason = banReason;
		this.bannedAt = bannedAt;
		this.bannedByAdminId = bannedByAdminId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLineNotifyToken() {
		return lineNotifyToken;
	}

	public void setLineNotifyToken(String lineNotifyToken) {
		this.lineNotifyToken = lineNotifyToken;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public String getBanReason() {
		return banReason;
	}

	public void setBanReason(String banReason) {
		this.banReason = banReason;
	}

	public LocalDateTime getBannedAt() {
		return bannedAt;
	}

	public void setBannedAt(LocalDateTime bannedAt) {
		this.bannedAt = bannedAt;
	}

	public Long getBannedByAdminId() {
		return bannedByAdminId;
	}

	public void setBannedByAdminId(Long bannedByAdminId) {
		this.bannedByAdminId = bannedByAdminId;
	}
}
