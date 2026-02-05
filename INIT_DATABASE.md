# データベース初期化ガイド

## 変更内容
サーバーを再起動してもデータがリセットされないように、以下の変更を行いました：

### 1. schema.sqlの変更
- `DROP TABLE`文を削除
- `CREATE TABLE`を`CREATE TABLE IF NOT EXISTS`に変更
- これにより、既にテーブルが存在する場合は作成をスキップします

### 2. data.sqlの変更
- `INSERT`文に`ON CONFLICT DO NOTHING`を追加（ユーザーとカテゴリ）
- 商品データは`WHERE NOT EXISTS`を使用して重複チェック
- 既存データがあれば挿入をスキップします

### 3. application.propertiesの設定
- `spring.sql.init.mode=always`のまま（開発環境）
- スキーマとデータのSQLが安全になったため、`always`でも既存データは保持されます

## 動作確認

1. アプリケーションを起動
2. ユーザー登録や商品出品を行う
3. サーバーを停止
4. サーバーを再起動
5. データが保持されていることを確認

## 本番環境での設定

本番環境では、`application.properties`の`spring.sql.init.mode`を`never`に変更することを推奨します：

```properties
spring.sql.init.mode=never
```

これにより、SQLファイルは実行されず、既存のデータベーススキーマとデータがそのまま使用されます。
