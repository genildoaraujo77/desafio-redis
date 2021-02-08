create table t_gastos (
    id bigint auto_increment,
    create_time timestamp,
    update_time timestamp,
    description varchar(255),
    value bigint,
    primary key (id)
);