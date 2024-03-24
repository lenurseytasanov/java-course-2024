create table if not exists Chat
(
    id bigint not null,
    primary key (id)
);

create table if not exists Link
(
    id bigint generated always as identity,
    url text unique not null,
    updated_at timestamp with time zone not null,
    checked_at timestamp with time zone not null,
    primary key (id)
);

create table if not exists Tracking
(
    chat_id bigint not null,
    link_id serial not null,
    primary key (chat_id, link_id),
    foreign key (chat_id) references Chat(id),
    foreign key (link_id) references Link(id)
);
