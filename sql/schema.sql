-- create database matching_bot;
-- use hr;
use matching_bot;

-- 1. 외래키 제약 조건 해제
-- SET FOREIGN_KEY_CHECKS = 0;
-- DROP TABLE IF EXISTS `attached_items`;
-- DROP TABLE IF EXISTS `career`;
-- DROP TABLE IF EXISTS `community_category`;
-- DROP TABLE IF EXISTS `community_comment`;
-- DROP TABLE IF EXISTS `community_post`;
-- DROP TABLE IF EXISTS `company`;
-- DROP TABLE IF EXISTS `company_bookmark`;
-- DROP TABLE IF EXISTS `company_log`;
-- DROP TABLE IF EXISTS `job`;
-- DROP TABLE IF EXISTS `job_bookmark`;
-- DROP TABLE IF EXISTS `notification`;
-- DROP TABLE IF EXISTS `occupation`;
-- DROP TABLE IF EXISTS `resume`;
-- DROP TABLE IF EXISTS `resume_bookmark`;
-- DROP TABLE IF EXISTS `user`;
-- DROP TABLE IF EXISTS `attached_items`;
-- -- 3. 외래키 제약 조건 다시 활성화
-- SET FOREIGN_KEY_CHECKS = 1;

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
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS community_category;
DROP TABLE IF EXISTS occupation;

CREATE TABLE user (
    id BIGINT NOT NULL PRIMARY KEY auto_increment COMMENT '회원 ID',
    role VARCHAR(10) NOT NULL COMMENT '역할', -- 관리자=admin, 구직자=user
    name VARCHAR(10) NOT NULL COMMENT '이름',
    address VARCHAR(200) NOT NULL COMMENT '주소',
    email VARCHAR(50) NOT NULL UNIQUE COMMENT '이메일 (이메일형식)',
    password VARCHAR(100) NOT NULL COMMENT '비밀번호 (암호화저장)',
    gender enum('M', 'F') NOT NULL COMMENT '성별 (m/f)',
    birth DATE COMMENT '생년월일',
    phone varchar(11) COMMENT '연락처',
    agree_service enum('Y', 'N') COMMENT '서비스이용약관동의 (Y/N)',
    agree_privacy enum('Y', 'N') COMMENT '정보수집및이용동의 (Y/N)',
    agree_marketing enum('Y', 'N') COMMENT '마케팅정보수신동의 (Y/N)',
    agree_location enum('Y', 'N') COMMENT '위치기반서비스동의 (Y/N)',
    alert_bookmark enum('Y', 'N') COMMENT '관심기업채용공고알림 (Y/N)',
    alert_resume enum('Y', 'N') COMMENT '이력서열람알림 (Y/N)',
    status enum('Y', 'N') COMMENT '상태 (Y/N)',
    created_by VARCHAR(50) COMMENT '생성자',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);


CREATE TABLE occupation (
    id BIGINT PRIMARY KEY auto_increment,                -- 직무번호 (기본키)
    job_role_code CHAR(15) NOT NULL,      -- 직무코드
    job_role_name VARCHAR(15) NOT NULL,   -- 직무명
    job_type_code CHAR(15) NOT NULL,      -- 직종코드
    job_type_name VARCHAR(15) NOT NULL,   -- 직종명
    job_group_code CHAR(15) NOT NULL,     -- 직군코드
    job_group_name VARCHAR(15) NOT NULL   -- 직군명
);


-- 기업 회원 테이블 생성 --
create table company(
	id bigint not null auto_increment,
	email varchar(50) not null unique,
	password varchar(100) not null,
	role enum('COMPANY') not null,
	name varchar(50) not null,
	phone varchar(11),
	business_no bigint not null unique,
	address varchar(200) not null,
	industry varchar(50) not null,
	year_found year not null,
	headcount int not null,
	annual_revenue bigint not null,
	operating_income bigint not null,
	jobs_last_year int not null,
	agree_terms enum('Y', 'N') not null,
	agree_privacy enum('Y', 'N') not null,
	agree_finance enum('Y', 'N') not null,
	agree_marketing enum('Y', 'N') not null,
	agree_third_party enum('Y', 'N') not null,
	created_by varchar(50) not null,
	created_at datetime not null,
	updated_by varchar(50),
	updated_at datetime,
	PRIMARY KEY (id)
);

