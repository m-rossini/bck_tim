INSERT INTO BCK_RULE VALUES ( 49, 'R01-11', 'Chamadas Zeradas sem Benef�cios',  null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 50, 'R03-2' , 'Benef�cio Inv�lido',               null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 51, 'R04-3' , 'Valida��o de CredCorp',            null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 52, 'R04-4' , 'Valida��o de Chamadas em MC',      null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 53, 'R15-3' , 'Valida��o de ICMS na NF',          null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 54, 'R19-1' , 'Valor de Promo��es',               null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 55, 'R19-3' , 'Limites de Promo��es',             null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 56, 'R19-6' , 'Promo��o Fora da Vig�ncia',        null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 57, 'R25-1' , 'Cobran�a de Taxa de Ades�o',       null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 58, 'R28-1' , 'Pacote Diferente do Plano',        null, 1, null,null,null);
INSERT INTO BCK_RULE VALUES ( 59, 'R28-2' , 'Plano sem Pacote Obrigat�rio',     null, 1, null,null,null);


INSERT INTO QLF_TARIFF_ZONE VALUES (  (SELECT MAX(OBJID)+ 1 FROM QLF_TARIFF_ZONE), 407, 'TAP Incollect IR - MOC', 'CRIOU', '-', null, null, null);
INSERT INTO QLF_TARIFF_ZONE VALUES (  (SELECT MAX(OBJID)+ 1 FROM QLF_TARIFF_ZONE), 408, 'TAP Incollect IR - MTC', 'CRIIN', '-', null, null, null);

