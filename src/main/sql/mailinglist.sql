-- use sql plus - login username/password@instancename
set heading off
set linesize 1000
set pagesize 5000
spool c:\temp\vts\mailinglist\vts01152013.txt
select first_name||' '||last_name as Name, address1 || ' ' || address2 as Address, city, state, zip
from veteran_info
where 
va_current = 1 and
substr(zip, 1, 5) in 
(
'84791',
'84790',
'84786',
'84783',
'84782',
'84781',
'84780',
'84779',
'84774',
'84771',
'84770',
'84767',
'84765',
'84763',
'84757',
'84746',
'84745',
'84737',
'84738',
'84733',
'84725',
'84722',
'84714',
'84719',
'84720',
'84721',
'84742',
'84753',
'84756',
'84760',
'84761',
'84772',
'84710',
'84729',
'84741',
'84755',
'84758',
'84762'
) and
address1 is not null
order by last_name, first_name
spool off

--
select first_name||' '||last_name as Name, address1 || ' ' || address2 as Address, city, state, zip, email
from veteran_info
where 
va_current = 1 and
zip in 
(
'84791',
'84790',
'84784',
'84783',
'84782',
'84781',
'84780',
'84779',
'84774',
'84771',
'84770',
'84767',
'84765',
'84763',
'84757',
'84746',
'84745',
'84738',
'84737',
'84733',
'84725',
'84722',
'84742'
) and
address1 is not null
order by last_name, first_name

-- for all veteran born from 01-01-1925 to 01-12-1938
select first_name||' '||last_name as Name, address1 || ' ' || address2 as Address, city, state, zip
from veteran_info
where 
va_current = 1 and
date_of_birth >= to_date('01-JAN-1925') and date_of_birth <= to_date('01-DEC-1938') and
address1 is not null
order by last_name, first_name

