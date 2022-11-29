SET REFERENTIAL_INTEGRITY FALSE; /* 모든 제약조건 해제 */
truncate table transaction;
truncate table account;
truncate table users; /* 테이블 비우기 */
SET REFERENTIAL_INTEGRITY TRUE; /* 다시 제약조건 걸어주기 */