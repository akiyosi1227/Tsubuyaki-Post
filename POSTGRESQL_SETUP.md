# PostgreSQL 移行セットアップガイド

## 📝 概要
H2 データベースから PostgreSQL へ移行するための手順書です。

---

## ✅ ステップ 1: JDBC ドライバのインストール

### 方法 A: Maven 統合 (推奨)
プロジェクトで Maven を使用する場合、`pom.xml` に以下の依存関係を追加してください：

```xml
<!-- PostgreSQL JDBC Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.0</version>
</dependency>
```

**Tsubuyak-Post プロジェクト用**

### 方法 B: 手動インストール
1. PostgreSQL JDBC ドライバをダウンロード:
   - https://jdbc.postgresql.org/download.html
   - または: https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.0/postgresql-42.7.0.jar

2. ファイルを配置:
   ```
   src/main/webapp/WEB-INF/lib/postgresql-42.7.0.jar
   ```

3. Eclipse でプロジェクトを更新:
   - プロジェクト右クリック → Build Path → Configure Build Path
   - Add External JARs で postgresql-42.7.0.jar を追加

### 方法 C: Eclipse の UI から追加
1. Eclipse で `WEB-INF/lib` フォルダを右クリック
2. Import → File system
3. postgresql-42.7.0.jar を選択して追加

---

## ✅ ステップ 2: PostgreSQL データベース作成

### 1. PostgreSQL の起動確認
```bash
# macOS (Homebrew を使用している場合)
brew services start postgresql

# 接続テスト
psql -U postgres -h localhost
```

### 2. PostgreSQL データベースとテーブルの作成
```bash
# データベース作成
psql -U postgres -h localhost -c "CREATE DATABASE tsubuyak_post;"

# テーブル初期化スクリプト実行
psql -U postgres -h localhost -d tsubuyak_post -f schema.sql
```

または、PostgreSQL GUI ツール（pgAdmin など）を使用してください。

---

## ✅ ステップ 3: db.properties 設定確認

`src/main/resources/db.properties` が以下のように設定されていることを確認:

```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/tsubuyak_post
db.user=postgres
db.password=postgres
```

**注意**: 実運用環境では、パスワードを環境変数から読み込むなど、セキュリティを強化してください。

---

## ✅ ステップ 4: 接続テスト

Eclipse で Tomcat を起動し、以下にアクセス:
```
http://localhost:8080/Tsubuyak-Post/
```

接続エラーが発生した場合は、以下を確認:
- PostgreSQL が起動しているか
- db.properties の接続情報が正しいか
- JDBC ドライバが WEB-INF/lib に配置されているか
- ファイアウォール設定でポート 5432 が開いているか

---

## 🗑️ H2 ドライバの削除（オプション）

PostgreSQL への完全移行後、H2 を不要なら削除:
```bash
rm src/main/webapp/WEB-INF/lib/h2-2.4.240.jar
```

---

## 📞 トラブルシューティング

### エラー: "No suitable driver found for jdbc:postgresql://..."
→ PostgreSQL JDBC ドライバが WEB-INF/lib に配置されていません。ステップ 1 を再実行してください。

### エラー: "Connection refused"
→ PostgreSQL が起動していません。`brew services start postgresql` を実行してください。

### エラー: "FATAL: database "tsubuyak_post" does not exist"
→ データベースが作成されていません。ステップ 2-2 を実行してください。

---

## 📚 参考資料
- PostgreSQL JDBC ドライバ: https://jdbc.postgresql.org/
- PostgreSQL 公式ドキュメント: https://www.postgresql.org/docs/
