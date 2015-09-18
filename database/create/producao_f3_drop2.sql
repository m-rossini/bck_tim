-- criando visão temporária para auxiliar update 
create view temp_count as (
SELECT a.web_request_id, a.filename, a.create_datetime, a.message, 
 ( SELECT COUNT(bck_consequence.objid) 
        FROM bck_consequence 
 JOIN bck_rule ON bck_rule.objid = bck_consequence.rule_uid 
 WHERE transaction_id = a.web_request_id AND 
 INSTR(a.message, rule_name) > 0 ) record_count 
 FROM web_bundlefile a 
)
/

-- procurando relatórios duplicados. Não deveria encontrar nenhum!!!
select web_request_id, filename from (
select web_request_id, filename, count(*) as filecount from web_bundlefile
group by web_request_id, filename
) where filecount > 1
/

-- para cada par web_request_id e filename encontrado, usar a seguinte consulta para selecionar os ids duplicados
-- select file_id from web_bundlefile
-- where  web_request_id = ?
-- and filename = ? 
-- order by create_datetime 
-- /

-- remover todos menos o mais recente (último da lista anterior)
-- delete from web_bundlefile where file_id = ?
-- /


-- gerar a lista de updates para atualizar o novo campo da tabela
select 
'update web_bundlefile set record_count = ' || record_count || ' where web_request_id = ' || web_request_id || 
                                                               ' and filename = ''' || filename || ''';'
from temp_count
where record_count > 0
/

-- copiar o resultado da consulta acima e executá-lo


-- remover visão temporária
drop view temp_count
/



-- atualizando os flags da tabela de endereços de operadoras
update bck_carrier_data
set local_flag = 'T' 
where carrier_dm_uid in ( select objid from bck_carrier_dm where carrier_code = 41)
/