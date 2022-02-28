create table user
(
    id                 int primary key,
    username           varchar(100),
    encrypted_password varchar(100),
    avatar             varchar(100),
    created_at         datetime,
    updated_at         datetime
)