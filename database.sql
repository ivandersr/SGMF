CREATE TABLE aluno (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nome varchar(60) NOT NULL,
  DataNasc datetime(6) NOT NULL,
  Telefone varchar(20) DEFAULT NULL,
  DataInicioTreino datetime(6) DEFAULT NULL,
  Presenca boolean NOT NULL,
  Treino varchar(2000) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE plano (
	Id int(11) NOT NULL AUTO_INCREMENT,
	Nome varchar(60) NOT NULL,
	Mensalidade double(10,2) NOT NULL,
	Meses int(3) NOT NULL;
);


INSERT INTO aluno (Nome, DataNasc, Telefone, DataInicioTreino, Presenca, Treino) VALUES 
  ('AlunoTeste1','1991-07-25 00:00:00',998499840,'2019-05-12 00:00:00',0,'Teste de inser��o do treino do aluno n�mero 1.'),
  ('AlunoTeste2','1993-09-13 00:00:00',999214479,'2019-06-08 00:00:00',0,'Teste de inser��o do treino do aluno n�mero 2.'),
  ('AlunoTeste3','1990-12-01 00:00:00',981442808,'2019-08-02 00:00:00',0,'Teste de inser��o do treino do aluno n�mero 3.');
  
INSERT INTO plano (Nome, Mensalidade, Meses) VALUES
  ('Plano 1', '77.00', '2'),
  ('Plano 2', '92.00', '1');
