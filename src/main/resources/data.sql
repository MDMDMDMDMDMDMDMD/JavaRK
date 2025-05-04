-- Initial data setup for the application
-- This script will be executed automatically by Spring Boot when the application starts

-- Create a test user if it doesn't exist
INSERT INTO users (id, username, email, created_at, updated_at)
VALUES (1, 'testuser', 'test@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Create a sample todo item if it doesn't exist
INSERT INTO todos (id, title, description, completed, user_id, created_at, updated_at)
VALUES (1, 'Sample Todo', 'This is a sample todo item', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;