set head off;

set pagesize 0;

select
  user_login ||
  ';' ||
  first_name || ' ' || last_name ||
  ';' ||
  custom_1 ||
  ';' ||
  to_char(( select min(b.insert_date) from auth_passwd_history b where b.user_objid = a.objid ), 'DD/MM/YYYY') ||
  ';' ||
  to_char(( select max(b.last_used) from auth_passwd_history b where b.user_objid = a.objid ), 'DD/MM/YYYY') ||
  ';' ||
  ( select b.role_name
        from auth_role b
        join auth_user_roles c on c.role_objid = b.objid
        where c.user_objid = a.objid
        and ( c.expiration_date is null or c.expiration_date > sysdate )
        and b.role_status = 'A'
  ) ||
  ';' ||
  decode(user_status, 'L', 0, 1)
from auth_user a;

quit;