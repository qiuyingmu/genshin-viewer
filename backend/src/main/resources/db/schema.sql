-- ============================================
-- Genshin Viewer Database Schema
-- ============================================

CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    nickname    VARCHAR(50)  DEFAULT '',
    avatar      VARCHAR(255) DEFAULT '',
    role        VARCHAR(20)  DEFAULT 'user',
    enabled     BOOLEAN      DEFAULT TRUE,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_operation_log (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       DEFAULT NULL,
    username    VARCHAR(50)  DEFAULT '',
    action      VARCHAR(50)  NOT NULL,
    resource    VARCHAR(50)  NOT NULL,
    resource_id VARCHAR(50)  DEFAULT '',
    detail      VARCHAR(500) DEFAULT '',
    ip          VARCHAR(50)  DEFAULT '',
    duration    BIGINT       DEFAULT 0,
    result      VARCHAR(20)  DEFAULT 'success',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genshin_artifact (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    set_id      VARCHAR(100) NOT NULL,
    slot        VARCHAR(20)  NOT NULL,
    stars       INT          DEFAULT 5,
    level       INT          DEFAULT 0,
    main_stat   VARCHAR(50)  NOT NULL,
    main_value  VARCHAR(50)  DEFAULT '',
    subs        TEXT         DEFAULT '[]',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genshin_character (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    char_id       VARCHAR(50)  NOT NULL,
    level         INT          DEFAULT 1,
    friendship    INT          DEFAULT 0,
    constellation INT          DEFAULT 0,
    talents       VARCHAR(200) DEFAULT '{}',
    weapon        VARCHAR(100) DEFAULT '',
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genshin_gacha (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    rarity      INT          DEFAULT 3,
    type        VARCHAR(20)  DEFAULT '角色',
    banner      VARCHAR(20)  DEFAULT 'character',
    gacha_time  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_artifact_user ON genshin_artifact(user_id);
CREATE INDEX IF NOT EXISTS idx_character_user ON genshin_character(user_id);
CREATE INDEX IF NOT EXISTS idx_gacha_user ON genshin_gacha(user_id);
CREATE INDEX IF NOT EXISTS idx_gacha_banner ON genshin_gacha(user_id, banner);
CREATE INDEX IF NOT EXISTS idx_oplog_user ON sys_operation_log(user_id);
CREATE INDEX IF NOT EXISTS idx_oplog_action ON sys_operation_log(action);
CREATE INDEX IF NOT EXISTS idx_oplog_time ON sys_operation_log(created_at);
