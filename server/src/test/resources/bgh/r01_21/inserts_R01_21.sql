
INSERT INTO QLF_PACKAGE(OBJID, SHORT_DESC, DESCRIPTION) VALUES (SEQ1.NEXTVAL, 'AGG01', )
/


-- ACCOUNT2.BGH
INSERT INTO meusonho_rates ( TARIFF_ZONE_UID, STATE, PACKAGE_NAME, PPM, LOADED_DATE )
VALUES ( (select objid from qlf_tariff_zone where short_desc='VCMC'),
         'SP', 'Pacote Ideal = Meu Sonho 125', 0.36, SYSDATE)
/

-- ACCOUNT2.BGH
INSERT INTO meusonho_rates ( TARIFF_ZONE_UID, STATE, PACKAGE_NAME, PPM, LOADED_DATE )
VALUES ( (select objid from qlf_tariff_zone where short_desc='VCMC'),
         'SP', 'Pacote Ideal = Meu Sonho 250', 0.40, SYSDATE)
/

-- ACCOUNT4.BGH
INSERT INTO meusonho_rates ( TARIFF_ZONE_UID, STATE, PACKAGE_NAME, PPM, LOADED_DATE )
VALUES ( (select objid from qlf_tariff_zone where short_desc='VCTC'),
         'RJ', 'Pacote Ideal = Meu Sonho 250', 0.43, SYSDATE)
/

INSERT INTO meusonho_rates ( TARIFF_ZONE_UID, STATE, PACKAGE_NAME, PPM, LOADED_DATE )
VALUES ( (select objid from qlf_tariff_zone where short_desc='VCFC'),
         'RJ', 'Pacote Ideal = Meu Sonho 250', 0.43, SYSDATE)
/

INSERT INTO meusonho_rates ( TARIFF_ZONE_UID, STATE, PACKAGE_NAME, PPM, LOADED_DATE )
VALUES ( (select objid from qlf_tariff_zone where short_desc='VCMC'),
         'RJ', 'Pacote Ideal = Meu Sonho 250', 0.43, SYSDATE)
/