

-- creating temporary table
create table temp1 ( objid number(19) )
/

-- marking all attributes
insert into temp1 ( select attribute_uid from bck_consequence where rule_uid in (select objid from bck_rule where rule_code in ('R01-9', 'R01-13')) )
/

-- deleting consequences
delete from bck_consequence
where rule_uid in (select objid from bck_rule where rule_code in ('R01-9', 'R01-13'))
/

-- deleting attributes 
delete from bck_consequence_attr where objid in (select objid from temp1)
/

-- deleting rules
delete from bck_rule where rule_code in ('R01-9', 'R01-13')
/

-- commiting
commit
/

-- dropping temp table
drop table temp1
/