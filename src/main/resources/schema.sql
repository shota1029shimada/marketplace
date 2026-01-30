-- =========================================
-- schema.sql (PostgreSQL)
-- DDL：テーブル作成・制約・インデックス
-- =========================================

-- ========== CLEAN DROP (依存順) ==========
DROP TABLE IF EXISTS chat CASCADE;
DROP TABLE IF EXISTS favorite_item CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS app_order CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS user_complaint CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ========== CREATE ==========
-- ユーザー情報を管理するテーブル
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,                 -- 表示名
  email VARCHAR(255) NOT NULL UNIQUE,        -- ログインID
  password VARCHAR(255) NOT NULL,            -- ハッシュ化済パスワード
  role VARCHAR(20) NOT NULL,                 -- 'USER' / 'ADMIN'
  line_notify_token VARCHAR(255),            -- LINE 通知連携用
  enabled BOOLEAN NOT NULL DEFAULT TRUE,     -- 有効/無効

  -- BAN 管理項目
  banned BOOLEAN NOT NULL DEFAULT FALSE,     -- BAN 状態
  ban_reason TEXT,                           -- BAN 理由
  banned_at TIMESTAMP,                       -- BAN 日時
  banned_by_admin_id INT,                    -- BAN 実行管理者ID（users.id想定）

  CONSTRAINT chk_users_role CHECK (role IN ('USER', 'ADMIN'))
);

-- ※ banned_by_admin_id を users(id) にFKで繋ぐならここで追加（循環は起きない）
ALTER TABLE users
  ADD CONSTRAINT fk_users_banned_by_admin
  FOREIGN KEY (banned_by_admin_id) REFERENCES users(id);

-- カテゴリ（ファッション、家具、家電など）
CREATE TABLE category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- 出品商品テーブル
CREATE TABLE item (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,                      -- 出品者ID
  name VARCHAR(255) NOT NULL,                -- 商品名
  description TEXT,                          -- 商品説明
  price NUMERIC(10,2) NOT NULL,              -- 価格
  category_id INT,                           -- カテゴリ
  status VARCHAR(20) NOT NULL DEFAULT '出品中', -- 状態：出品中 / 売却済
  image_url TEXT,                            -- 画像URL
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 登録日時

  CONSTRAINT fk_item_user
    FOREIGN KEY (user_id) REFERENCES users(id),

  CONSTRAINT fk_item_category
    FOREIGN KEY (category_id) REFERENCES category(id),

  CONSTRAINT chk_item_status
    CHECK (status IN ('出品中', '売却済'))
);

-- 注文情報テーブル
CREATE TABLE app_order (
  id SERIAL PRIMARY KEY,
  item_id INT NOT NULL,                      -- 対象商品
  buyer_id INT NOT NULL,                     -- 購入者
  price NUMERIC(10,2) NOT NULL,              -- 購入価格
  status VARCHAR(20) NOT NULL DEFAULT '購入済', -- 購入状態
  payment_intent_id VARCHAR(128),            -- Stripe PaymentIntent ID
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 購入日時

  CONSTRAINT fk_order_item
    FOREIGN KEY (item_id) REFERENCES item(id),

  CONSTRAINT fk_order_buyer
    FOREIGN KEY (buyer_id) REFERENCES users(id),

  CONSTRAINT chk_order_status
    CHECK (status IN ('購入済', 'キャンセル', '返金', '保留'))
);

-- 1商品につき1回だけ購入される設計ならユニーク推奨（フリマ想定）
ALTER TABLE app_order
  ADD CONSTRAINT ux_app_order_item UNIQUE (item_id);

-- payment_intent_id は重複しない想定ならユニーク推奨（NULLは複数可）
CREATE UNIQUE INDEX IF NOT EXISTS ux_order_payment_intent_id
  ON app_order(payment_intent_id);

-- チャット（交渉・質問用）
CREATE TABLE chat (
  id SERIAL PRIMARY KEY,
  item_id INT NOT NULL,
  sender_id INT NOT NULL,                    -- 発言者
  message TEXT NOT NULL,                     -- 内容（空は基本NG推奨）
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_chat_item
    FOREIGN KEY (item_id) REFERENCES item(id),

  CONSTRAINT fk_chat_sender
    FOREIGN KEY (sender_id) REFERENCES users(id)
);

-- お気に入り商品
CREATE TABLE favorite_item (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  item_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT ux_favorite_user_item UNIQUE (user_id, item_id),

  CONSTRAINT fk_fav_user
    FOREIGN KEY (user_id) REFERENCES users(id),

  CONSTRAINT fk_fav_item
    FOREIGN KEY (item_id) REFERENCES item(id)
);

-- 購入後レビュー
CREATE TABLE review (
  id SERIAL PRIMARY KEY,
  order_id INT NOT NULL UNIQUE,              -- 注文1件にレビュー1件
  reviewer_id INT NOT NULL,                  -- レビュー投稿者（購入者）
  seller_id INT NOT NULL,                    -- 出品者
  item_id INT NOT NULL,                      -- 対象商品
  rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5), -- 星 1〜5
  comment TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_review_order
    FOREIGN KEY (order_id) REFERENCES app_order(id),

  CONSTRAINT fk_review_reviewer
    FOREIGN KEY (reviewer_id) REFERENCES users(id),

  CONSTRAINT fk_review_seller
    FOREIGN KEY (seller_id) REFERENCES users(id),

  CONSTRAINT fk_review_item
    FOREIGN KEY (item_id) REFERENCES item(id)
);

-- 通報情報（ユーザー同士）
CREATE TABLE user_complaint (
  id SERIAL PRIMARY KEY,
  reported_user_id INT NOT NULL,             -- 通報されたユーザー
  reporter_user_id INT NOT NULL,             -- 通報者
  reason TEXT NOT NULL,                      -- 理由
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_uc_reported
    FOREIGN KEY (reported_user_id) REFERENCES users(id),

  CONSTRAINT fk_uc_reporter
    FOREIGN KEY (reporter_user_id) REFERENCES users(id)
);

-- ========== INDEX ==========
-- BAN 状態、カテゴリー、検索などの高速化目的
CREATE INDEX IF NOT EXISTS idx_users_banned
  ON users(banned);

CREATE INDEX IF NOT EXISTS idx_users_banned_by_admin_id
  ON users(banned_by_admin_id);

CREATE INDEX IF NOT EXISTS idx_item_user_id
  ON item(user_id);

CREATE INDEX IF NOT EXISTS idx_item_category_id
  ON item(category_id);

CREATE INDEX IF NOT EXISTS idx_order_item_id
  ON app_order(item_id);

CREATE INDEX IF NOT EXISTS idx_order_buyer_id
  ON app_order(buyer_id);

CREATE INDEX IF NOT EXISTS idx_chat_item_id
  ON chat(item_id);

CREATE INDEX IF NOT EXISTS idx_chat_sender_id
  ON chat(sender_id);

CREATE INDEX IF NOT EXISTS idx_fav_user_id
  ON favorite_item(user_id);

CREATE INDEX IF NOT EXISTS idx_fav_item_id
  ON favorite_item(item_id);

CREATE INDEX IF NOT EXISTS idx_review_order_id
  ON review(order_id);

CREATE INDEX IF NOT EXISTS idx_uc_reported_user_id
  ON user_complaint(reported_user_id);

CREATE INDEX IF NOT EXISTS idx_uc_reporter_user_id
  ON user_complaint(reporter_user_id);
