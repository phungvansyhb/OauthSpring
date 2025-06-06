alter table app_table
    add column if not exists provider varchar(255) ,
    add column if not exists provider_id varchar(20);
