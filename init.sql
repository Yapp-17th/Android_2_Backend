insert into address(id, city, created_at, updated_at)
values(1, 'JONGNO_GU', now(), now()),
       (2, 'JUNG_GU', now(), now()),
       (3, 'YONGSAN_GU', now(), now()),
       (4, 'SEONGDONG_GU', now(), now()),
       (5, 'GWANGJIN_GU', now(), now()),
       (6, 'DONGDAEMUN_GU', now(), now()),
       (7, 'JUNGNANG_GU', now(), now()),
       (8, 'SEONGBUK_GU', now(), now()),
       (9, 'GANGBUK_GU', now(), now()),
       (10, 'DOBONG_GU', now(), now()),
       (11, 'NOWON_GU', now(), now()),
       (12, 'EUNPYEONG_GU', now(), now()),
       (13, 'SEODAEMUN_GU', now(), now()),
       (14, 'MAPO_GU', now(), now()),
       (15, 'YANGCHEON_GU', now(), now()),
       (16, 'GANGSEO_GU', now(), now()),
       (17, 'GURO_GU', now(), now()),
       (18, 'GEUMCHEON_GU', now(), now()),
       (19, 'YEONGDEUNGPO_GU', now(), now()),
       (20, 'DONGJAK_GU', now(), now()),
       (21, 'GWANAK_GU', now(), now()),
       (22, 'SEOCHO_GU', now(), now()),
       (23, 'GANGNAM_GU', now(), now()),
       (24, 'SONGPA_GU', now(), now()),
       (25, 'GANGDONG_GU', now(), now());

insert into category(id, exercise, created_at, updated_at)
values(1, 'SOCCER', now(), now()),
       (2, 'BASKETBALL', now(), now()),
       (3, 'TENNIS', now(), now()),
       (4, 'RUNNING', now(), now()),
       (5, 'HIKING', now(), now()),
       (6, 'BICYCLE', now(), now()),
       (7, 'CLIMBING', now(), now()),
       (8, 'VOLLYBALL', now(), now()),
       (9, 'BASEBALL', now(), now()),
       (10, 'FUTSAL', now(), now()),
       (11, 'BADMINTON', now(), now()),
       (12, 'RUGBY', now(), now()),
       (13, 'BOXING', now(), now()),
       (14, 'ICE_HOKEY', now(), now()),
       (15, 'GOLF', now(), now()),
       (16, 'OTHERS', now(), now());

insert into tag(id, name, created_at, updated_at)
values(1, 'TIGHT_USER', now(), now()),
       (2, 'FUN_USER', now(), now());

insert into user(id, access_token, oauth_id, username, nickname, email, intro, status, address_id, suspended_day, created_at, updated_at) values(-9, "explanet_bot", "explanet_bot", "운동플래닛", "운동플래닛", "ex_planet@gmail.com", "운동플래닛", "ACTIVE", 1, 0, now(), now());
