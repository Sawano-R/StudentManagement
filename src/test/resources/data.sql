INSERT INTO students( name, name_kana,nickname, mail, resion,age,gender)
VALUES ('山田太郎','やまだたろう','タロちゃん','yamada.taro@example.com','東京都',34,'男'),
('佐藤 花子','さとう　はなこ','はな','sato.hanako@example.com','北海道',34,'女'),
('鈴木 一郎','すずき　いちろう',' いっちー','suzuki.ichiro@example.com','大阪府',45,'男'),
('高橋 美咲','タカハシ　サキ',' サキ','takahashi.saki@example.com','大阪府',45,'女'),
('中村 翼','ナカムラ　ツバサ',' つばさ','nakamura.tsubasa@example.com','愛知県',30,'その他');

INSERT INTO students_courses(id_students,course,start_day,end_day)
VALUES(1,'java',' 2025-05-12','2025-08-12'),
(1,'excel','2025-05-12','2025-07-12'),
(2,'java','2025-05-26','2025-09-26'),
(3,'java','2025-05-31','2025-08-31'),
(4,'excel','2025-06-10','2025-08-15'),
(5,'excel','2025-06-10','2025-08-15');

INSERT INTO courses_status(id_courses,id_students,status)
VALUES(1,1,'仮申込'),
(2,1,'本申込'),
(3,2,'受講中'),
(4,3,'受講終了'),
(5,4,'仮申込'),
(6,5,'受講中');
