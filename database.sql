CREATE TABLE public."user" (
	id uuid NOT null,
	username varchar NOT NULL,
	"password" varchar NOT NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT user_un UNIQUE (email)
);

CREATE TABLE public.quiz (
	id uuid NOT null,
	session_id varchar NOT NULL,
	user_id uuid NOT NULL,
	results varchar NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT quiz_pk PRIMARY KEY (id),
	CONSTRAINT quiz_fk FOREIGN KEY (user_id) REFERENCES public."user"(id)
);

CREATE TABLE public.questions (
	id uuid NOT null,
	question_id varchar NOT NULL,
	quiz_id uuid NOT NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT questions_pk PRIMARY KEY (id),
	CONSTRAINT questions_fk FOREIGN KEY (quiz_id) REFERENCES public.quiz(id)
);