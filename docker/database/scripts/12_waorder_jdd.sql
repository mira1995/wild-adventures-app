INSERT INTO waorder.orders(order_date, status, is_paid, useraccount_id) VALUES
	('2018-09-30', 'NOT_PAID', false, 2),
	('2018-07-06', 'FINALIZED', true, 2),
	('2018-08-08', 'UPDATE_DEMAND', true, 2),
	('2018-08-08', 'DELETE_DEMAND', true, 2),
	('2018-09-01', 'FINALIZED', true, 1),
	('2018-09-01', 'FINALIZED', true, 3);
	
INSERT INTO waorder.order_session(nb_order, order_id, session_id) VALUES
	(2, 1, 1),
	(3, 1, 2),
	(1, 2, 5),
	(5, 3, 6),
	(5, 4, 7),
	(1, 5, 1),
	(2, 5, 2),
	(2, 5, 5),
	(10, 6, 2),
	(10, 6, 8);