INSERT INTO BCK_RULE VALUES ( 49, 'R01-11', 'Chamadas Zeradas sem Benefícios',  null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 50, 'R03-2' , 'Benefício Inválido',               null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 51, 'R04-3' , 'Validação de CredCorp',            null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 52, 'R04-4' , 'Validação de Chamadas em MC',      null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 53, 'R15-3' , 'Validação de ICMS na NF',          null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 54, 'R19-1' , 'Valor de Promoções',               null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 55, 'R19-3' , 'Limites de Promoções',             null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 56, 'R19-6' , 'Promoção Fora da Vigência',        null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 57, 'R25-1' , 'Cobrança de Taxa de Adesão',       null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 58, 'R28-1' , 'Pacote Diferente do Plano',        null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 59, 'R28-2' , 'Plano sem Pacote Obrigatório',     null, 1, null,null,null);


INSERT INTO QLF_TARIFF_ZONE VALUES (  (SELECT MAX(OBJID)+ 1 FROM QLF_TARIFF_ZONE), 407, 'TAP Incollect IR - MOC', 'CRIOU', '-', null, null, null);
INSERT INTO QLF_TARIFF_ZONE VALUES (  (SELECT MAX(OBJID)+ 1 FROM QLF_TARIFF_ZONE), 408, 'TAP Incollect IR - MTC', 'CRIIN', '-', null, null, null);

