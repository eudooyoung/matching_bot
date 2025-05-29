-- create database matching_bot;
-- use hr;
use matching_bot;

/*-- 1. 외래키 제약 조건 해제
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `attached_items`;
DROP TABLE IF EXISTS `career`;
DROP TABLE IF EXISTS `community_category`;
DROP TABLE IF EXISTS `community_comment`;
DROP TABLE IF EXISTS `community_post`;
DROP TABLE IF EXISTS `company`;
DROP TABLE IF EXISTS `company_bookmark`;
DROP TABLE IF EXISTS `company_log`;
DROP TABLE IF EXISTS `job`;
DROP TABLE IF EXISTS `job_bookmark`;
DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `occupation`;
DROP TABLE IF EXISTS `resume`;
DROP TABLE IF EXISTS `resume_bookmark`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `attached_items`;
DROP TABLE IF EXISTS `region`;
-- 3. 외래키 제약 조건 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;*/

DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS attached_items;
DROP TABLE IF EXISTS company_log;
DROP TABLE IF EXISTS resume_bookmark;
DROP TABLE IF EXISTS job_bookmark;
DROP TABLE IF EXISTS company_bookmark;
DROP TABLE IF EXISTS community_comment;
DROP TABLE IF EXISTS community_post;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS career;
DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS resume;
DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS community_category;
DROP TABLE IF EXISTS occupation;

CREATE TABLE member (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '회원 ID',
    role ENUM('MEMBER', 'ADMIN') NOT NULL COMMENT '역할', -- 관리자=admin, 구직자=member
    name VARCHAR(10) NOT NULL COMMENT '이름',
    address VARCHAR(200) NOT NULL COMMENT '주소',
    email VARCHAR(50) NOT NULL UNIQUE COMMENT '이메일 (이메일형식)',
    password VARCHAR(100) NOT NULL COMMENT '비밀번호 (암호화저장)',
    gender ENUM('M', 'F') NOT NULL COMMENT '성별 (M/F)',
    birth DATE COMMENT '생년월일',
    phone VARCHAR(20) COMMENT '연락처',
    agree_service ENUM('Y', 'N') NOT NULL CHECK (agree_service = 'Y') COMMENT '서비스이용약관동의 (Y/N)',
    agree_privacy ENUM('Y', 'N') NOT NULL CHECK (agree_privacy = 'Y') COMMENT '정보수집및이용동의 (Y/N)',
    agree_marketing ENUM('Y', 'N') COMMENT '마케팅정보수신동의 (Y/N)',
    agree_location ENUM('Y', 'N') COMMENT '위치기반서비스동의 (Y/N)',
    alert_bookmark ENUM('Y', 'N') COMMENT '관심기업채용공고알림 (Y/N)',
    alert_resume ENUM('Y', 'N') COMMENT '이력서열람알림 (Y/N)',
    status ENUM('Y', 'N') COMMENT '상태 (Y/N)',
    created_by VARCHAR(50) COMMENT '생성자',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);


CREATE TABLE occupation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,                -- 직무번호 (기본키)
    job_role_code BIGINT NOT NULL,      -- 직무코드
    job_role_name VARCHAR(15) NOT NULL,   -- 직무명
    job_type_code BIGINT NOT NULL,      -- 직종코드
    job_type_name VARCHAR(15) NOT NULL,   -- 직종명
    job_group_code BIGINT NOT NULL,     -- 직군코드
    job_group_name VARCHAR(15) NOT NULL   -- 직군명
);


-- 기업 회원 테이블 생성 --
/*create table company(
	id BIGINT NOT NULL AUTO_INCREMENT,
	email VARCHAR(50) NOT NULL unique,
	password VARCHAR(100) NOT NULL,
	role ENUM('COMPANY') NOT NULL,
	name VARCHAR(50) NOT NULL,
	phone VARCHAR(20),
	business_no BIGINT NOT NULL unique,
	address VARCHAR(200) NOT NULL,
	industry VARCHAR(50) NOT NULL,
	year_found year NOT NULL,
	headcount INT NOT NULL,
	annual_revenue BIGINT NOT NULL,
	operating_income BIGINT NOT NULL,
	jobs_last_year INT NOT NULL,
	agree_terms ENUM('Y', 'N') NOT NULL,
	agree_privacy ENUM('Y', 'N') NOT NULL,
	agree_finance ENUM('Y', 'N') NOT NULL,
	agree_marketing ENUM('Y', 'N') NOT NULL,
	agree_third_party ENUM('Y', 'N') NOT NULL,
	created_by VARCHAR(50) NOT NULL,
	created_at DATETIME NOT NULL,
	updated_by VARCHAR(50),
	updated_at DATETIME,
	PRIMARY KEY (id)
);*/

CREATE TABLE company (
    id BIGINT NOT NULL AUTO_INCREMENT,
    
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('COMPANY') NOT NULL DEFAULT 'COMPANY',

    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    business_no VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(200) NOT NULL,
    industry VARCHAR(50) NOT NULL,

    year_found year NOT NULL,
    headcount INT NOT NULL,
    annual_revenue INT NOT NULL,
    operating_income INT NOT NULL,
    jobs_last_year INT NOT NULL,

    agree_terms ENUM('Y', 'N') NOT NULL CHECK(agree_terms = 'Y'),
    agree_privacy ENUM('Y', 'N') NOT NULL CHECK(agree_privacy = 'Y'),
    agree_finance ENUM('Y', 'N') NOT NULL CHECK(agree_finance = 'Y'),
    agree_marketing ENUM('Y', 'N'),
    agree_third_party ENUM('Y', 'N'),

    report_status ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '평가 보고서 상태',

    created_by VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id)
);


