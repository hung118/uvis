SET ECHO OFF
SET SERVEROUTPUT ON
DECLARE
  -- Define working variables
  lv_ssn_lastdigits_count     NUMBER  := 0;
  lv_ssn_middigits_count      NUMBER  := 0;
  lv_update_count             NUMBER  := 0;
  lv_update_err_count         NUMBER  := 0;

  -- Define New Person Record Variables
  lv_rec_ssn                  VARCHAR2(11);

  -- Define new PERSON_NOTES record variables
  lv_found                    NUMBER;
  lv_rec_count                NUMBER;
  lv_rec_personnotes_pk       NUMBER;
  lv_rec_person_fk            NUMBER;
  lv_rec_note                 VARCHAR2(4000);
  lv_rec_umdid                VARCHAR2(30);

  -- Define cursors
  CURSOR person_cur IS
    SELECT
      p.person_pk,
      p.ssn
    FROM
      person p
    ORDER BY
      p.person_pk;

BEGIN

  FOR person_rec IN person_cur
  LOOP
    -- Initialize working variables

    -- Convert SSN column
			lv_found := 1;
			WHILE lv_found = 1
			LOOP
      	lv_ssn_lastdigits_count := lv_ssn_lastdigits_count + 1;
      	IF lv_ssn_lastdigits_count > 9999  OR (lv_ssn_lastdigits_count = 9999 AND lv_ssn_middigits_count = 0) THEN
        	lv_ssn_lastdigits_count := 0;
        	lv_ssn_middigits_count := lv_ssn_middigits_count + 1;
      	END IF;
--    	lv_rec_ssn := '???-' || LPAD(lv_ssn_middigits_count,2,'0') || '-' || LPAD(lv_ssn_lastdigits_count,4,'0');
      	lv_rec_ssn := '999-' || LPAD(lv_ssn_middigits_count,2,'0') || '-' || LPAD(lv_ssn_lastdigits_count,4,'0');

				SELECT count(*)
				INTO lv_rec_count
				FROM PERSON
				WHERE
    			ssn = lv_rec_ssn;

    		IF lv_rec_count = 0 THEN
    			lv_found := 0;
    		END IF;
    	END LOOP;

    	BEGIN
        UPDATE PERSON SET SSN = lv_rec_ssn WHERE person_pk = person_rec.person_pk;
        lv_update_count := lv_update_count + 1;
    	EXCEPTION
      	WHEN OTHERS THEN
          lv_update_err_count := lv_update_err_count + 1;
   		END;
  END LOOP;

  COMMIT;

  DBMS_OUTPUT.PUT_LINE('Total PERSON records updated: ' || lv_update_count || '   Errors: ' || lv_update_err_count);
END;
/

