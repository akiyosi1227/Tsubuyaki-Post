# Tsubuyaki-Post PostgreSQL データベーススキーマ

## 📊 概要

Tsubuyaki-Post（つぶやきポスト）アプリケーションで使用するPostgreSQLデータベースの設計ドキュメントです。

---

## 🗂️ テーブル一覧

### 1. **users テーブル** - ユーザー認証・管理
### 2. **mutters テーブル** - つぶやき投稿

---

## 📋 テーブル詳細設計

### 1️⃣ users テーブル

**用途**: ユーザー認証情報の保存（ログイン・ユーザー管理）

#### DDL（テーブル定義）
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### カラム仕様

| カラム名 | 型 | 制約 | 説明 |
|---------|-----|------|------|
| `id` | SERIAL | PRIMARY KEY | ユーザーID（自動採番） |
| `name` | VARCHAR(100) | NOT NULL, UNIQUE | ユーザー名（一意） |
| `pass` | VARCHAR(255) | NOT NULL | パスワード（ハッシュ化推奨） |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 作成日時 |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新日時 |

#### インデックス
```sql
CREATE INDEX idx_users_name ON users(name);
```
- ユーザー名での検索を高速化

#### サンプルデータ
```sql
INSERT INTO users (name, pass) VALUES 
    ('testuser', '1234'),
    ('alice', 'password123'),
    ('bob', 'password456');
```

**⚠️ セキュリティ注意**: 実運用環境では、パスワードは必ずハッシュ化して保存してください。

---

### 2️⃣ mutters テーブル

**用途**: ユーザーのつぶやき投稿データの保存

#### DDL（テーブル定義）
```sql
CREATE TABLE mutters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### カラム仕様

| カラム名 | 型 | 制約 | 説明 |
|---------|-----|------|------|
| `id` | SERIAL | PRIMARY KEY | 投稿ID（自動採番） |
| `name` | VARCHAR(100) | NOT NULL | ユーザー名 |
| `text` | TEXT | NOT NULL | 投稿内容 |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 投稿日時 |

#### インデックス
```sql
CREATE INDEX idx_mutters_name ON mutters(name);
CREATE INDEX idx_mutters_created_at ON mutters(created_at DESC);
```
- ユーザー名での検索を高速化
- 投稿日時での検索を高速化（新しい投稿を優先表示）

#### サンプルデータ
```sql
INSERT INTO mutters (name, text) VALUES 
    ('testuser', 'これが最初のつぶやきです！'),
    ('alice', 'PostgreSQLへの移行が完了しました'),
    ('bob', 'つぶやきアプリ、便利ですね');
```

---

## 🔧 セットアップ手順

### ステップ 1: PostgreSQL サーバーの起動

**macOS (Homebrew)**
```bash
brew services start postgresql
```

**確認:**
```bash
psql -U postgres -h localhost
```

### ステップ 2: データベース作成

```bash
psql -U postgres -h localhost -c "CREATE DATABASE tsubuyak_post;"
```

### ステップ 3: スキーマ初期化（テーブル作成）

```bash
psql -U postgres -h localhost -d tsubuyak_post -f schema.sql
```

### ステップ 4: 確認クエリ

テーブルが正常に作成されたか確認：

```bash
# テーブル一覧表示
psql -U postgres -d tsubuyak_post -c "\dt"

# users テーブルの内容確認
psql -U postgres -d tsubuyak_post -c "SELECT * FROM users;"

# mutters テーブルの内容確認
psql -U postgres -d tsubuyak_post -c "SELECT * FROM mutters ORDER BY id DESC;"
```

---

## 🔍 主要なクエリ例

### ユーザー操作

#### ユーザー認証（ログイン）
```sql
SELECT id, name, pass FROM users WHERE name = 'testuser';
```

#### 新規ユーザー登録
```sql
INSERT INTO users(name, pass) VALUES('newuser', 'hashed_password');
```

#### パスワード更新
```sql
UPDATE users SET pass = 'new_hashed_password', updated_at = CURRENT_TIMESTAMP 
WHERE name = 'testuser';
```

#### ユーザー削除
```sql
DELETE FROM users WHERE name = 'testuser';
```

---

### つぶやき操作

#### 全投稿の取得（新しい順）
```sql
SELECT id, name, text, created_at FROM mutters ORDER BY id DESC;
```

#### 新規つぶやき投稿
```sql
INSERT INTO mutters(name, text) VALUES('alice', 'つぶやき内容');
```

#### 特定ユーザーのつぶやき取得
```sql
SELECT id, name, text, created_at FROM mutters WHERE name = 'alice' ORDER BY created_at DESC;
```

#### つぶやき件数の集計
```sql
SELECT COUNT(*) as total_mutters FROM mutters;
SELECT name, COUNT(*) as count FROM mutters GROUP BY name ORDER BY count DESC;
```

---

## 🔐 セキュリティに関する推奨事項

### 1. パスワードハッシュ化
実運用環境では、平文でパスワードを保存してはいけません。

**Java での実装例（BCrypt）:**
```java
// dependencies に追加
// implementation 'org.mindrot:jbcrypt:0.4'

