insert into pay_users(user_id, deposit, prize, created_at, updated_at) values (1L, 0, 0, now(), now());
insert into pay_users(user_id, deposit, prize, created_at, updated_at) values (2L, 3000, 5000, now(), now());
insert into pay_users(user_id, deposit, prize, created_at, updated_at) values (3L, 10000, 2500, now(), now());

insert into PRIZE_HISTORY
(PRIZE_HISTORY_ID, CREATED_AT, UPDATED_AT, BANK_CODE, DEPOSITOR, NUMBER, AMOUNT, CHALLENGE_ID, PRIZE_HISTORY_TYPE, USER_ID)
values
    ('f8cc227d-029c-4843-9682-f037297abc09', now(), now(), null, null, null, 400, 1, 'IN', 2);

insert into PRIZE_HISTORY
(PRIZE_HISTORY_ID, CREATED_AT, UPDATED_AT, BANK_CODE, DEPOSITOR, NUMBER, AMOUNT, CHALLENGE_ID, PRIZE_HISTORY_TYPE, USER_ID)
values
    ('f8cc227d-029c-4843-9682-f037297abc10', now(), now(), null, null, null, 400, 1, 'IN', 3);