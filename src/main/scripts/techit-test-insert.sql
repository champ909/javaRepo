	-- John Smith is the supervisor of Unit 1
insert into users (firstName, lastName, username, pass, phone, email, status, unit_id) values
    ('John', 'Smith', 'jsmith', sha2('abcd',256), '(555) 555-5555', 'jsmith@localhost.localdomain', 1, 1);

-- Jimmy Jim is a technician of Unit 1
insert into users (firstName, lastName, username, pass, phone, email, status, unit_id) values
    ('Jimmy', 'Jim', 'jjim', sha2('abcd',256), '(555) 555-5555', 'jjim@localhost.localdomain', 2, 1);

-- Bob Lee is a technician of Unit 1
insert into users (firstName, lastName, username, pass, phone, email, status, unit_id) values
    ('Bob', 'Lee', 'blee', sha2('abcd',256), '(555) 555-5555', 'blee@localhost.localdomain', 2, 1);

-- Johesph Joestar is a regular user (no department)
insert into users (firstName, lastName, username, pass, phone, email, status) values
    ('Joseph', 'Joestar', 'jojo', sha2('abcd',256), '(666) 666-6666', 'jojo@localhost.localdomin', 0); 

-- A ticket created by jojo
insert into tickets (username, userfirstName, userlastName, phone, email, currentProgress, unit_id,
    subject, details, startDate, lastUpdated, ticketLocation) values
    ('jojo', 'Joseph', 'Joestar', '(666) 666-6666', 'jojo@localhost.localdomin', 0, 1,
    'Projector malfunction.', 'The projector is broken in room A220.', 
    '2016-10-13 00:00:01', '2016-10-13 00:00:01', 'ET A220');