INSERT INTO waimage.image_type(code, name) VALUES 
	('ADV', 'adventure'),
	('CAT', 'category'),
	('USR', 'user');

INSERT INTO waimage.image(alt, description, uri, type_code) VALUES
	('Ile Reunion Diagonale des fous 1', 'Ile Reunion Diagonale des fous 1 une aventure wild adventures', 'reunion1.jpg', 'ADV' ),
	('Ile Reunion Diagonale des fous 2', 'Ile Reunion Diagonale des fous 2 une aventure wild adventures', 'reunion2.jpg', 'ADV' ),
	('Ile Reunion Diagonale des fous 3', 'Ile Reunion Diagonale des fous 3 une aventure wild adventures', 'reunion3.jpg', 'ADV' ),
	('Sahara algerien', 'Sahara algerien 1 une aventure wild adventures', 'desert1.jpg', 'ADV' ),
	('Himalaya Tibetain', 'Himalaya Tibetain 1 une aventure wild adventures', 'himalaya1.jpg', 'ADV' ),
	('Nautique categorie', E'Nautique categorie 1 une categorie d\'aventure wild adventures', 'sea.jpg', 'CAT' ),
	('Montagne categorie', E'Montagne categorie 1 une categorie d\'aventure wild adventures', 'mountain.jpg', 'CAT' ),
	('Acrobranche categorie', E'Acrobranche categorie 1 une categorie d\'aventure wild adventures', 'acrobranche.jpg', 'CAT' ),
	('Trek categorie', E'Trek categorie 1 une categorie d\'aventure wild adventures', 'trek.jpg', 'CAT' ),
	('Desert categorie', E'Desert categorie 1 une categorie d\'aventure wild adventures', 'desert.jpg', 'CAT' ),
	('Thrill categorie', E'Thrill categorie 1 une categorie d\'aventure wild adventures', 'thrill.jpg', 'CAT' ),
	('Escape game categorie', E'Escape game categorie 1 une categorie d\'aventure wild adventures', 'escape-game.jpg', 'CAT' );
	
INSERT INTO waimage.image_adventure(image_id, adventure_id) VALUES
	(1,1),
	(2,1),
	(3,1),
	(4,2),
	(5,3);
	
INSERT INTO waimage.image_category(image_id, category_id) VALUES
	(6,1),
	(7,2),
	(8,3),
	(9,4),
	(10,5),
	(11,6),
	(12,7);
	