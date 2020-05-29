CREATE TABLE public.users (
	id serial NOT NULL,
	"name" varchar(100) NULL,
	btcbalance int4 NULL,
	usdbalance int4 NULL,
	username varchar(100) NULL,
	"password" varchar(200) NULL,
	CONSTRAINT users_pk PRIMARY KEY (id)
);


CREATE TABLE public.orders (
	id serial NOT NULL,
	"type" varchar(100) NULL,
	amount int4 NULL,
	price int4 NULL,
	userid int4 NULL
);


-- public.orders foreign keys

ALTER TABLE public.orders ADD CONSTRAINT orders_userid_fkey FOREIGN KEY (userid) REFERENCES users(id);