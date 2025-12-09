create table Alumno (
    nombre TEXT,
    apellido TEXT,
    edad INTEGER,
    primary key (nombre, apellido)
);

INSERT INTO ALUMNO VALUES ('Adrián', 'Junquera', 21);
INSERT INTO ALUMNO VALUES ('Juan', 'Pérez', 19);
INSERT INTO ALUMNO VALUES ('María', 'López', 23);
INSERT INTO ALUMNO VALUES ('Unai', 'Nieto', 21);

SELECT * FROM ALUMNO;