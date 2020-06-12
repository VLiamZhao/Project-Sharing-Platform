CREATE TABLE users (
                id              		BIGSERIAL NOT NULL,
                role_id         	    BIGINT NOT NULL,
                username        	    VARCHAR(30) NOT NULL UNIQUE,
                password        	    VARCHAR(300) NOT NULL,
                email           		VARCHAR(300) NOT NULL UNIQUE,
                user_type               VARCHAR(30) NOT NULL,
                phone_num       	    VARCHAR(30),
                PRIMARY KEY (id)
);

CREATE TABLE user_info (
                id              		BIGSERIAL NOT NULL,
                user_id         	    BIGINT NOT NULL,
                first_name      	    VARCHAR(30),
                last_name       	    VARCHAR(30),
                address         	    VARCHAR(300),
                institution          	VARCHAR(300),
                register_time           TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
                icon_url                 VARCHAR(300),
                PRIMARY KEY (id)
);


CREATE TABLE recommendation_letter (
                id              		BIGSERIAL NOT NULL,
                student_id      	    BIGINT NOT NULL,
                company_id      	    BIGINT NOT NULL,
                letter_url           	VARCHAR(300),
                issue_institution       VARCHAR(300),
                create_time             TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
                status                  VARCHAR(300),
                PRIMARY KEY (id)
);

CREATE TABLE resume (
                id              		BIGSERIAL NOT NULL,
                student_id      	    BIGINT NOT NULL,
                resume_url              VARCHAR(300),
                resume_name             VARCHAR(300),
                create_time             TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
                status                  VARCHAR(300),
                description             VARCHAR(300),
                PRIMARY KEY (id)
);

CREATE TABLE project (
                id              		BIGSERIAL NOT NULL,
                company_id      	    BIGINT NOT NULL,
                project_url             VARCHAR(300),
                project_name            VARCHAR(300),
                create_time             TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
                status                  VARCHAR(300),
                description             VARCHAR(300),
                PRIMARY KEY (id)
);

CREATE TABLE ratestar (
                id              		BIGSERIAL NOT NULL,
                student_id	            BIGINT NOT NULL,
                company_id              BIGINT NOT NULL,
                rate_time               TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
                star_num                INT,
                PRIMARY KEY (id)
);

CREATE TABLE student_project (
                id              		BIGSERIAL NOT NULL,
                student_id              BIGSERIAL NOT NULL,
                project_id              BIGSERIAL NOT NULL,
                status                  VARCHAR(300),
                PRIMARY KEY (id)
);

CREATE TABLE role (
                id                       BIGSERIAL NOT NULL,
                role_name                VARCHAR(30) NOT NULL UNIQUE,
                allowed_resource         VARCHAR(200),
                allowed_read             BOOLEAN NOT NULL default FALSE ,
                allowed_create           BOOLEAN NOT NULL default FALSE,
                allowed_update           BOOLEAN NOT NULL default FALSE,
                allowed_delete           BOOLEAN NOT NULL default FALSE,
                PRIMARY KEY (id)
);


ALTER TABLE users
    ADD CONSTRAINT role_fk FOREIGN KEY ( role_id )
        REFERENCES role ( id );

ALTER TABLE user_info
    ADD CONSTRAINT users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( id );

ALTER TABLE ratestar
    ADD CONSTRAINT users_fk FOREIGN KEY ( company_id )
        REFERENCES users ( id );

ALTER TABLE resume
    ADD CONSTRAINT users_fk FOREIGN KEY ( student_id )
        REFERENCES users ( id );

ALTER TABLE student_project
    ADD CONSTRAINT users_fk FOREIGN KEY ( student_id )
        REFERENCES users ( id );

ALTER TABLE student_project
    ADD CONSTRAINT project_fk FOREIGN KEY ( project_id )
        REFERENCES project ( id );

ALTER TABLE project
    ADD CONSTRAINT users_fk FOREIGN KEY ( company_id )
        REFERENCES users ( id );

ALTER TABLE recommendation_letter
    ADD CONSTRAINT users_fk FOREIGN KEY ( student_id )
        REFERENCES users ( id );

