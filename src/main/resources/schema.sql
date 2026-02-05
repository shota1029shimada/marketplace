-- ========== CREATE ==========
-- IF NOT EXISTS を使用して、既にテーブルが存在する場合は作成をスキップ

-- ユーザー情報を管理するテーブル
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,                 -- 表示名
  email VARCHAR(255) NOT NULL UNIQUE,        -- ログイン ID
  password VARCHAR(255) NOT NULL,            -- ハッシュ化済パスワード
  role VARCHAR(20) NOT NULL,                 -- 'USER' / 'ADMIN'
  line_notify_token VARCHAR(255),            -- LINE 通知連携用
  enabled BOOLEAN NOT NULL DEFAULT TRUE,     -- 有効/無効

  -- BAN 管理項目
  banned BOOLEAN NOT NULL DEFAULT FALSE,     -- BAN 状態
  ban_reason TEXT,                           -- BAN 理由
  banned_at TIMESTAMP,                       -- BAN 日時
  banned_by_admin_id INT                     -- BAN 実行管理者
);

-- カテゴリ（ファッション、家具、家電など）
CREATE TABLE IF NOT EXISTS category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- 出品商品テーブル
CREATE TABLE IF NOT EXISTS item (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,                      -- 出品者 ID
  name VARCHAR(255) NOT NULL,                -- 商品名
  description TEXT,                          -- 商品説明
  price NUMERIC(10, 2) NOT NULL,             -- 価格
  category_id INT,                           -- カテゴリ
  status VARCHAR(20) DEFAULT '出品中',        -- 状態：出品中 / 売却済
  image_url TEXT,                            -- 画像 URL
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 登録日時

  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 注文情報テーブル
CREATE TABLE IF NOT EXISTS app_order (
  id SERIAL PRIMARY KEY,
  item_id INT NOT NULL,                      -- 対象商品
  buyer_id INT NOT NULL,                     -- 購入者
  price NUMERIC(10, 2) NOT NULL,             -- 価格
  status VARCHAR(20) DEFAULT '購入済',        -- 購入状態
  payment_intent_id VARCHAR(128),            -- Stripe PaymentIntent ID
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 購入日時

  FOREIGN KEY (item_id) REFERENCES item(id),
  FOREIGN KEY (buyer_id) REFERENCES users(id)
);

-- チャット（交渉・質問用）
CREATE TABLE IF NOT EXISTS chat (
  id SERIAL PRIMARY KEY,
  item_id INT NOT NULL,
  sender_id INT NOT NULL,                    -- 発言者
  message TEXT,                              -- 内容
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (item_id) REFERENCES item(id),
  FOREIGN KEY (sender_id) REFERENCES users(id)
);

-- お気に入り商品
CREATE TABLE IF NOT EXISTS favorite_item (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  item_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (user_id, item_id),                 -- 重複登録禁止

  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (item_id) REFERENCES item(id)
);

-- 購入後レビュー
CREATE TABLE IF NOT EXISTS review (
  id SERIAL PRIMARY KEY,
  order_id INT NOT NULL UNIQUE,              -- 注文 1 件にレビュー 1 件
  reviewer_id INT NOT NULL,
  seller_id INT NOT NULL,
  item_id INT NOT NULL,
  rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5), -- 星 1〜5
  comment TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (order_id) REFERENCES app_order(id),
  FOREIGN KEY (reviewer_id) REFERENCES users(id),
  FOREIGN KEY (seller_id) REFERENCES users(id),
  FOREIGN KEY (item_id) REFERENCES item(id)
);

-- 通報情報（ユーザー同士）
CREATE TABLE IF NOT EXISTS user_complaint (
  id SERIAL PRIMARY KEY,
  reported_user_id INT NOT NULL,             -- 通報されたユーザー
  reporter_user_id INT NOT NULL,             -- 通報者
  reason TEXT NOT NULL,                      -- 理由
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (reported_user_id) REFERENCES users(id),
  FOREIGN KEY (reporter_user_id) REFERENCES users(id)
);

-- ========== INDEX ==========
-- BAN 状態、カテゴリー、検索などの高速化目的
CREATE INDEX IF NOT EXISTS idx_users_banned
  ON users(banned);

CREATE INDEX IF NOT EXISTS idx_users_banned_by
  ON users(banned_by_admin_id);

CREATE INDEX IF NOT EXISTS idx_item_user_id
  ON item(user_id);

CREATE INDEX IF NOT EXISTS idx_item_category_id
  ON item(category_id);

CREATE INDEX IF NOT EXISTS idx_order_item_id
  ON app_order(item_id);

CREATE INDEX IF NOT EXISTS idx_order_buyer_id
  ON app_order(buyer_id);

CREATE UNIQUE INDEX IF NOT EXISTS ux_order_pi
  ON app_order(payment_intent_id);

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

CREATE INDEX IF NOT EXISTS idx_uc_reported
  ON user_complaint(reported_user_id);

CREATE INDEX IF NOT EXISTS idx_uc_reporter
  ON user_complaint(reporter_user_id);
