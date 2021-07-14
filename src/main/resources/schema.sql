CREATE TABLE IF NOT EXISTS user_details
(
    usr_id       VARCHAR(50),
    usr_pswrd    VARCHAR(128),
    usr_mail     VARCHAR(200),
    usr_fst_name VARCHAR(200),
    usr_lst_name VARCHAR(200),
    usr_role VARCHAR(7),
    usr_enabled BOOLEAN,
    PRIMARY KEY (usr_id)
);
