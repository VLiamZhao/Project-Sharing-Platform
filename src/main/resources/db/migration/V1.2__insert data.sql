insert into role (id, role_name, allowed_resource, allowed_read, allowed_create, allowed_update, allowed_delete) values
(1, 'Admin', '/auth,/students, /enterprises, /resumes, /projects', 'Y', 'Y', 'Y', 'Y'),
(2, 'Student', '/auth,/students', 'Y', 'Y', 'Y', 'N'),
(3, 'Company', '/enterprises,/auth', 'Y', 'Y', 'Y', 'N')
;
commit;

insert into users (id, role_id, username, password, email, user_type, phone_num) values
(1, 2, 'Tommy', '25f9e794323b453885f5181f1b624d0b', 'tommy@gmail.com', 'student', '22233336666'),
(2, 2, 'Amy', '25f9e794323b453885f5181f1b624d0b', 'amy@gmail.com', 'student', '2223333777'),
(3, 3, 'Google', '25f9e794323b453885f5181f1b624d0b', 'google@gmail.com', 'company', '22233338888'),
(4, 3, 'Sun', '25f9e794323b453885f5181f1b624d0b', 'sun@gmail.com', 'company', '22233339999')
;
commit;

insert into user_info (user_id, first_name, last_name, address, institution, icon_url) values
(1, 'Tommy','Zhang', '999 Washington Ave, Fairfax, VA 22030', 'UW', 'qwesdasdasdasd.png'),
(2, 'Amy', 'Wang', '998 Washington Ave, Fairfax, VA 22030', 'UCLA', 'asdasdasdasdasd.png'),
(3, 'Gloria', 'Zhang', '997 Washington Ave, Fairfax, VA 22030', 'Google Ltd', 'zxcasdasdasdasd.png'),
(4, 'Bob', 'Huang', '996 Washington Ave, Fairfax, VA 22030', 'Sun Ltd', 'ghdasdasdasd.png')
;
commit;

insert into recommendation_letter (id, student_id, company_id, letter_url, issue_institution) values
(1, 1, 3, 'sdfdfh3wer324few234dfs9.pdf', 'Google'),
(2, 2, 4, 'sdf3io4v9fd843ojfd8934kfv.pdf', 'Sun')
;
commit;

insert into resume (id, student_id, resume_url, resume_name, status) values
(1, 1, 'sdfdfh3wer322213fds435.pdf', 'TommyResume.pdf','valid'),
(2, 2, 'c4353io4v9fd843o2345fd324.pdf', 'AmyRsume.pdf','valid')
;
commit;

insert into project (id, company_id, project_url, project_name, status) values
(1, 3, '123fe3c213422213fds435.pdf', 'PSP.pdf','valid'),
(2, 4, '456g67k34v9fd843o2345fd324.pdf', 'CloudBusiness.pdf','valid')
;
commit;

insert into ratestar (id, student_id, company_id,star_num) values
(1, 1, 3, 5),
(2, 2, 4, 3)
;
commit;

insert into student_project (id, student_id, project_id,status) values
(1, 1, 1, 'approved'),
(2, 2, 2, 'approved')
;
commit;