-- ACCOUNT3.BGH
insert into service_rates (
	select a.objid, b.objid, to_date('01/01/2000', 'DD/MM/YYYY'), sysdate, 5.0, 0, 0, null, null, null
	from qlf_plans a,
	qlf_services b
    where b.description = 'Serviço de Voz' and 
    a.plan_name = 'TIM Empresa Nacional' and 
    a.state = 'SP'
);


-- ACCOUNT4.BGH
insert into service_rates (
	select a.objid, b.objid, to_date('01/01/2000', 'DD/MM/YYYY'), sysdate, 36.5, 0, 0, null, null, null
	from qlf_plans a,
	qlf_services b
    where b.description = 'Tarifa Zero 41' and 
    a.plan_name = 'TIM Empresa Nacional' and 
    a.state = 'SP'
);

insert into service_rates (
	select a.objid, b.objid, to_date('01/01/2000', 'DD/MM/YYYY'), sysdate, 17.4, 0, 0, null, null, null
	from qlf_plans a,
	qlf_services b
    where b.description = 'Tarifa Zero' and 
    a.plan_name = 'TIM Empresa Nacional' and 
    a.state = 'SP'
);

-- ACCOUNT5.BGH
insert into package_rates (
	select a.objid, b.objid, to_date('01/01/2000', 'DD/MM/YYYY'), sysdate, 165, 0, null, null, null
	from qlf_plans a,
	qlf_package b
    where b.description = 'Novo Pacote Minutos Nacional' and 
    a.plan_name = 'Plano Nosso Modo' and 
    a.state = 'RJ'
);
