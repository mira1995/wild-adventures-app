INSERT INTO waadventure.adventure (title, description, status, location) VALUES
	('La diagonale des fous', E'Une des randonées les plus folles de la planète, traversez les plus beaux paysages montagneux sur l\'île de la Réunion! Des deux bouts paradysiaques de l\'île en passant par le volcan découvrez le splus beaux paysages de cette île Française', 'AVAILABLE', 'Ile de la Réunion (FR)'),
	('Le Sahara Algérien', E'Passez plus de cinq jours de randonnées dans l\'un des plus beaux déserts et des plus vastes déserts du mondes !', 'AVAILABLE', 'Algérie'),
	(E'L\'Himalaya Tibétain', E'Attaquez vous aux plus hauts plateaux du Tibet lors de cette aventures inoubliables sur le sommet de la planète !', 'AVAILABLE', 'Himalaye (Tibet)'),
	('Halloween Escape Game (SUPER ESCAPE)', E'Retrouvez l\'évènement d\'Halloween de notre partenaire SUPER ESCAPE, lors d\'un escape game remplie de frissons !', 'AVAILABLE', 'Paris (FR)'),
	('Un été Scandinave', E'Vous n\'arrivez pas à choisir entre ski et vacances à la plage ? Retrouvez cette aventure sportive offrant un panachée des plus belles randonnées, pistes de skis et activités balnéaires en Norvège! Quinze jours d\'aventures incroyables !', 'AVAILABLE', 'Norvège');
	
INSERT INTO wacategory.category_adventure (category_id, adventure_id) VALUES
	(1,1),
	(4,1),
	(4,2),
	(5,2),
	(2,3),
	(4,3),
	(7,4),
	(1,5),
	(2,5),
	(4,5);
	
INSERT INTO waadventure.session (adventure_id, start_date, end_date, price) VALUES
	(1, '2019-10-21', '2019-10-31', 2000.00),
	(2, '2018-11-21', '2018-11-30', 1500.00),
	(2, '2018-12-21', '2018-12-31', 1400.00),
	(2, '2019-02-21', '2019-03-04', 1600.00),
	(3, '2019-10-31', '2019-11-30', 4000.00),
	(4, '2018-10-31', '2018-11-30', 25.00),
	(4, '2018-11-01', '2018-12-31', 25.00),
	(4, '2018-11-02', '2019-03-04', 25.00);
	
	
	
	
	
	