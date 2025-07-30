CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    creator_id BIGINT REFERENCES users(id)
);

CREATE TABLE task_assignees (
    task_id BIGINT REFERENCES tasks(id),
    user_id BIGINT REFERENCES users(id)
);