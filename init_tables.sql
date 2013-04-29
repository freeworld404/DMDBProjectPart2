use dmdb2013;

DROP TABLE IF EXISTS 
	user, comments, fund, fundingLevel, 
	project, multimedia, category, city, 
	stretchGoal, videoData, imageData;

DROP VIEW IF EXISTS video, image;

CREATE TABLE user
	(	id	int not null auto_increment,
		email varchar(128) not null,
		username varchar(128) not null unique,
		pw varchar(64) not null,
		PRIMARY KEY (id));

CREATE TABLE comments
	(	id int not null auto_increment,
		content text not null,
		commentDate timestamp null default current_timestamp,
		uid int references user on update cascade on delete set null,
		pid int not null references project on update cascade on delete cascade,
		PRIMARY KEY (id));

CREATE TABLE fund
	(	id int not null auto_increment,
		fundDate timestamp null default current_timestamp,
		flid int not null references fundingLevel on update cascade on delete cascade,
		uid int references user on update cascade on delete set null,
		PRIMARY KEY (id));

CREATE TABLE fundingLevel
	(	id int not null auto_increment,
		amount int not null,
		reward text not null,
		pid int not null references project on update cascade on delete cascade,
		PRIMARY KEY (id));

CREATE TABLE project
	(	id int not null auto_increment,
		title varchar(128) not null,
		description text not null,
		baseGoal int not null,
		createDate timestamp null default current_timestamp,
		startDate date,
		endDate date,
		uid int not null references user on update cascade on delete cascade,
		catId int references category on update cascade on delete set null,
		cityId int references city on update cascade on delete set null, 
		PRIMARY KEY (id));

CREATE TABLE multimedia
	(	id int not null auto_increment,
		pid int not null references project on update cascade on delete cascade,
		PRIMARY KEY (id));

CREATE TABLE  videoData 
	(	vid int not null auto_increment,
		mid int not null references multimedia on update cascade on delete cascade,
		content blob not null,
		PRIMARY KEY (vid));

CREATE TABLE imageData
	(	iid int not null auto_increment,
		mid int not null references multimedia on update cascade on delete cascade,
		content blob not null,
		PRIMARY KEY (iid));

CREATE TABLE category
	(	id int not null auto_increment,
		catName varchar(64) not null,
		PRIMARY KEY (id));

CREATE TABLE  city
	(	id int not null auto_increment,
		cityName varchar (64) not null,
		PRIMARY KEY (id));

CREATE TABLE stretchGoal
	(	id int not null auto_increment,
		amount int not null,
		reward text not null,
		pid int not null references project on update cascade on delete cascade,
		PRIMARY KEY (id));

CREATE VIEW video as
	select *
	from multimedia m, videoData v
	where m.id = v.mid;

CREATE VIEW image as
	select *
	from multimedia m, imageData i
	where m.id = i.mid;

insert into user
	(email, username, pw)
values
	('john@john.com', 'john', 'a1b2c3'),
	('rijadn@ethz.ch', 'rijad', '4321'),
	('apatel@ethz.ch', 'abhi', 'ajsdfk1234'),
	('danielug@ethz.ch', 'dani', '4321'),
	('pimarko@ethz.ch', 'marko', '4321'),
	('jkrucher@ethz.ch', 'jonas', '4321'),
	('legabrie@ethz.ch', 'gabriel', '1234hfla');

insert into comments
	(content, uid, pid)
values
	('this is awesome!', 1, 1),
	('Cool stuff', 2, 3),
	('Super!', 3, 2),
	('Genial..', 4, 1);

insert into fund
	(flid, uid)
values
	(1, 2),
	(9, 1),
	(7, 3),
	(3, 4);

insert into fundingLevel
	(amount, reward, pid)
values
	(50, 'free tee', 1),
	(40, 'free te', 1),
	(30, 'free t', 1),
	(50, 'free tee', 2),
	(40, 'free te', 2),
	(30, 'free t', 2),
	(50, 'free tee', 3),
	(40, 'free te', 3),
	(30, 'free t', 3);

insert into project
	(title, description, baseGoal, startDate, endDate, uid, catId, cityId)
values
	('Death Star', 'I am your father', 999999999, '2013-01-01', '3000-12-31', 4, 3, 3),
	('Batman', 'Become Batman', 10000000, '2013-01-01', '2025-12-31', 3, 1, 2),
	('World Domination', 'Rule the world', 100000000, '1987-11-15', '2025-12-31', 2, 2, 1);

insert into multimedia
	(pid)
values
	(1),
	(2),
	(3);

insert into videoData
	(mid, content)
values
	(1,''),
	(2,''),
	(3,'');

insert into imageData
	(mid, content)
values
	(3,''),
	(2,''),
	(1,'');

insert into category
	(catName)
values
	('Superheroes'),
	('Supervillains'),
	('Tech');
insert into city
	(cityName)
values
	('New York'),
	('Gotham City'),
	('Mos Espa');

insert into stretchGoal
	(amount, reward, pid)
values
	(500000000,'I will let you live',3),
	(20000000,'Chance to become Robin',2),
	(100000000,'Make Death Star invincible',1);


grant all on dmdb2013.* to dmdb@localhost;
