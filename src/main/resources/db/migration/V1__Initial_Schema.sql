-- Create roles table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create user_roles join table
CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id),
    role_id BIGINT NOT NULL REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

-- Create portfolios table
CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create assets table
CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    current_price DECIMAL(19, 4) NOT NULL,
    price_updated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create market_data table
CREATE TABLE market_data (
    id UUID PRIMARY KEY,
    day_low DECIMAL(19, 4),
    day_high DECIMAL(19, 4),
    year_low DECIMAL(19, 4),
    year_high DECIMAL(19, 4),
    market_cap DECIMAL(19, 2),
    pe_ratio DECIMAL(10, 2),
    dividend_yield DECIMAL(10, 2),
    volume BIGINT,
    average_volume BIGINT,
    fifty_day_avg DECIMAL(19, 4),
    two_hundred_day_avg DECIMAL(19, 4),
    historical_data TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Add foreign key to assets table
ALTER TABLE assets
ADD COLUMN market_data_id UUID REFERENCES market_data(id);

-- Create investments table
CREATE TABLE investments (
    id UUID PRIMARY KEY,
    portfolio_id UUID NOT NULL REFERENCES portfolios(id),
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    initial_amount DECIMAL(19, 2) NOT NULL,
    quantity DECIMAL(19, 6) NOT NULL,
    purchase_price DECIMAL(19, 4) NOT NULL,
    purchase_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create transactions table
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    investment_id UUID NOT NULL REFERENCES investments(id),
    type VARCHAR(10) NOT NULL,
    quantity DECIMAL(19, 6) NOT NULL,
    price DECIMAL(19, 4) NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL,
    notes TEXT,
    transaction_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Create simulation_results table
CREATE TABLE simulation_results (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    initial_investment DECIMAL(19, 2) NOT NULL,
    monthly_contribution DECIMAL(19, 2) NOT NULL,
    annual_return_rate DECIMAL(10, 2) NOT NULL,
    investment_duration_years INTEGER NOT NULL,
    inflation_rate DECIMAL(10, 2) NOT NULL,
    tax_rate DECIMAL(10, 2) NOT NULL,
    final_amount DECIMAL(19, 2) NOT NULL,
    total_contributions DECIMAL(19, 2) NOT NULL,
    total_earnings DECIMAL(19, 2) NOT NULL,
    result_data TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Create indexes
CREATE INDEX idx_portfolios_user_id ON portfolios(user_id);
CREATE INDEX idx_investments_portfolio_id ON investments(portfolio_id);
CREATE INDEX idx_investments_asset_id ON investments(asset_id);
CREATE INDEX idx_transactions_investment_id ON transactions(investment_id);
CREATE INDEX idx_simulation_results_user_id ON simulation_results(user_id);

-- Insert default roles
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_ADMIN');
