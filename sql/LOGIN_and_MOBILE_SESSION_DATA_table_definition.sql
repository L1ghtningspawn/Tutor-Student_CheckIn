use seesselm_Capstone;
drop table LOGIN;
create table LOGIN 
(
	login_id int(11) auto_increment,
    email varchar(400),
	`password` varchar(255),
    
    primary key (login_id)
);

drop table MOBILE_SESSION_DATA;
create table MOBILE_SESSION_DATA
(
	id int(11) auto_increment,
    login_id int(11),
    session_id varchar(255),
    session_start datetime default now(),
    session_end datetime,
    
    primary key(id),
    foreign key(login_id) references LOGIN.login_id
    
);