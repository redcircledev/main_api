INSERT INTO user_details (usr_id, usr_pswrd, usr_mail, usr_fst_name, usr_lst_name) VALUES ('KylieVg','P@$$W0rd','mail@test.com','Ivan','Alcazar') ON CONFLICT DO NOTHING;