-- 채용 공고 테이블 생성 --
CREATE TABLE job (
    id BIGINT NOT NULL AUTO_INCREMENT,
    company_id BIGINT NOT NULL,
    occupation_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    address VARCHAR(100) NOT NULL,
    main_task VARCHAR(500) NOT NULL,
    required_skills VARCHAR(500) NOT NULL,
    required_traits VARCHAR(500) NOT NULL,
    skill_keywords VARCHAR(100),
    trait_keywords VARCHAR(100),
    latitude DOUBLE NOT NULL DEFAULT 37.5665,        -- ✅ 위도 default 값 추가
    longitude DOUBLE NOT NULL DEFAULT 126.9780,       -- ✅ 경도 default 값 추가
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    enroll_email VARCHAR(50) NOT NULL,
    notice VARCHAR(255),
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES company(id),
    FOREIGN KEY (occupation_id) REFERENCES occupation(id)
);


-- 알림 테이블 생성 --
create table notification(
	id BIGINT NOT NULL AUTO_INCREMENT,
	member_id BIGINT NOT NULL,
	title VARCHAR(100) NOT NULL,
	content VARCHAR(255) NOT NULL,
	status VARCHAR(10) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (member_id) REFERENCES member(id)
);


-- 1. 카테고리 테이블 생성
CREATE TABLE community_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- 2. 게시글 테이블 생성
CREATE TABLE community_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    views INT NOT NULL DEFAULT 0,
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES community_category(id),
    CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(id)
);

-- 3. 댓글 테이블 생성
CREATE TABLE community_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES community_post(id),
    CONSTRAINT fk_comment_member FOREIGN KEY (member_id) REFERENCES member(id)
);


create table attached_items (
	id BIGINT AUTO_INCREMENT PRIMARY KEY comment '첨부파일 ID',
	reference_id BIGINT NOT NULL comment '참조 ID (company, resume, community_post)',
	type VARCHAR(50) NOT NULL comment 'VL=기업평가, VS=평가요약, PO=기업포스터, RE=이력서사진, CO=게시글첨부',
	check (type in ('VL', 'VS', 'PO', 'RE', 'CO')),
	original_name VARCHAR(255) NOT NULL comment '실제이름',
	system_name VARCHAR(255) NOT NULL comment '시스템이름',
	path VARCHAR(255) NOT NULL comment '경로',
	status ENUM('Y', 'N') NOT NULL comment '상태',
	created_by VARCHAR(50) NOT NULL comment '생성자',
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL comment '생성일',
	updated_by VARCHAR(50) comment '수정자',
	updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP comment '수정일',
	index idx_attached (type, reference_id)
);

select * from attached_items;

CREATE TABLE resume (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    occupation_id BIGINT NOT NULL,
    title VARCHAR(50) NOT NULL,
    skill_answer VARCHAR(255) NOT NULL,
    trait_answer VARCHAR(255),
    skill_keywords VARCHAR(100),
    talent_keywords VARCHAR(100),
    keywords_status ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '키워드 추출 상태',
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (occupation_id) REFERENCES occupation(id)
);

-- tbl_career
CREATE TABLE career (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    career_type VARCHAR(10)  CHECK (career_type IN ('new', 'exp')),
    company_name VARCHAR(50) NOT NULL,
    department_name VARCHAR(50) NOT NULL,
    position_title VARCHAR(50) NOT NULL ,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    salary BIGINT,
    career_summary VARCHAR(255),
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (resume_id) REFERENCES resume(id)
);


create table resume_bookmark (
	id BIGINT PRIMARY KEY AUTO_INCREMENT comment '관심 이력서 ID',
	company_id BIGINT NOT NULL comment '기업 ID',
	resume_id BIGINT NOT NULL comment '이력서 ID',
	UNIQUE (company_id, resume_id),
	FOREIGN KEY (company_id) REFERENCES company(id),
	FOREIGN KEY (resume_id) REFERENCES resume(id)
);



-- 관심 기업 테이블
CREATE TABLE company_bookmark (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '관심 기업 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    company_id BIGINT NOT NULL COMMENT '기업 ID',
    CONSTRAINT fk_cb_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_cb_company FOREIGN KEY (company_id) REFERENCES company(id)
);

-- 관심 채용 공고 테이블
CREATE TABLE job_bookmark (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '관심 채용 공고 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    job_id BIGINT NOT NULL COMMENT '채용 공고 ID',
    CONSTRAINT fk_jb_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_jb_job FOREIGN KEY (job_id) REFERENCES job(id)
);

CREATE TABLE company_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '열람 ID',
    company_id BIGINT NOT NULL COMMENT '열람 기업 ID',
    log_date DATE NOT NULL COMMENT '열람 날짜',
    FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE refresh_token (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  role ENUM('MEMBER', 'COMPANY', 'ADMIN') NOT NULL,
  refresh_token TEXT NOT NULL,
  issued_at DATETIME NOT NULL,
  expired_at DATETIME NOT NULL,
  UNIQUE (email, role)
);

CREATE TABLE region (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '지역번호',
    region_code BIGINT NOT NULL UNIQUE COMMENT '시/군/구 코드',
    region_name VARCHAR(15) NOT NULL COMMENT '시/군/구 명',
    region_type_code BIGINT NOT NULL COMMENT '시/도 코드',
    region_type_name VARCHAR(15) NOT NULL COMMENT '시/도 명'
);


commit;
show tables;

