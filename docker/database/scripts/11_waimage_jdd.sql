INSERT INTO waimage.image_type(code, name) VALUES 
	('ADV', 'adventure'),
	('CAT', 'category'),
	('USR', 'user');

INSERT INTO waimage.image(alt, description, uri, type_code) VALUES
	('Ile Reunion Diagonale des fous 1', 'Ile Reunion Diagonale des fous 1 une aventure wild adventures', 'reunion1.jpg', 'ADV' ),
	('Ile Reunion Diagonale des fous 2', 'Ile Reunion Diagonale des fous 2 une aventure wild adventures', 'reunion2.jpg', 'ADV' ),
	('Ile Reunion Diagonale des fous 3', 'Ile Reunion Diagonale des fous 3 une aventure wild adventures', 'reunion3.jpg', 'ADV' ),
	('Sahara algerien', 'Sahara algerien 1 une aventure wild adventures', 'desert1.jpg', 'ADV' );
	
INSERT INTO waimage.image_adventure(image_id, adventure_id) VALUES
	(1,1),
	(2,1),
	(3,1),
	(4,2);