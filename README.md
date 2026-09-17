# Tsubuyaki-Post

Java / JSP / Servlet / PostgreSQL で開発した、シンプルなつぶやき投稿 Web アプリケーションです。

ユーザー登録、ログイン、投稿、一覧表示、削除といった機能を通して、JavaによるWebアプリケーション開発、セッション管理、JDBCを用いたデータベース連携を学ぶ目的で制作しました。

## アプリ概要

ユーザー登録後にログインし、短いメッセージを投稿できるWebアプリです。投稿はPostgreSQLに保存され、新しいものから一覧表示されます。ログインユーザーは自分の投稿だけを削除できます。

## 開発で重視した点

本プロジェクトでは、Javaを利用したWebアプリケーションの仕組みを理解するため、次の実装に重点を置きました。

- Servlet / JSPを利用した画面遷移
- ユーザー登録・ログイン・ログアウト処理
- `HttpSession`によるログイン状態の管理
- Logic・DAOによる処理の分離
- JDBCを利用したPostgreSQLとの連携
- 投稿の登録・取得・削除処理
- 入力値検証と、自分の投稿だけを削除できる認可処理

### 生成AIの利用について

本プロジェクトではJavaによる機能実装とバックエンドの学習に注力するため、HTML / CSSの配色やレイアウト案の作成に生成AIを活用しました。生成された内容は、そのまま使用するのではなく、コードと画面表示を確認しながら調整して組み込んでいます。

別途制作したRubyのポートフォリオでは、画面デザインを含めて一から実装しています。本プロジェクトは、Java、Servlet、JDBC、PostgreSQLを利用した処理の理解と実装力を示す作品として位置付けています。

## 画面イメージ

アプリを起動しなくても主要機能を確認できるよう、実際の操作画面を掲載しています。

### ログイン

登録済みのユーザー名とパスワードを使ってログインします。

![ログイン画面](images/login.png)

### ユーザー登録

ユーザー名、パスワード、確認用パスワードを入力し、確認処理を経て登録します。

![新規ユーザー登録画面](images/user-registration.png)

### 登録完了

登録処理が成功すると、登録したユーザー名を表示してログイン画面へ案内します。

![ユーザー登録完了画面](images/registration-complete.png)

### ログイン成功

認証に成功するとログインユーザー名を表示し、投稿一覧へ遷移できます。

![ログイン成功画面](images/login-success.png)

### 投稿・一覧表示・削除

つぶやきの投稿と新しい順での一覧表示に対応しています。ログインユーザー本人の投稿にだけ削除ボタンを表示します。

![つぶやき投稿・一覧画面](images/mutter-list.png)

## 主な機能

- ユーザーの新規登録
- ログイン / ログアウト
- セッションによるログイン状態管理
- つぶやきの投稿
- つぶやきの一覧表示（新しい順）
- 自分の投稿の削除
- PostgreSQLによるデータ永続化
- ユーザー名の重複、未入力、文字数、パスワード一致の確認

## 使用技術

| 分類 | 技術 |
| --- | --- |
| 言語 | Java 21 |
| フロントエンド | JSP / HTML / CSS |
| Web | Jakarta Servlet 6.0 |
| Webサーバー | Apache Tomcat 10 |
| データベース | PostgreSQL 12以降 |
| DB接続 | JDBC / PostgreSQL JDBC Driver |
| 開発環境 | Eclipse |
| バージョン管理 | Git / GitHub |

## アプリケーション構成

```text
ブラウザ
   ↓
JSP（画面表示）
   ↓
Servlet（リクエスト受付・画面遷移）
   ↓
Logic（アプリケーションロジック）
   ↓
DAO（SQL実行）
   ↓ JDBC
PostgreSQL（データ永続化）
```

## データベース設計

### usersテーブル

| カラム | 型 | 用途 |
| --- | --- | --- |
| `id` | `SERIAL` | 主キー、自動採番 |
| `name` | `VARCHAR(100)` | 必須、一意のユーザー名 |
| `pass` | `VARCHAR(255)` | 必須、パスワード |
| `created_at` | `TIMESTAMP` | 作成日時 |
| `updated_at` | `TIMESTAMP` | 更新日時 |

### muttersテーブル

