-- тестовые пользователи для демо, пароли описаны в readme, поэтому хэши тут не секрет
insert into users (username, password_hash) values ('alice', '$2a$10$iT3bHs8Kh6rpUklxgc9erOKlunKPnwhqLl.TIRf39FXSPd18N5kUu'); -- nosemgrep: generic.secrets.security.detected-bcrypt-hash.detected-bcrypt-hash
insert into users (username, password_hash) values ('bob', '$2a$10$AOzwLrfhbhlQ/2hkOy4.j.vWNMHICBxVa8cVv6p9l3jUjP5j1KdJ2'); -- nosemgrep: generic.secrets.security.detected-bcrypt-hash.detected-bcrypt-hash

insert into notes (title, content, created_at, owner_id) values ('Welcome', 'First note, feel free to delete it', current_timestamp, 1);
insert into notes (title, content, created_at, owner_id) values ('Shopping', 'milk, bread, coffee', current_timestamp, 1);
insert into notes (title, content, created_at, owner_id) values ('Todo', 'finish the lab', current_timestamp, 2);
