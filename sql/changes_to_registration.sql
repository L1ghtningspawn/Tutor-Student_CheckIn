alter table registration
	drop email;
    
alter table registration
	add column login_id int,
    add constraint foreign key(login_id) references LOGIN.login_id;