# password: ratel_001 / 97b6a7c35da84e9073ea511a4eb2bc72a5edf5c733a010b406baa02fa3386149d04fccfcf61a4885e4e09d0c1001b1eb78ee86ffea97e57f8e61dae4f97b7170
insert into customer (customer_name, customer_code, password, nick_name, id_card, email)
values ('admin', '7671a1ca78af4096a0543fbcaa22d402', '97b6a7c35da84e9073ea511a4eb2bc72a5edf5c733a010b406baa02fa3386149d04fccfcf61a4885e4e09d0c1001b1eb78ee86ffea97e57f8e61dae4f97b7170', 'admin', 'admin', 'admin@ivipa.com');

insert into operator(customer_id, operator_type)
values(1, 0);