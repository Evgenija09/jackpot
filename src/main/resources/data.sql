INSERT IGNORE INTO jackpot (id, initial_pool_amount, current_pool_amount, contribution_strategy_type, contribution_rate, contribution_decay_rate, reward_strategy_type, reward_chance, reward_growth_rate, reward_pool_limit, version)
VALUES
('jackpot-fixed-fixed', 1000.0000, 1000.0000, 'FIXED', 0.10000000, NULL, 'FIXED', 0.50000000, NULL, NULL, 0),
('jackpot-variable-variable', 1000.0000, 1000.0000, 'VARIABLE', 0.10000000, 0.00001000, 'VARIABLE', 0.01000000, 0.00001000, 10000.0000, 0),
('jackpot-fixed-variable', 500.0000, 500.0000, 'FIXED', 0.05000000, NULL, 'VARIABLE', 0.01000000, 0.00001000, 5000.0000, 0),
('jackpot-always-win', 100.0000, 100.0000, 'FIXED', 0.10000000, NULL, 'VARIABLE', 0.01000000, 0.00001000, 100.0000, 0);
