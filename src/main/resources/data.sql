-- users
-- 初期ユーザー3名登録：出品者・購入者・管理者
INSERT INTO users (name, email, password, role, enabled)
VALUES
  (
    '出品者 A',
    'sellerA@example.com',
    '{noop}password',
    'USER',
    TRUE
  ),
  (
    '購入者 B',
    'xyz@example.com',
    '{noop}password',
    'USER',
    TRUE
  ),
  (
    '運営者 C',
    'adminC@example.com',
    '{noop}adminpass',
    'ADMIN',
    TRUE
  );

-- category
-- 商品カテゴリの初期データ
INSERT INTO category (name) VALUES
  ('本'),
  ('家電'),
  ('ファッション'),
  ('おもちゃ'),
  ('文房具');

-- item
-- 初期出品商品データ（sellerA が出品した商品）
INSERT INTO item (user_id, name, description, price, category_id, status)
VALUES
  (
    (SELECT id FROM users WHERE email = 'sellerA@example.com'),
    'Java プログラミング入門',
    '初心者向けの Java 入門書です。',
    1500.00,
    (SELECT id FROM category WHERE name = '本'),
    '出品中'
  ),
  (
    (SELECT id FROM users WHERE email = 'sellerA@example.com'),
    'ワイヤレスイヤホン',
    'ノイズキャンセリング機能付き。',
    800.00,
    (SELECT id FROM category WHERE name = '家電'),
    '出品中'
  );
