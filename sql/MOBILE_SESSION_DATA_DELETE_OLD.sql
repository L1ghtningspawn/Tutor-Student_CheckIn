drop trigger MOBILE_SESSION_DATA_DELETE_OLD;

delimiter $$
Create Trigger MOBILE_SESSION_DATA_DELETE_OLD 
before insert
on MOBILE_SESSION_DATA for each row
begin
	Delete from MOBILE_SESSION_DATA
	where login_id=NEW.login_id and
		DATEDIFF(session_end, NOW()) <= 0;
end $$