
    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    create table tickets (
		id bigint auto_increment primary key,
        completionDetails varchar(255),
        currentPriority integer,
        currentProgress integer not null,
        department varchar(255),
        details varchar(255),
        email varchar(255) not null,
        endDate datetime,
        lastUpdated datetime,
        lastUpdatedTime varchar(255),
        phone varchar(255),
        startDate datetime not null,
        startDateTime varchar(255),
        subject varchar(255) not null,
        ticketLocation varchar(255),
        userFirstName varchar(255) not null,
        userLastName varchar(255) not null,
        username varchar(255) not null,
        unit_id bigint
    ) engine=InnoDB;

    create table units (
		id bigint auto_increment primary key,
        description varchar(255),
        email varchar(255),
        location varchar(255),
        name varchar(255) not null,
        phone varchar(255)
    ) engine=InnoDB;

	insert into units (name, description) values ('TechOPs', concat_ws(' ',
    'Technical Operations, or TechOps,  is a unit in the ECST College.'
    'TechOps runs the Hydrogen Station, and provides technical assistance to',
    'the ECST departments such as creating and replacing part for senior',
    'design project.'));

    create table updates (
		id bigint auto_increment primary key,
        modifiedDate varchar(255) not null,
        modifier varchar(255) not null,
        updateDetails varchar(255) not null,
        ticket_id bigint
    ) engine=InnoDB;

    create table users (
		id bigint auto_increment primary key,
        department varchar(255),
        email varchar(255),
        enabled bit not null default true,
        firstName varchar(255) not null,
        lastName varchar(255) not null,
        pass varchar(255) not null,
        phone varchar(255),
        status integer not null,
        username varchar(255) not null unique,
        unit_id bigint
    ) engine=InnoDB;

	insert into users (firstName, lastName, username, pass, email, status, enabled) values
    ('System', 'Admin', 'techit', sha2('abcd',256), 'techit@localhost.localdomain', 0, true);	
	
    create table users_tickets (
       technicians_id bigint not null,
        tickets_id bigint not null
    ) engine=InnoDB;

    alter table users 
       add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

    alter table tickets 
       add constraint FKmj126vcy9uobxd6rfu269wjc2 
       foreign key (unit_id) 
       references units (id);

    alter table updates 
       add constraint FK3fnl74oyd1raon25v5lo3hyag 
       foreign key (ticket_id) 
       references tickets (id);

    alter table users 
       add constraint FKp2hfld4bhbwtakwrmt4xq6een 
       foreign key (unit_id) 
       references units (id);

    alter table users_tickets 
       add constraint FKbc2abl00uhgxid597yy5aq4cq 
       foreign key (tickets_id) 
       references tickets (id);

    alter table users_tickets 
       add constraint FKhvflx9xdmlva74r6706t0j9kx 
       foreign key (technicians_id) 
       references users (id);
