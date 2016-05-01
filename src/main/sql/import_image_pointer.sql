-- Procedure to import image pointer to veteran_attachment table from veterans_export table which is imported from csv file.

SET ECHO OFF
SET SERVEROUTPUT ON
DECLARE
	-- Declare working variables
	insert_count NUMBER  := 0;
	veteranId NUMBER := 0;

  	-- Delare cursor
  	CURSOR veterans_export IS
		select ssn, image_pointer
		from veterans_export
		where 
  			length(ssn) = 9 and
  			ssn not like '%X%';

BEGIN
	
	for ve_rec in veterans_export
	loop
		select id into veteranId 
		from veteran_info
		where va_current = 1 and 
		replace(ssn, '-', '') = ve_rec.ssn;
		
		if veteranId > 0 then
			insert into veteran_attachment (id, veteran_id, attachment_file_name, attachment_content_type, doc_type_id, created_by, attachment_pointer)
			values (vts_attachment_seq.nextVal, veteranId, substr(ve_rec.image_pointer, 21), 'image/tiff', 600662, 100, ve_rec.image_pointer);
			insert_count := insert_count + 1;
		end if;
		
		veteranId := 0;
	end loop;
	
	commit;

	DBMS_OUTPUT.PUT_LINE('Total veteran attachment inserted: ' || insert_count);
END;
/
