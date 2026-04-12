# JavaJolt — Entity Relationship Diagram

```mermaid
erDiagram
    users {
        bigint id PK
        varchar username UK
        varchar email UK
        varchar password
        boolean is_deleted
        timestamp created_at
    }
    roles {
        bigint id PK
        varchar name
    }
    user_roles {
        bigint user_id FK
        bigint role_id FK
    }
    courses {
        bigint id PK
        varchar name
        varchar programming_language
        varchar difficulty
        boolean is_deleted
        timestamp created_at
    }
    lessons {
        bigint id PK
        bigint course_id FK
        varchar title
        int order_index
        boolean is_deleted
        timestamp created_at
    }
    exercises {
        bigint id PK
        bigint lesson_id FK
        varchar title
        varchar difficulty
        boolean is_deleted
        timestamp created_at
    }
    progress {
        bigint id PK
        bigint user_id FK
        bigint exercise_id FK
        boolean completed
        int score
        timestamp created_at
        timestamp completed_at
    }

    users ||--o{ user_roles : "has"
    roles ||--o{ user_roles : "assigned to"
    courses ||--o{ lessons : "contains"
    lessons ||--o{ exercises : "contains"
    users ||--o{ progress : "tracks"
    exercises ||--o{ progress : "tracked in"
```

## Relationships

- **users ↔ roles** — Many-to-many via `user_roles` join table. A user can have multiple roles (USER, ADMIN). A role can belong to many users.
- **courses → lessons** — One-to-many. A course contains many lessons ordered by `order_index`.
- **lessons → exercises** — One-to-many. A lesson contains many exercises.
- **users ↔ exercises** — Many-to-many via `progress`. Each progress row tracks one user's completion of one exercise, including score and timestamp.

## Design Decisions

- All entities except `roles` and `user_roles` carry an `is_deleted` flag for soft delete — data is never permanently removed.
- `password` stores a BCrypt hash — plain text is never persisted.
- `isAdmin` is not stored as a column on `users` — it is derived at runtime from the roles collection.
- Surrogate keys (`bigint id`) used on all tables for stable, fast joins.
