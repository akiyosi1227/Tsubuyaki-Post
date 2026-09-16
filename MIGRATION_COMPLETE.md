# 🔄 H2 → PostgreSQL 移行 実装完了ガイド
# Tsubuyak-Post プロジェクト

## 📊 実装サマリー

H2 データベースから PostgreSQL への移行が完了しました。以下のファイルが作成・修正されました。

---

## ✅ 実装済みの変更一覧

### 1️⃣ **設定ファイル修正**

#### `src/main/resources/db.properties`
```properties
# PostgreSQL Database Configuration
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/tsubuyak_post
db.user=postgres
db.password=postgres
```

**変更内容:**
- H2 から PostgreSQL への接続情報に修正
- ドライバ: `org.postgresql.Driver`
- ポート: 5432（PostgreSQL デフォルト）

**⚠️ 注意**: 実運用では、パスワードを環境変数から読み込んでください。

---

### 2️⃣ **新規ファイル作成**

#### `POSTGRESQL_SETUP.md`
PostgreSQL 環境構築の詳細なセットアップガイド
- JDBC ドライバのインストール方法（Maven / 手動）
- PostgreSQL データベース作成手順
- 接続テスト方法
- トラブルシューティング

#### `schema.sql`
PostgreSQL スキーマ初期化スクリプト
```sql
-- Users テーブル（ユーザー認証用）
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Mutters テーブル（つぶやき投稿用）
CREATE TABLE mutters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**含まれるもの:**
- インデックス作成（パフォーマンス最適化）
- サンプルデータ挿入（テスト用）
- テーブルコメント（ドキュメンテーション）

---

### 3️⃣ **DAO クラス実装**

#### 新規: `src/main/java/dao/UserDAO.java`

ユーザー管理の完全な CRUD 操作

| メソッド | 機能 |
|---------|------|
| `authenticate(userName, password)` | ユーザー認証（DB照合） |
| `findByName(userName)` | ユーザー情報取得 |
| `create(userName, password)` | 新規ユーザー登録 |
| `updatePassword(userName, newPassword)` | パスワード変更 |
| `delete(userName)` | ユーザー削除 |

**例:**
```java
UserDAO userDAO = new UserDAO();
boolean isAuthenticated = userDAO.authenticate("alice", "password123");
```

---

### 4️⃣ **ビジネスロジック改善**

#### `src/main/java/model/LoginLogic.java` (改善)

**変更前:**
```java
public boolean excute(User user) {
    if (user.getPass().equals("1234")) { return true; }
    return false;
}
```

**変更後:**
```java
public boolean excute(User user) {
    return userDAO.authenticate(user.getName(), user.getPass());
}
```

**改善内容:**
- ✅ ハードコード化された認証から DB ベースの認証へ
- ✅ 複数ユーザー対応
- ✅ セキュリティ向上（DB から検証）

#### 新規: `src/main/java/model/UserRegistrationLogic.java`

ユーザー登録機能
```java
UserRegistrationLogic regLogic = new UserRegistrationLogic();
boolean success = regLogic.register("newuser", "password123");
```

**含まれる機能:**
- 新規ユーザー登録（重複チェック付き）
- パスワード変更（現在のパスワード検証付き）

---

## 🚀 実装後の次のステップ

### Step 1: JDBC ドライバのインストール
```bash
# 方法 A: Maven を使用（推奨）
# pom.xml に以下を追加:
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.0</version>
</dependency>

# 方法 B: 手動インストール
# postgresql-42.7.0.jar を WEB-INF/lib にコピー
```

### Step 2: PostgreSQL 起動
```bash
# macOS (Homebrew)
brew services start postgresql

# または GUI から起動
```

### Step 3: データベース初期化
```bash
# データベース作成
psql -U postgres -c "CREATE DATABASE tsubuyak_post;"

# スキーマ初期化
psql -U postgres -d tsubuyak_post -f schema.sql
```

### Step 4: Eclipse でプロジェクト更新
1. プロジェクト右クリック → `Clean Build`
2. プロジェクト右クリック → `Properties` → `Project Facets`
3. `Runtimes` タブで Tomcat が選択されているか確認

### Step 5: Tomcat で実行
1. Servers ビューで Tomcat を右クリック → `Start`
2. ブラウザで `http://localhost:8080/Tsubuyak-Post/` にアクセス
3. スキーマで作成したユーザーで ログイン（ユーザー名: `testuser`、パスワード: `1234`）

---

## 📋 実装後の確認項目

- [ ] PostgreSQL が起動している
- [ ] JDBC ドライバが WEB-INF/lib に配置されている
- [ ] `db.properties` に正しい接続情報が設定されている
- [ ] PostgreSQL に tsubuyak_post データベースが存在する
- [ ] schema.sql が実行されて users と mutters テーブルが作成されている
- [ ] Eclipse でプロジェクトのコンパイルエラーがない
- [ ] Tomcat で アプリケーションが起動できる
- [ ] ログインが正常に動作する

---

## 🔐 セキュリティに関する注意

### 実運用での推奨事項

1. **パスワードハッシュ化**
   ```java
   // BCrypt を使用する場合
   implementation 'org.springframework.security:spring-security-crypto:6.0.0'
   ```

2. **認証情報の環境変数化**
   ```java
   String password = System.getenv("DB_PASSWORD");
   ```

3. **SQL インジェクション対策**
   - ✅ 実装済み（PreparedStatement 使用）

4. **SSL/TLS による通信暗号化**
   ```properties
   db.url=jdbc:postgresql://localhost:5432/tsubuyak_post?ssl=true
   ```

---

## 🐛 トラブルシューティング

| エラー | 原因 | 解決方法 |
|------|------|--------|
| `No suitable driver found` | JDBC ドライバがない | `POSTGRESQL_SETUP.md` の Step 1 を実行 |
| `Connection refused` | PostgreSQL が起動していない | `brew services start postgresql` |
| `database "tsubuyak_post" does not exist` | DB が作成されていない | `POSTGRESQL_SETUP.md` の Step 2 を実行 |
| `ユーザーが見つかりません` | テーブルにデータがない | schema.sql を再実行（サンプルデータ挿入） |

---

## 📚 関連ドキュメント

- `POSTGRESQL_SETUP.md` - セットアップガイド
- `schema.sql` - データベーススキーマ定義
- `DBUtil.java` - DB 接続ユーティリティ（変更不要）
- `MuttersDAO.java` - つぶやき投稿 DAO（互換性確認済み）

---

## ✨ 実装の特徴

✅ **完全な互換性**: H2 と PostgreSQL の間で SQL クエリは互換  
✅ **拡張性**: UserDAO で ユーザー管理機能を追加可能  
✅ **セキュリティ**: PreparedStatement を使用して SQL インジェクション対策  
✅ **ドキュメント**: 詳細なコメントと設定ガイドを提供  
✅ **テスト用データ**: schema.sql にサンプルデータを含む  

---

**実装日**: 2026年9月15日  
**プロジェクト**: Tsubuyak-Post  
**対象バージョン**: Jakarta Servlet 6.0 / Java 21 / Tomcat 10