| カラム | 型 | 用途 |
| --- | --- | --- |
| `id` | `SERIAL` | 主キー、自動採番 |
| `name` | `VARCHAR(100)` | 必須、投稿者名 |
| `text` | `TEXT` | 必須、投稿内容 |
| `created_at` | `TIMESTAMP` | 投稿日時 |

現在は`mutters.name`に投稿者名を保持しており、`users`との外部キーは設定していません。今後は`user_id`を追加して`users.id`を参照させ、データの整合性を高める予定です。

## セットアップ

### 必要な環境

- JDK 21
- Eclipse IDE for Enterprise Java and Web Developers
- Apache Tomcat 10
- PostgreSQL 12以降
- PostgreSQL JDBC Driver
- Git

### リポジトリの取得

```bash
git clone https://github.com/akiyosi1227/Tsubuyaki-Post.git
cd Tsubuyaki-Post
```

### データベースの作成

```bash
psql -U postgres -h localhost -c "CREATE DATABASE tsubuyak_post;"
psql -U postgres -h localhost -d tsubuyak_post -f schema.sql
```

### 接続設定例

```properties
db.url=jdbc:postgresql://localhost:5432/tsubuyak_post
db.user=postgres
db.password=your_password
```

接続情報を含むファイルはGitの管理対象に含めず、本番環境では環境変数やシークレット管理機能を使用してください。PostgreSQL JDBC Driverを追加し、EclipseでJDK 21とTomcat 10を設定した後、Tomcatからプロジェクトを起動します。

## H2 DatabaseからPostgreSQLへの移行

開発当初はH2 Databaseを使用していましたが、より実践的なデータベース運用と永続化を学ぶため、PostgreSQLへ移行しました。

- JDBCドライバと接続設定の変更
- PostgreSQL向けのテーブル定義と初期化SQLの作成
- `SERIAL`を利用した主キーの自動採番
- 検索・並び替え用インデックスの作成
- DAOが発行するSQLとデータ型の確認
- H2固有の設定に依存しない構成への整理

## セキュリティ

### 実装済み

- `PreparedStatement`によるSQLインジェクション対策
- `HttpSession`によるログイン状態管理
- 投稿IDとログインユーザー名を削除条件にした認可処理
- ユーザー登録時の入力値・重複チェック
- DB接続情報をGitの管理対象から除外する運用

### 今後の対応

- BCryptまたはArgon2によるパスワードのハッシュ化
- 出力エスケープによるXSS対策
- CSRFトークンの導入
- セッションID再生成、タイムアウト、Cookie属性の設定
- HTTPSによる通信の暗号化
- 環境変数やシークレット管理によるDB接続情報の保護
- ログイン試行回数の制限

> 現在のDB設計資料には学習用の平文パスワード例が含まれています。実運用では必ず安全なパスワードハッシュを保存する必要があります。

## 今後の改善

- パスワードのハッシュ化
- 外部キーによるユーザーと投稿の関連付け
- 投稿編集、検索、ページネーション
- レスポンシブデザインへの対応
- JUnitによる単体・結合テスト
- Maven / Gradleによる依存関係管理
- Dockerによる実行環境の再現
- CI/CDとクラウドへのデプロイ

## 学習したこと

- ServletとJSPを利用したJava Webアプリケーションの構造
- HTTPのGET / POSTと画面遷移
- `HttpSession`による認証状態の管理
- Servlet、Logic、DAO、Modelの役割分担
- JDBCと`PreparedStatement`によるCRUD処理
- PostgreSQLのテーブル設計、制約、インデックス、SQL
- H2 DatabaseからPostgreSQLへの移行
- 入力検証、認証、認可、機密情報管理の重要性
- Git / GitHubによるソースコード管理とドキュメント作成

## 関連ドキュメント

- [`DATABASE_SCHEMA.md`](DATABASE_SCHEMA.md) — データベース設計
- [`POSTGRESQL_SETUP.md`](POSTGRESQL_SETUP.md) — PostgreSQL導入・接続手順
- [`MIGRATION_COMPLETE.md`](MIGRATION_COMPLETE.md) — PostgreSQLへの移行内容
- [`schema.sql`](schema.sql) — テーブル・インデックス作成SQL

## 注意事項

本アプリケーションは学習目的で制作しています。本番環境で利用する場合は、パスワードのハッシュ化、CSRF / XSS対策、HTTPS、シークレット管理などを追加してください。

## 作者

- GitHub: [akiyosi1227](https://github.com/akiyosi1227)
