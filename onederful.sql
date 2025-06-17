CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 식별자',
    username   VARCHAR(100) NOT NULL UNIQUE COMMENT '아이디',
    email      VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일',
    password   VARCHAR(255) NOT NULL COMMENT '비밀번호',
    name       VARCHAR(100) NOT NULL COMMENT '이름',
    role       VARCHAR(50)  NOT NULL COMMENT '권한 (ENUM)',
    created_at DATETIME COMMENT '생성일자',
    updated_at DATETIME COMMENT '수정일자',
    deleted_at DATETIME COMMENT '삭제날짜',
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '삭제여부'
);

CREATE TABLE tasks
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '업무 식별자',
    title       VARCHAR(100) NOT NULL COMMENT '제목',
    description TEXT         NOT NULL COMMENT '설명',
    priority    VARCHAR(100) NOT NULL COMMENT '우선순위 식별자 (ENUM)',
    manager_id  BIGINT       NOT NULL COMMENT '담당자 식별자 (FK)',
    user_id     BIGINT       NOT NULL COMMENT '작성자 식별자 (FK)',
    status      VARCHAR(100) NOT NULL COMMENT '태스크 상태 (ENUM)',
    due_date    DATETIME COMMENT '마감일자',
    started_at  DATETIME COMMENT '시작일자',
    created_at  DATETIME COMMENT '생성일자',
    updated_at  DATETIME COMMENT '수정일자',
    deleted_at  DATETIME COMMENT '삭제날짜',
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '삭제여부',

    FOREIGN KEY (manager_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 식별자',
    content    TEXT    NOT NULL COMMENT '댓글 내용',
    task_id    BIGINT  NOT NULL COMMENT '업무 식별자 (FK)',
    user_id    BIGINT  NOT NULL COMMENT '작성자 식별자 (FK)',
    created_at DATETIME COMMENT '생성일자',
    updated_at DATETIME COMMENT '수정일자',
    deleted_at DATETIME COMMENT '삭제날짜',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제여부',

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

CREATE TABLE logs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '로그 식별자',
    user_id     BIGINT       NOT NULL COMMENT '사용자 식별자 (FK)',
    activity    VARCHAR(100) NOT NULL COMMENT '활동 유형',
    ip_address  VARCHAR(100) NOT NULL COMMENT 'ip 주소',
    method      VARCHAR(100) NOT NULL COMMENT '요청 메서드 (ENUM)',
    target_id   BIGINT       NOT NULL COMMENT '작업 대상 식별자',
    request_url VARCHAR(200) NOT NULL COMMENT '로그 요청 url',
    created_at  DATETIME COMMENT '생성일자',

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
