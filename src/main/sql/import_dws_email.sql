-- Procedure to import email field in veteran_info table from dws_all table which is imported from csv file.

SET ECHO OFF
SET SERVEROUTPUT ON
DECLARE
	-- Define working variables
	update_count NUMBER  := 0;


  	-- Define cursor
  	CURSOR veteran_dws_cur IS
		select v.id, d.email
		from veteran_info v join dws_all d on replace(v.ssn, '-') = d.ssn
		where 
  			v.va_current = 1 and
  			v.email is null and
  			d.email is not null;

BEGIN
	
	for vd_rec in veteran_dws_cur
	loop
		UPDATE veteran_info
		SET email = vd_rec.email
		WHERE id = vd_rec.id;
		
		update_count := update_count + 1;	
	end loop;
	
	commit;

	DBMS_OUTPUT.PUT_LINE('Total veteran records updated: ' || update_count);
END;
/