-- 채용 공고 테이블 생성 --
create table job(
	id bigint not null auto_increment,
	company_id bigint not null,
	occupation_id bigint not null,
	title varchar(100) not null,
	description varchar(500) not null,
	address varchar(100) not null,
	main_task varchar(255) not null,
	required_skills varchar(255) not null,
	required_traits varchar(255) not null,
	skill_keywords varchar(100),
	trait_keywords varchar(100),
	start_date date not null,
	end_date date not null,
	enroll_email varchar(50) not null,
	notice varchar(255),
	created_by varchar(50) not null,
	created_at datetime not null,
	updated_by varchar(50),
	updated_at datetime,
	primary key (id),
	FOREIGN KEY (company_id) REFERENCES company(id),
	FOREIGN KEY (occupation_id) REFERENCES occupation(id)
);

-- 알림 테이블 생성 --
create table notification(
	id bigint not null auto_increment,
	user_id bigint not null,
	title varchar(100) not null,
	content varchar(255) not null,
	status varchar(10) not null,
	primary key (id),
	foreign key (user_id) references user(id)
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
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    views INT NOT NULL DEFAULT 0,
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES community_category(id),
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 3. 댓글 테이블 생성
CREATE TABLE community_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES community_post(id),
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES user(id)
);


create table attached_items (
	id bigint auto_increment primary key comment '첨부파일 ID',
	reference_id bigint not null comment '참조 ID (company, resume, community_post)',
	type varchar(50) not null comment 'VL=기업평가, VS=평가요약, PO=기업포스터, RE=이력서사진, CO=게시글첨부',
	check (type in ('VL', 'VS', 'PO', 'RE', 'CO')),
	original_name varchar(255) not null comment '실제이름',
	system_name varchar(255) not null comment '시스템이름',
	path varchar(255) not null comment '경로',
	status enum('Y', 'N') not null comment '상태',
	created_by varchar(50) not null comment '생성장',
	created_at datetime default current_timestamp not null comment '생성일',
	updated_by varchar(50) comment '수정자',
	updated_at datetime on update current_timestamp comment '수정일',
	index idx_attached (type, reference_id)
);

select * from attached_items;

CREATE TABLE resume (
    id BIGINT PRIMARY KEY auto_increment,
    user_id BIGINT NOT NULL,
    occupation_id BIGINT NOT NULL,
    title VARCHAR(50) NOT NULL,
    skill_answer VARCHAR(255) NOT NULL,
    trait_answer VARCHAR(255) NOT NULL,
    skill_keywords VARCHAR(100) NOT NULL,
    talent_keywords VARCHAR(100) NOT NULL,
    created_by varchar(50) NOT NULL,
    created_at datetime default current_timestamp NOT NULL,
    updated_by varchar(50),
    updated_at datetime on update current_timestamp,
	FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (occupation_id) REFERENCES occupation(id)
);

-- tbl_career
CREATE TABLE career (
    id BIGINT PRIMARY KEY auto_increment,
    resume_id BIGINT NOT NULL,
    career_type VARCHAR(10)  CHECK (career_type IN ('new', 'exp')),
    company_name VARCHAR(50) NOT NULL,
    department_name VARCHAR(50) NOT NULL,
    position_title VARCHAR(50) NOT NULL ,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    salary BIGINT,
    career_summary VARCHAR(255),
    created_by varchar(50) not null,
    created_at datetime default current_timestamp not null,
    updated_by varchar(50),
    updated_at datetime on update current_timestamp,
	FOREIGN KEY (resume_id) REFERENCES resume(id)
);


create table resume_bookmark (
	id bigint primary key auto_increment comment '관심 이력서 ID',
	company_id bigint not null comment '기업 ID',
	resume_id bigint not null comment '이력서 ID',
	unique (company_id, resume_id),
	foreign key (company_id) references company(id),
	foreign key (resume_id) references resume(id)
);



-- 관심 기업 테이블
CREATE TABLE company_bookmark (
    id BIGINT PRIMARY KEY auto_increment COMMENT '관심 기업 ID',
    user_id BIGINT NOT NULL COMMENT '회원 ID',
    company_id BIGINT NOT NULL COMMENT '기업 ID',
    CONSTRAINT fk_cb_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_cb_company FOREIGN KEY (company_id) REFERENCES company(id)
);

-- 관심 채용 공고 테이블
CREATE TABLE job_bookmark (
    id BIGINT PRIMARY KEY auto_increment COMMENT '관심 채용 공고 ID',
    user_id BIGINT NOT NULL COMMENT '회원 ID',
    job_id BIGINT NOT NULL COMMENT '채용 공고 ID',
    CONSTRAINT fk_jb_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_jb_job FOREIGN KEY (job_id) REFERENCES job(id)
);

CREATE TABLE company_log (
    id BIGINT PRIMARY KEY auto_increment COMMENT '열람 ID',
    company_id BIGINT NOT NULL COMMENT '열람 기업 ID',
    log_date DATE NOT NULL COMMENT '열람 날짜',
    FOREIGN KEY (company_id) REFERENCES company(id)
);

commit;
show tables;

