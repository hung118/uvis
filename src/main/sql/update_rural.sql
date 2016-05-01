-- Procedure to update rural field in veteran_info table based on zip codes found in rural_crosswalk table.
-- Possible values for rural field:
--	U - Urban
--	R - Rural
--	H - Highly Rural

SET ECHO OFF
SET SERVEROUTPUT ON
DECLARE
	-- Define working variables
	update_count NUMBER  := 0;


  	-- Define cursors
  	CURSOR veteran_cur IS
		SELECT
      		id,
      		ssn,
      		zip
    	FROM
      		veteran_info
    	WHERE
      		va_current = 1
      	ORDER BY
      		id;
	
	CURSOR rural_cur IS
		SELECT
			zip_code,
			designation
		FROM
			rural_crosswalk
		WHERE
			zip_code like '84%';	-- for all Utah zip codes

BEGIN
	
	for veteran_rec in veteran_cur
	loop
		for rural_rec in rural_cur
			loop
				if substr(veteran_rec.zip, 1, 5) = rural_rec.zip_code then
					UPDATE veteran_info
					SET rural = rural_rec.designation
					WHERE id = veteran_rec.id;
					
					update_count := update_count + 1;	
					exit;
				end if;
			end loop;
	end loop;
	
	commit;

	DBMS_OUTPUT.PUT_LINE('Total veteran records updated: ' || update_count);
END;
/

