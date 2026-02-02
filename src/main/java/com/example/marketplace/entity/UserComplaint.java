package com.example.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * ユーザー通報情報（user_complaint）
 */
@Entity
@Table(name = "user_complaint")
public class UserComplaint {

	/**
	 * 通報ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 通報されたユーザー
	 */
	@ManyToOne
	@JoinColumn(name = "reported_user_id", nullable = false)
	private User reportedUser;

	/**
	 * 通報者
	 */
	@ManyToOne
	@JoinColumn(name = "reporter_user_id", nullable = false)
	private User reporterUser;

	/**
	 * 通報理由
	 */
	@Column(nullable = false)
	private String reason;

	/**
	 * 通報日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public UserComplaint() {
	}

	public UserComplaint(Long id, User reportedUser, User reporterUser, String reason, LocalDateTime createdAt) {
		this.id = id;
		this.reportedUser = reportedUser;
		this.reporterUser = reporterUser;
		this.reason = reason;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getReportedUser() {
		return reportedUser;
	}

	public void setReportedUser(User reportedUser) {
		this.reportedUser = reportedUser;
	}

	public User getReporterUser() {
		return reporterUser;
	}

	public void setReporterUser(User reporterUser) {
		this.reporterUser = reporterUser;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