import org.mindrot.bcrypt.BCrypt;

// パスワード登録時（ハッシュ化）
String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
userDAO.create(userName, hashedPassword);

// ログイン時（検証）
String storedHash = userDAO.findByName(userName).getPass();
boolean isPasswordCorrect = BCrypt.checkpw(password, storedHash);
```

### 2. SQL インジェクション対策
✅ **実装済み**: PreparedStatement を使用してパラメータ化クエリを実装

### 3. 接続認証情報の保護
```java
// 環境変数から取得
String password = System.getenv("DB_PASSWORD");
```

### 4. SSL/TLS による通信暗号化
```properties
db.url=jdbc:postgresql://localhost:5432/tsubuyak_post?ssl=true&sslmode=require
```

### 5. アカウントロック機能（将来の拡張）
```sql
-- users テーブルに以下を追加
ALTER TABLE users ADD COLUMN is_locked BOOLEAN DEFAULT FALSE;
ALTER TABLE users ADD COLUMN failed_login_attempts INT DEFAULT 0;
ALTER TABLE users ADD COLUMN last_login_at TIMESTAMP;
```

---

## 📈 パフォーマンス最適化

### 作成済みインデックス

```sql
-- ユーザー名での検索を高速化
CREATE INDEX idx_users_name ON users(name);

-- つぶやき投稿の検索を高速化
CREATE INDEX idx_mutters_name ON mutters(name);

-- 新しい投稿を優先表示する際の検索を高速化
CREATE INDEX idx_mutters_created_at ON mutters(created_at DESC);
```

### 今後追加すべきインデックス（投稿が増えた場合）

```sql
-- ユーザー名とタイムスタンプの複合インデックス
CREATE INDEX idx_mutters_user_time ON mutters(name, created_at DESC);

-- パスワード検証時の高速化（オプション）
CREATE INDEX idx_users_name_pass ON users(name, pass);
```

---

## 🔄 リレーション（関連設定）

現在、`mutters` テーブルは `users` テーブルとの直接的なForeign Key制約がありません。

### 将来の拡張：外部キー制約の追加（推奨）

```sql
-- mutters テーブルを修正してユーザーIDを追加
ALTER TABLE mutters ADD COLUMN user_id INT;

-- 外部キー制約を追加
ALTER TABLE mutters 
ADD CONSTRAINT fk_mutters_user_id 
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- インデックス追加
CREATE INDEX idx_mutters_user_id ON mutters(user_id);
```

このようにすることで：
- ユーザー削除時に関連投稿も自動削除（ON DELETE CASCADE）
- ユーザーIDによる検索が高速化
- データ整合性が向上

---

## 📊 ER 図（エンティティ・リレーションシップ図）

```
┌─────────────┐                    ┌──────────────┐
│    users    │                    │   mutters    │
├─────────────┤                    ├──────────────┤
│ id (PK)     │◄─────── 1:N ───────┤ id (PK)      │
│ name (UQ)   │  (user_id)         │ user_id (FK) │
│ pass        │                    │ name         │
│ created_at  │                    │ text         │
│ updated_at  │                    │ created_at   │
└─────────────┘                    └──────────────┘
```

---

## 📝 バージョン履歴

| 日付 | バージョン | 変更内容 |
|------|-----------|--------|
| 2026-09-16 | 1.0 | 初版作成 - users, mutters テーブル設計 |

---

## 🆘 トラブルシューティング

### Q1: 「FATAL: database "tsubuyak_post" does not exist」

**A:** データベースが作成されていません。以下を実行してください：
```bash
psql -U postgres -h localhost -c "CREATE DATABASE tsubuyak_post;"
```

### Q2: 「ERROR: duplicate key value violates unique constraint "users_name_key"」

**A:** 同じユーザー名が既に存在します。別のユーザー名を使用してください。

### Q3: 投稿の日時が UTC になっている

**A:** PostgreSQL の タイムゾーンを確認・設定してください：
```sql
-- 現在のタイムゾーン確認
SHOW TIMEZONE;

-- タイムゾーンを日本時間に設定
SET TIMEZONE = 'Asia/Tokyo';
```

---

## 📚 参考資料

- [PostgreSQL 公式ドキュメント](https://www.postgresql.org/docs/)
- [JDBC ドライバ](https://jdbc.postgresql.org/)
- [SQLインジェクション対策](https://owasp.org/www-community/attacks/SQL_Injection)
- [BCrypt パスワードハッシュ化](https://www.mindrot.org/projects/jbcrypt/)

---

**最終更新**: 2026年9月16日  
**プロジェクト**: Tsubuyaki-Post  
**対応バージョン**: PostgreSQL 12.0+, Jakarta Servlet 6.0, Java 21, Tomcat 10
