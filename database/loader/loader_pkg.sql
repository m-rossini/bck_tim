
drop sequence seq1;

create sequence seq1 start with 1 increment by 1;


create or replace package loader 
as 
function word( p_str in varchar2, 
				p_n in varchar2, 
				p_separated_by in varchar2 default ',',
				p_enclosed_by in varchar2 default '''' ) 
	return varchar2;

function get_uid( p_table_name in varchar2,
				p_key_column in varchar2,
				p_key_value in varchar2 )
	return number;

function get_plan_uid( plan_code in number,
				uf in varchar2)
	return number;
pragma restrict_references( word, WNDS, RNDS ); 
end;
/

create or replace package body loader 
as 
type vcArray is table of varchar2(2000) index by binary_integer;

g_words vcArray; 
g_empty vcArray; 
g_last_string varchar2(4096);

function de_quote( p_str in varchar2, p_enc_by in varchar2 ) 
	return varchar2
is 
begin 
	return replace( ltrim( rtrim( p_str, p_enc_by ), p_enc_by ), p_enc_by||p_enc_by, p_enc_by ); 
end de_quote;

procedure parse( p_str in varchar2, p_delim in varchar2, p_sep in varchar2 )
is 
	l_n number default 1; 
	l_in_quote boolean default FALSE; 
	l_ch char(1);
	l_len number default nvl(length( p_str ),0); 
begin 
	if ( l_len = 0 ) then
		return;
	end if;

	g_words := g_empty; 
	g_words(1) := NULL;

	for i in 1 .. l_len loop 
		l_ch := substr( p_str, i, 1 ); 
		if ( l_ch = p_delim ) then 
			l_in_quote := NOT l_in_quote; 
		end if; 
		if ( l_ch = p_sep AND NOT l_in_quote ) then 
			l_n := l_n + 1; 
			g_words(l_n) := NULL;
		else 
			g_words(l_n) := g_words(l_n)||l_ch; 
		end if; 
	end loop;

	for i in 1 .. l_n loop 
		g_words(i) := de_quote( g_words(i), p_delim );
	end loop; 
end parse;

function word( p_str in varchar2, 
				p_n in varchar2, 
				p_separated_by in varchar2 default ',', 
				p_enclosed_by in varchar2 default '''' ) 
	return varchar2 
is 
begin
	if ( g_last_string is NULL or p_str <> g_last_string ) then 
		g_last_string := p_str; 
		parse( p_str, p_enclosed_by, p_separated_by );
	end if; 
	return g_words( p_n ); 

exception 
	when no_data_found then 
		return NULL; 
end;

function get_uid( p_table_name in varchar2,
				p_key_column in varchar2,
				p_key_value in varchar2 )
	return number
is
	l_sql varchar2(200);
	l_objid number(19,0) := 0;
begin
	l_sql := 'select objid from ' || p_table_name || ' where ' || p_key_column || ' = ''' || p_key_value || '''';
	--dbms_output.put_line(l_sql);
	execute immediate l_sql into l_objid;

	return l_objid;

exception
	when no_data_found then
		return -1;
end;


function get_plan_uid( plan_code in number,
				uf in varchar2)
	return number
is
	l_sql varchar2(200);
	l_objid number(19,0) := 0;
begin
  if ( uf is NULL) then
    l_sql := 'select objid from qlf_plans where tmcode = ' || plan_code || ' and state is NULL ';
  else 
	  l_sql := 'select objid from qlf_plans where tmcode = ' || plan_code || ' and state =  ''' || uf || '''';
	end if;
	--dbms_output.put_line(l_sql);
	execute immediate l_sql into l_objid;

	return l_objid;

exception
	when no_data_found then
		return -1;
end;


end loader; 
/