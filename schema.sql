-- ============================================================
-- PostgreSQL Schema Initialization for Tsubuyak-Post Project
-- ============================================================

-- Drop existing tables if they exist (for clean reinstallation)
DROP TABLE IF EXISTS mutters CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Users Table
-- ============================================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add comment
COMMENT ON TABLE users IS 'ユーザー情報テーブル';
COMMENT ON COLUMN users.id IS 'ユーザーID（自動採番）';
COMMENT ON COLUMN users.name IS 'ユーザー名（ユニーク）';
COMMENT ON COLUMN users.pass IS 'パスワード（ハッシュ化推奨）';

-- ============================================================
-- Mutters Table
-- ============================================================
CREATE TABLE mutters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add comment
COMMENT ON TABLE mutters IS 'つぶやき（投稿）テーブル';
COMMENT ON COLUMN mutters.id IS '投稿ID（自動採番）';
COMMENT ON COLUMN mutters.name IS 'ユーザー名';
COMMENT ON COLUMN mutters.text IS '投稿内容';
COMMENT ON COLUMN mutters.created_at IS '投稿日時';

-- ============================================================
-- Indexes for Performance
-- ============================================================
CREATE INDEX idx_users_name ON users(name);
CREATE INDEX idx_mutters_name ON mutters(name);
CREATE INDEX idx_mutters_created_at ON mutters(created_at DESC);

-- ============================================================
-- Sample Data (Optional - for testing)
-- ============================================================
-- Insert sample users
INSERT INTO users (name, pass) VALUES 
    ('testuser', '1234'),
    ('alice', 'password123'),
    ('bob', 'password456');

-- Insert sample mutters
INSERT INTO mutters (name, text) VALUES 
    ('testuser', 'これが最初のつぶやきです！'),
    ('alice', 'PostgreSQLへの移行が完了しました'),
    ('bob', 'つぶやきアプリ、便利ですね');

-- ============================================================
-- Verification Queries
-- ============================================================
-- SELECT * FROM users;
-- SELECT * FROM mutters ORDER BY id DESC;
