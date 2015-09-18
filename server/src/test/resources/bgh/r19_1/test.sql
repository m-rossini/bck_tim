-- TMCODE 100, PLAN_NAME 'Plano T Você GSM PR', UF 'PR'
INSERT INTO microcell_rates VALUES (315536, to_date('10/01/2000', 'DD/MM/YYYY'), 'XYZ', SYSDATE, null, '', 0.1, null, null, null, null);
INSERT INTO microcell_rates VALUES (315536, to_date('10/01/2005', 'DD/MM/YYYY'), 'XYZ', SYSDATE, null, '', 0.12, null, null, null, null);
INSERT INTO microcell_rates VALUES (315536, to_date('10/01/2010', 'DD/MM/YYYY'), 'XYZ', SYSDATE, null, '', 0.15, null, null, null, null);

-- testcase 01
INSERT INTO microcell_rates VALUES (315536, to_date('10/01/2000', 'DD/MM/YYYY'), 'DPN', SYSDATE, null, '', null, 6, null, null, null);

-- testcase 04
INSERT INTO microcell_rates VALUES (315536, to_date('10/01/2000', 'DD/MM/YYYY'), 'ABC', SYSDATE, null, '', 0.5, null, null, null, null);