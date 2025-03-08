--Oracle SQL scripts

CREATE TABLE USER_DTLS (
    USER_ID       NUMBER(10) NOT NULL,
    EMAIL_ID      VARCHAR2(100) NOT NULL,
    FIRST_NAME    VARCHAR2(40) NOT NULL,
    LAST_NAME     VARCHAR2(40) NOT NULL,
    CREATION_DATE TIMESTAMP(6),
    CONSTRAINT EMAIL_ID_PK PRIMARY KEY ( EMAIL_ID )
)
/

----------------------------------------------------------------------------------------------------------------------------

INSERT INTO USER_DTLS (USER_ID,EMAIL_ID,FIRST_NAME,LAST_NAME,CREATION_DATE) VALUES (0,'myemailid@somehost.com','First','Last',CURRENT_TIMESTAMP)
/

----------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE
/

----------------------------------------------------------------------------------------------------------------------------

COMMIT
/

----------------------------------------------------------------------------------------------------------------------------