# Stripe設定ガイド

## 問題
購入ボタンを押すと「Stripe is not configured. Set STRIPE_SECRET_KEY / STRIPE_PUBLIC_KEY to enable payments.」というエラーが表示されます。

## 解決方法

### 1. Stripeテストキーの取得
1. [Stripeダッシュボード](https://dashboard.stripe.com/test/apikeys)にアクセス
2. テストモード（Test mode）で以下を取得：
   - **Publishable key** (pk_test_...)
   - **Secret key** (sk_test_...)

### 2. 環境変数の設定方法

#### 方法A: ターミナルで実行する場合
```bash
export STRIPE_PUBLIC_KEY="pk_test_あなたの公開キー"
export STRIPE_SECRET_KEY="sk_test_あなたのシークレットキー"
```

その後、アプリケーションを起動：
```bash
./mvnw spring-boot:run
```

#### 方法B: IDE（IntelliJ IDEA / Eclipse）で実行する場合
- **IntelliJ IDEA**: Run → Edit Configurations → Environment variables に追加
- **Eclipse**: Run → Run Configurations → Environment タブで追加

#### 方法C: シェルスクリプトを作成（推奨）
`run.sh` ファイルを作成して実行：

```bash
#!/bin/bash
export STRIPE_PUBLIC_KEY="pk_test_あなたの公開キー"
export STRIPE_SECRET_KEY="sk_test_あなたのシークレットキー"
./mvnw spring-boot:run
```

### 3. 開発環境用の一時的な設定（非推奨）
`application.properties`に直接設定することもできますが、セキュリティ上推奨されません：

```properties
stripe.public.key=pk_test_あなたの公開キー
stripe.api.secretKey=sk_test_あなたのシークレットキー
```

**注意**: この方法は開発環境のみで使用し、Gitにコミットしないでください。

## 確認
環境変数が正しく設定されているか確認：
```bash
echo $STRIPE_PUBLIC_KEY
echo $STRIPE_SECRET_KEY
```
