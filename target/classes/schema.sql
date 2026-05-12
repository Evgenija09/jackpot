CREATE TABLE IF NOT EXISTS jackpot (
    id                          VARCHAR(36)    PRIMARY KEY,
    initial_pool_amount         DECIMAL(19,4)  NOT NULL,
    current_pool_amount         DECIMAL(19,4)  NOT NULL,
    contribution_strategy_type  VARCHAR(20)    NOT NULL,
    contribution_rate           DECIMAL(10,8)  NOT NULL,
    contribution_decay_rate     DECIMAL(10,8),
    reward_strategy_type        VARCHAR(20)    NOT NULL,
    reward_chance               DECIMAL(10,8)  NOT NULL,
    reward_growth_rate          DECIMAL(10,8),
    reward_pool_limit           DECIMAL(19,4),
    version                     INT            NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS jackpot_contribution (
    id                      BIGINT         AUTO_INCREMENT PRIMARY KEY,
    bet_id                  VARCHAR(36)    NOT NULL,
    user_id                 VARCHAR(36)    NOT NULL,
    jackpot_id              VARCHAR(36)    NOT NULL,
    stake_amount            DECIMAL(19,4)  NOT NULL,
    contribution_amount     DECIMAL(19,4)  NOT NULL,
    current_jackpot_amount  DECIMAL(19,4)  NOT NULL,
    created_at              DATETIME       NOT NULL,
    CONSTRAINT uk_contribution_bet_id UNIQUE (bet_id)
);

CREATE TABLE IF NOT EXISTS jackpot_reward (
    id              BIGINT         AUTO_INCREMENT PRIMARY KEY,
    bet_id          VARCHAR(36)    NOT NULL,
    user_id         VARCHAR(36)    NOT NULL,
    jackpot_id      VARCHAR(36)    NOT NULL,
    reward_amount   DECIMAL(19,4)  NOT NULL,
    created_at      DATETIME       NOT NULL,
    CONSTRAINT uk_reward_bet_id UNIQUE (bet_id)
);

CREATE INDEX IF NOT EXISTS idx_contribution_jackpot_id ON jackpot_contribution (jackpot_id);
CREATE INDEX IF NOT EXISTS idx_reward_jackpot_id ON jackpot_reward (jackpot_id);
CREATE INDEX IF NOT EXISTS idx_reward_bet_id ON jackpot_reward (bet_id);
