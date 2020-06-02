

CREATE TABLE public.users (
	id serial NOT NULL,
	"name" varchar(100) NULL,
	btcbalance numeric(200,10) NULL,
	usdbalance numeric(200,10) NULL,
	username varchar(100) NULL,
	"password" varchar(200) NULL,
	"token" varchar(200) NULL,
	CONSTRAINT username_uk UNIQUE (username),
	CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE public.orders (
	id serial NOT NULL,
	"type" varchar(100) NULL,
	amount numeric(200,10) NULL,
	price numeric(200,10) NULL,
	username varchar(100) NULL,
	status varchar(100) NULL,
	coin varchar(100) NULL
);

ALTER TABLE public.orders ADD CONSTRAINT orders_userid_fkey FOREIGN KEY (username) REFERENCES users(username);