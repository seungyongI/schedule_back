-- INSERT INTO 'calendars' table
INSERT INTO calendars (cal_idx, c_holiday, c_theme) VALUES
(1, '2024-01-01', 'LIGHT'),
(2, '2024-01-02', 'DARK'),
(3, '2024-01-03', 'HYS'),
(4, '2024-01-04', 'JHI'),
(5, '2024-01-05', 'LIGHT');

-- user 테이블에 대한 더미 데이터 삽입
INSERT INTO user (u_idx, u_email, u_password, u_nickname, u_img, u_joinday, cal_idx, provider) VALUES
(1, 'johndoe@example.com', 'password123', 'JohnDoe', 'profile1.jpg', '2024-01-01 09:00:00', 1, 'LOCAL'),
(2, 'janesmith@example.com', 'password456', 'JaneSmith', 'profile2.jpg', '2024-01-02 10:00:00', 2, 'LOCAL'),
(3, 'mikejohnson@example.com', 'password789', 'MikeJohnson', 'profile3.jpg', '2024-01-03 11:00:00', 3, 'LOCAL'),
(4, 'alicewong@example.com', 'password101', 'AliceWong', 'profile4.jpg', '2024-01-04 12:00:00', 4, 'LOCAL'),
(5, 'davidlee@example.com', 'password202', 'DavidLee', 'profile5.jpg', '2024-01-05 13:00:00', 5, 'LOCAL');


-- INSERT INTO 'schedule' table
INSERT INTO schedule (s_idx, s_title, s_content, s_start, s_end, s_location, s_color, cal_idx) VALUES
(1, 'Meeting', 'Team meeting', '2024-01-10 10:00:00', '2024-01-10 11:00:00', 'Conference Room', 'BLUE', 1),
(2, 'Workout', 'Morning workout', '2024-01-11 06:00:00', '2024-01-11 07:00:00', 'Gym', 'GREEN', 2),
(3, 'Lunch with client', 'Business lunch', '2024-01-12 12:00:00', '2024-01-12 13:00:00', 'Restaurant', 'YELLOW', 3),
(4, 'Coding Session', 'Project coding', '2024-01-13 14:00:00', '2024-01-13 16:00:00', 'Office', 'VIOLET', 4),
(5, 'Daily walk', 'Evening walk', '2024-01-14 18:00:00', '2024-01-14 19:00:00', 'Park', 'ORANGE', 5),
-- 추가된 일정들
(6, 'Project Planning', 'Planning for new project', '2024-01-05 10:00:00', '2024-01-05 11:00:00', 'Office', 'PINK', 1),
(7, 'Client Call', 'Discussion with client', '2024-01-06 15:00:00', '2024-01-06 16:00:00', 'Office', 'GRAY', 1),
(8, 'Marketing Strategy', 'Team discussion on marketing', '2024-01-07 09:00:00', '2024-01-07 10:00:00', 'Office', 'BLUE', 1),
(9, 'Team Building', 'Team-building activities', '2024-01-08 14:00:00', '2024-01-10 17:00:00', 'Outdoor Park', 'GREEN', 1),
(10, 'Weekly Review', 'Review of the week', '2024-01-09 11:00:00', '2024-01-09 12:00:00', 'Conference Room', 'YELLOW', 1),
(11, 'Weekly Review2', 'Review of the week2', '2024-01-09 13:00:00', '2024-01-09 15:00:00', 'Conference Room2', 'YELLOW', 1);

-- INSERT INTO 'diary' table
INSERT INTO diary (d_idx, d_title, d_content, d_date, d_category, cal_idx) VALUES
(1, 'Day 1', 'Went for a walk', '2024-01-01', 'DAILY', 1),
(2, 'Day 2', 'Started new project', '2024-01-02', 'GROWTH', 2),
(3, 'Day 3', 'Workout session', '2024-01-03', 'EXERCISE', 3),
(4, 'Day 4', 'Visited new city', '2024-01-04', 'TRIP', 4),
(5, 'Day 5', 'Random thoughts', '2024-01-05', 'ETC', 5),
(6, 'Day 5-2', 'Random thoughts2', '2024-01-05', 'ETC', 5),
(7, 'Day 6', 'Meeting with team', '2024-01-06', 'DAILY', 1),
(8, 'Day 7', 'Read a book', '2024-01-07', 'GROWTH', 2),
(9, 'Day 8', 'Went hiking', '2024-01-08', 'EXERCISE', 3),
(10, 'Day 9', 'Learned a new recipe', '2024-01-09', 'TRIP', 4),
(11, 'Day 10', 'Random thoughts', '2024-01-10', 'ETC', 5),
(12, 'Day 11', 'Family gathering', '2024-01-11', 'DAILY', 1),
(13, 'Day 12', 'Team building activity', '2024-01-12', 'GROWTH', 2),
(14, 'Day 13', 'Attended a webinar', '2024-01-13', 'GROWTH', 3),
(15, 'Day 14', 'Worked out at gym', '2024-01-14', 'EXERCISE', 3),
(16, 'Day 15', 'Movie night with friends', '2024-01-15', 'TRIP', 4),
(17, 'Day 16', 'Random thoughts again', '2024-01-16', 'ETC', 5),
(18, 'Day 17', 'Exploring a new park', '2024-01-17', 'TRIP', 4),
(19, 'Day 18', 'Work meeting', '2024-01-18', 'DAILY', 1),
(20, 'Day 19', 'Started learning guitar', '2024-10-19', 'GROWTH', 2),
(21, 'Day 19', 'Started learning guitar2', '2024-10-19', 'GROWTH', 1),
(22, 'Day 19', 'Started learning guitar3', '2024-10-20', 'GROWTH', 1),
(23, 'Day 19', 'Started learning guitar4', '2024-10-25', 'GROWTH', 1),
(24, 'Day 19', 'Started learning guitar5', '2024-10-19', 'GROWTH', 1),
(25, 'Day 19', 'Started learning guitar', '2024-10-23', 'GROWTH', 1),
(26, 'Day 19', 'Started learning guitar2', '2024-10-23', 'GROWTH', 1),
(27, 'Day 19', 'Started learning guitar3', '2024-10-23', 'GROWTH', 1),
(28, 'Day 19', 'Started learning guitar4', '2024-10-23', 'GROWTH', 1),
(29, 'Day 19', 'Started learning guitar5', '2024-10-23', 'GROWTH', 1);
