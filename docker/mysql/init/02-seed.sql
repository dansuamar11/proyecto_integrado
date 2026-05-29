
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(20) NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id_rol`, `nombre_rol`) VALUES (1,'ADMIN'),(3,'ARBITRO'),(4,'ENTRENADOR'),(2,'GERENTE');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `id_rol_user` int NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`),
  KEY `id_rol_user` (`id_rol_user`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_rol_user`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`id_usuario`, `nombre`, `username`, `password_hash`, `id_rol_user`, `activo`) VALUES (1,'Administrador General','admin','$2a$10$1l1GYEMh8vSzoRF7X1mQV.5ZKG3mO0jLwiZ2VeAF.7kvdV95fcccC',1,1),(2,'Gerente Liga Aljarafe','gerente','$2a$10$SA5Kreim2UzTCpkj/ftreeFEyKO8fklomzx9sEn4VQiOEDbx3BSKS',2,1),(3,'Antonio Ramos','arbitro1','$2a$10$zo9LdHWnBbvb1vPgxDlOye3qaWpBulNzMSCdDobmJzhmiTlDCDeea',3,1),(4,'Pablo Lopez','arbitro2','$2a$10$zo9LdHWnBbvb1vPgxDlOye3qaWpBulNzMSCdDobmJzhmiTlDCDeea',3,1),(5,'Carlos Nunez','entrenador1','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(6,'Luis Moreno','entrenador2','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(7,'Sergio Campos','entrenador3','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(8,'Alberto Serrano','entrenador4','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(9,'Diego Herrera','entrenador5','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(10,'Manuel Cruz','entrenador6','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(11,'Javier Leon','entrenador7','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(12,'Pedro Romero','entrenador8','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(13,'Andres Gil','entrenador9','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(14,'Ruben Castro','entrenador10','$2a$10$sMkIkKToIQdhKU.oKIf/9uRD6iF555mHbJETW44pjZAmRPCihGD3i',4,1),(15,'Miguel Torres','arbitro3','$2a$10$zo9LdHWnBbvb1vPgxDlOye3qaWpBulNzMSCdDobmJzhmiTlDCDeea',3,1),(16,'Sergio Luna','arbitro4','$2a$10$zo9LdHWnBbvb1vPgxDlOye3qaWpBulNzMSCdDobmJzhmiTlDCDeea',3,1),(17,'Daniel Suárez Martín','dansuarez11','$2a$10$P6n4r9ZRYc3pobgkHvN7BeFCOU82UBmLsELpjuL/aJlivCaDSBCzC',4,1),(18,'Gerente 2','gerente2','$2a$10$K8bFd1DTAIoIAwbm.JzG6.WW//CU.fDIRlGDPCJ38dpm5GZz/NRmS',2,1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipo` (
  `id_equipo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(60) NOT NULL,
  `id_entrenador` int DEFAULT NULL,
  PRIMARY KEY (`id_equipo`),
  UNIQUE KEY `nombre` (`nombre`),
  UNIQUE KEY `id_entrenador` (`id_entrenador`),
  CONSTRAINT `equipo_ibfk_1` FOREIGN KEY (`id_entrenador`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `equipo` WRITE;
/*!40000 ALTER TABLE `equipo` DISABLE KEYS */;
INSERT INTO `equipo` (`id_equipo`, `nombre`, `id_entrenador`) VALUES (1,'Aljarafe Norte',5),(2,'Coria Basket',6),(3,'Tomares Club',7),(4,'Olivares Basket',8),(5,'Sanlucar Hoopers',9),(6,'Bormujos CB',17),(7,'Mairena Wolves',11),(8,'Gines Basket',12),(9,'Camas Linces',13),(10,'Umbrete Sur',14);
/*!40000 ALTER TABLE `equipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jugador` (
  `id_jugador` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `id_equipo` int NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `dorsal` int DEFAULT NULL,
  PRIMARY KEY (`id_jugador`),
  UNIQUE KEY `uk_jugador_equipo_dorsal` (`id_equipo`,`dorsal`),
  CONSTRAINT `jugador_ibfk_1` FOREIGN KEY (`id_equipo`) REFERENCES `equipo` (`id_equipo`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `jugador` WRITE;
/*!40000 ALTER TABLE `jugador` DISABLE KEYS */;
INSERT INTO `jugador` (`id_jugador`, `nombre`, `apellidos`, `id_equipo`, `activo`, `dorsal`) VALUES (1,'Alejandro','Norte Martin',1,1,3),(2,'Bruno','Norte Lopez',1,1,2),(3,'Carlos','Norte Garcia',1,1,1),(4,'Daniel','Norte Ruiz',1,1,7),(5,'Emilio','Norte Moreno',1,1,4),(6,'Fabian','Norte Vega',1,1,8),(7,'Gonzalo','Norte Ramos',1,1,6),(8,'Hugo','Norte Castro',1,1,0),(9,'Ivan','Norte Prieto',1,1,5),(10,'Alejandro','Coria Martin',2,1,3),(11,'Bruno','Coria Lopez',2,1,2),(12,'Carlos','Coria Garcia',2,1,1),(13,'Daniel','Coria Ruiz',2,1,7),(14,'Emilio','Coria Moreno',2,1,4),(15,'Fabian','Coria Vega',2,1,8),(16,'Gonzalo','Coria Ramos',2,1,6),(17,'Hugo','Coria Castro',2,1,0),(18,'Ivan','Coria Prieto',2,1,5),(19,'Alejandro','Tomares Martin',3,1,3),(20,'Bruno','Tomares Lopez',3,1,2),(21,'Carlos','Tomares Garcia',3,1,1),(22,'Daniel','Tomares Ruiz',3,1,7),(23,'Emilio','Tomares Moreno',3,1,4),(24,'Fabian','Tomares Vega',3,1,8),(25,'Gonzalo','Tomares Ramos',3,1,6),(26,'Hugo','Tomares Castro',3,1,0),(27,'Ivan','Tomares Prieto',3,1,5),(28,'Alejandro','Olivares Martin',4,1,3),(29,'Bruno','Olivares Lopez',4,1,2),(30,'Carlos','Olivares Garcia',4,1,1),(31,'Daniel','Olivares Ruiz',4,1,7),(32,'Emilio','Olivares Moreno',4,1,4),(33,'Fabian','Olivares Vega',4,1,8),(34,'Gonzalo','Olivares Ramos',4,1,6),(35,'Hugo','Olivares Castro',4,1,0),(36,'Ivan','Olivares Prieto',4,1,5),(37,'Alejandro','Sanlucar Martin',5,1,3),(38,'Bruno','Sanlucar Lopez',5,1,2),(39,'Carlos','Sanlucar Garcia',5,1,1),(40,'Daniel','Sanlucar Ruiz',5,1,7),(41,'Emilio','Sanlucar Moreno',5,1,4),(42,'Fabian','Sanlucar Vega',5,1,8),(43,'Gonzalo','Sanlucar Ramos',5,1,6),(44,'Hugo','Sanlucar Castro',5,1,0),(45,'Ivan','Sanlucar Prieto',5,1,5),(46,'Alejandro','Bormujos Martin',6,1,3),(47,'Bruno','Bormujos Lopez',6,1,2),(48,'Carlos','Bormujos Garcia',6,1,1),(49,'Daniel','Bormujos Ruiz',6,1,7),(50,'Emilio','Bormujos Moreno',6,1,4),(51,'Fabian','Bormujos Vega',6,1,8),(52,'Gonzalo','Bormujos Ramos',6,1,6),(53,'Hugo','Bormujos Castro',6,1,0),(54,'Ivan','Bormujos Prieto',6,1,5),(55,'Alejandro','Mairena Martin',7,1,3),(56,'Bruno','Mairena Lopez',7,1,2),(57,'Carlos','Mairena Garcia',7,1,1),(58,'Daniel','Mairena Ruiz',7,1,7),(59,'Emilio','Mairena Moreno',7,1,4),(60,'Fabian','Mairena Vega',7,1,8),(61,'Gonzalo','Mairena Ramos',7,1,6),(62,'Hugo','Mairena Castro',7,1,0),(63,'Ivan','Mairena Prieto',7,1,5),(64,'Alejandro','Gines Martin',8,1,3),(65,'Bruno','Gines Lopez',8,1,2),(66,'Carlos','Gines Garcia',8,1,1),(67,'Daniel','Gines Ruiz',8,1,7),(68,'Emilio','Gines Moreno',8,1,4),(69,'Fabian','Gines Vega',8,1,8),(70,'Gonzalo','Gines Ramos',8,1,6),(71,'Hugo','Gines Castro',8,1,0),(72,'Ivan','Gines Prieto',8,1,5),(73,'Alejandro','Camas Martin',9,1,3),(74,'Bruno','Camas Lopez',9,1,2),(75,'Carlos','Camas Garcia',9,1,1),(76,'Daniel','Camas Ruiz',9,1,7),(77,'Emilio','Camas Moreno',9,1,4),(78,'Fabian','Camas Vega',9,1,8),(79,'Gonzalo','Camas Ramos',9,1,6),(80,'Hugo','Camas Castro',9,1,0),(81,'Ivan','Camas Prieto',9,1,5),(82,'Alejandro','Umbrete Martin',10,1,3),(83,'Bruno','Umbrete Lopez',10,1,2),(84,'Carlos','Umbrete Garcia',10,1,1),(85,'Daniel','Umbrete Ruiz',10,1,7),(86,'Emilio','Umbrete Moreno',10,1,4),(87,'Fabian','Umbrete Vega',10,1,8),(88,'Gonzalo','Umbrete Ramos',10,1,6),(89,'Hugo','Umbrete Castro',10,1,0),(90,'Ivan','Umbrete Prieto',10,1,5),(91,'Daniel','Suárez Martín',1,0,9),(92,'Daniel','Suárez 2',1,0,20),(93,'Daniel','Suárez 3',1,0,35),(94,'Daniel','Suarez 55',1,0,55);
/*!40000 ALTER TABLE `jugador` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partido` (
  `id_partido` int NOT NULL AUTO_INCREMENT,
  `id_equipo_local` int NOT NULL,
  `id_equipo_visitante` int NOT NULL,
  `fecha` datetime NOT NULL,
  `puntos_local` int DEFAULT '0',
  `puntos_visitante` int DEFAULT '0',
  `id_arbitro` int DEFAULT NULL,
  `estado` enum('JUGADO','PENDIENTE') NOT NULL,
  PRIMARY KEY (`id_partido`),
  KEY `id_equipo_local` (`id_equipo_local`),
  KEY `id_equipo_visitante` (`id_equipo_visitante`),
  KEY `id_arbitro` (`id_arbitro`),
  CONSTRAINT `partido_ibfk_1` FOREIGN KEY (`id_equipo_local`) REFERENCES `equipo` (`id_equipo`),
  CONSTRAINT `partido_ibfk_2` FOREIGN KEY (`id_equipo_visitante`) REFERENCES `equipo` (`id_equipo`),
  CONSTRAINT `partido_ibfk_3` FOREIGN KEY (`id_arbitro`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `partido_chk_1` CHECK ((`id_equipo_local` <> `id_equipo_visitante`)),
  CONSTRAINT `partido_chk_2` CHECK ((`puntos_local` >= 0)),
  CONSTRAINT `partido_chk_3` CHECK ((`puntos_visitante` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `partido` WRITE;
/*!40000 ALTER TABLE `partido` DISABLE KEYS */;
INSERT INTO `partido` (`id_partido`, `id_equipo_local`, `id_equipo_visitante`, `fecha`, `puntos_local`, `puntos_visitante`, `id_arbitro`, `estado`) VALUES (1,1,2,'2026-01-11 18:00:00',128,138,3,'JUGADO'),(2,1,3,'2026-01-14 18:00:00',90,60,4,'JUGADO'),(3,1,4,'2026-01-17 18:00:00',67,68,15,'JUGADO'),(4,1,5,'2026-01-20 18:00:00',75,76,16,'JUGADO'),(5,1,6,'2026-01-23 18:00:00',83,84,3,'JUGADO'),(6,1,7,'2026-01-26 18:00:00',91,63,4,'JUGADO'),(7,1,8,'2026-01-29 18:00:00',68,71,15,'JUGADO'),(8,1,9,'2026-02-01 18:00:00',76,79,16,'JUGADO'),(9,1,10,'2026-02-04 18:00:00',84,87,3,'JUGADO'),(10,2,3,'2026-02-07 18:00:00',90,82,4,'JUGADO'),(11,2,4,'2026-02-10 18:00:00',67,61,15,'JUGADO'),(12,2,5,'2026-02-13 18:00:00',75,69,16,'JUGADO'),(13,2,6,'2026-02-16 18:00:00',83,77,3,'JUGADO'),(14,2,7,'2026-02-19 18:00:00',91,85,4,'JUGADO'),(15,2,8,'2026-02-22 18:00:00',68,64,15,'JUGADO'),(16,2,9,'2026-02-25 18:00:00',76,72,16,'JUGADO'),(17,2,10,'2026-02-28 18:00:00',84,80,3,'JUGADO'),(18,3,4,'2026-03-03 18:00:00',64,78,4,'JUGADO'),(19,3,5,'2026-03-06 18:00:00',72,86,15,'JUGADO'),(20,3,6,'2026-03-09 18:00:00',80,65,16,'JUGADO'),(21,3,7,'2026-03-12 18:00:00',140,130,3,'JUGADO'),(22,3,8,'2026-03-15 18:00:00',65,81,4,'JUGADO'),(23,3,9,'2026-03-18 18:00:00',73,60,15,'JUGADO'),(24,3,10,'2026-03-21 18:00:00',0,0,16,'PENDIENTE'),(25,4,5,'2026-03-24 18:00:00',0,0,3,'PENDIENTE'),(26,4,6,'2026-03-27 18:00:00',0,0,4,'PENDIENTE'),(27,4,7,'2026-03-30 18:00:00',0,0,15,'PENDIENTE'),(28,4,8,'2026-04-02 18:00:00',0,0,16,'PENDIENTE'),(29,4,9,'2026-04-05 18:00:00',0,0,3,'PENDIENTE'),(30,4,10,'2026-04-08 18:00:00',0,0,4,'PENDIENTE'),(31,5,6,'2026-04-11 18:00:00',0,0,15,'PENDIENTE'),(32,5,7,'2026-04-14 18:00:00',0,0,16,'PENDIENTE'),(33,5,8,'2026-04-17 18:00:00',0,0,3,'PENDIENTE'),(34,5,9,'2026-04-20 18:00:00',0,0,4,'PENDIENTE'),(35,5,10,'2026-04-23 18:00:00',0,0,15,'PENDIENTE'),(36,6,7,'2026-04-26 18:00:00',0,0,16,'PENDIENTE'),(37,6,8,'2026-04-29 18:00:00',0,0,3,'PENDIENTE'),(38,6,9,'2026-05-02 18:00:00',0,0,4,'PENDIENTE'),(39,6,10,'2026-05-05 18:00:00',0,0,15,'PENDIENTE'),(40,7,8,'2026-05-08 18:00:00',0,0,16,'PENDIENTE'),(41,7,9,'2026-05-11 18:00:00',0,0,3,'PENDIENTE'),(42,7,10,'2026-05-14 18:00:00',0,0,4,'PENDIENTE'),(43,8,9,'2026-05-17 18:00:00',0,0,15,'PENDIENTE'),(44,8,10,'2026-05-20 18:00:00',0,0,16,'PENDIENTE'),(45,9,10,'2026-05-23 18:00:00',0,0,3,'PENDIENTE');
/*!40000 ALTER TABLE `partido` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadisticas_partido` (
  `id_estadisticas` int NOT NULL AUTO_INCREMENT,
  `id_jugador` int NOT NULL,
  `id_partido` int NOT NULL,
  `puntos` int NOT NULL DEFAULT '0',
  `asistencias` int NOT NULL DEFAULT '0',
  `rebotes` int NOT NULL DEFAULT '0',
  `faltas` int NOT NULL DEFAULT '0',
  `minutos` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_estadisticas`),
  UNIQUE KEY `id_jugador` (`id_jugador`,`id_partido`),
  UNIQUE KEY `UKsro3814mb6fjnko5paypgqusb` (`id_jugador`,`id_partido`),
  KEY `id_partido` (`id_partido`),
  CONSTRAINT `estadisticas_partido_ibfk_1` FOREIGN KEY (`id_jugador`) REFERENCES `jugador` (`id_jugador`),
  CONSTRAINT `estadisticas_partido_ibfk_2` FOREIGN KEY (`id_partido`) REFERENCES `partido` (`id_partido`),
  CONSTRAINT `estadisticas_partido_chk_1` CHECK ((`puntos` >= 0)),
  CONSTRAINT `estadisticas_partido_chk_2` CHECK ((`asistencias` >= 0)),
  CONSTRAINT `estadisticas_partido_chk_3` CHECK ((`rebotes` >= 0)),
  CONSTRAINT `estadisticas_partido_chk_4` CHECK ((`faltas` >= 0)),
  CONSTRAINT `estadisticas_partido_chk_5` CHECK ((`minutos` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=415 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `estadisticas_partido` WRITE;
/*!40000 ALTER TABLE `estadisticas_partido` DISABLE KEYS */;
INSERT INTO `estadisticas_partido` (`id_estadisticas`, `id_jugador`, `id_partido`, `puntos`, `asistencias`, `rebotes`, `faltas`, `minutos`) VALUES (1,1,1,11,3,5,2,22),(2,2,1,11,4,7,3,25),(3,3,1,12,5,9,4,28),(4,4,1,13,6,11,0,31),(5,5,1,14,7,3,1,34),(6,6,1,15,8,5,2,18),(7,7,1,17,1,7,3,21),(8,8,1,17,2,9,4,24),(9,9,1,18,3,11,0,27),(10,10,1,50,4,3,1,30),(11,11,1,18,5,5,2,33),(12,12,1,19,6,7,3,36),(13,13,1,6,7,9,4,20),(14,14,1,7,8,11,0,23),(15,15,1,8,1,3,1,26),(16,16,1,9,2,5,2,29),(17,17,1,10,3,7,3,32),(18,18,1,11,4,9,4,35),(19,1,2,11,4,6,3,23),(20,2,2,12,5,8,4,26),(21,3,2,13,6,10,0,29),(22,4,2,14,7,2,1,32),(23,5,2,15,8,4,2,35),(24,6,2,16,1,6,3,19),(25,7,2,17,2,8,4,22),(26,8,2,18,3,10,0,25),(27,9,2,19,4,2,1,28),(28,19,2,13,6,2,1,20),(29,20,2,14,7,4,2,23),(30,21,2,15,8,6,3,26),(31,22,2,16,1,8,4,29),(32,23,2,17,2,10,0,32),(33,24,2,18,3,2,1,35),(34,25,2,19,4,4,2,19),(35,26,2,6,5,6,3,22),(36,27,2,7,6,8,4,25),(37,1,3,12,5,7,4,24),(38,2,3,13,6,9,0,27),(39,3,3,14,7,11,1,30),(40,4,3,15,8,3,2,33),(41,5,3,16,1,5,3,36),(42,6,3,17,2,7,4,20),(43,7,3,18,3,9,0,23),(44,8,3,19,4,11,1,26),(45,9,3,20,5,3,2,29),(46,28,3,9,8,11,1,29),(47,29,3,10,1,3,2,32),(48,30,3,11,2,5,3,35),(49,31,3,12,3,7,4,19),(50,32,3,13,4,9,0,22),(51,33,3,14,5,11,1,25),(52,34,3,15,6,3,2,28),(53,35,3,16,7,5,3,31),(54,36,3,17,8,7,4,34),(55,1,4,13,6,8,0,25),(56,2,4,14,7,10,1,28),(57,3,4,15,8,2,2,31),(58,4,4,16,1,4,3,34),(59,5,4,17,2,6,4,18),(60,6,4,18,3,8,0,21),(61,7,4,19,4,10,1,24),(62,8,4,20,5,2,2,27),(63,9,4,21,6,4,3,30),(64,37,4,19,2,10,1,19),(65,38,4,6,3,2,2,22),(66,39,4,7,4,4,3,25),(67,40,4,8,5,6,4,28),(68,41,4,9,6,8,0,31),(69,42,4,10,7,10,1,34),(70,43,4,11,8,2,2,18),(71,44,4,12,1,4,3,21),(72,45,4,13,2,6,4,24),(73,1,5,14,7,9,1,26),(74,2,5,15,8,11,2,29),(75,3,5,16,1,3,3,32),(76,4,5,17,2,5,4,35),(77,5,5,18,3,7,0,19),(78,6,5,19,4,9,1,22),(79,7,5,20,5,11,2,25),(80,8,5,21,6,3,3,28),(81,9,5,22,7,5,4,31),(82,46,5,15,4,9,1,28),(83,47,5,16,5,11,2,31),(84,48,5,17,6,3,3,34),(85,49,5,18,7,5,4,18),(86,50,5,19,8,7,0,21),(87,51,5,6,1,9,1,24),(88,52,5,7,2,11,2,27),(89,53,5,8,3,3,3,30),(90,54,5,9,4,5,4,33),(91,1,6,15,8,10,2,27),(92,2,6,16,1,2,3,30),(93,3,6,17,2,4,4,33),(94,4,6,18,3,6,0,36),(95,5,6,19,4,8,1,20),(96,6,6,20,5,10,2,23),(97,7,6,21,6,2,3,26),(98,8,6,22,7,4,4,29),(99,9,6,8,8,6,0,32),(100,55,6,11,6,8,1,18),(101,56,6,12,7,10,2,21),(102,57,6,13,8,2,3,24),(103,58,6,14,1,4,4,27),(104,59,6,15,2,6,0,30),(105,60,6,16,3,8,1,33),(106,61,6,17,4,10,2,36),(107,62,6,18,5,2,3,20),(108,63,6,19,6,4,4,23),(109,1,7,16,1,11,3,28),(110,2,7,17,2,3,4,31),(111,3,7,18,3,5,0,34),(112,4,7,19,4,7,1,18),(113,5,7,20,5,9,2,21),(114,6,7,21,6,11,3,24),(115,7,7,22,7,3,4,27),(116,8,7,8,8,5,0,30),(117,9,7,9,1,7,1,33),(118,64,7,7,8,7,1,27),(119,65,7,8,1,9,2,30),(120,66,7,9,2,11,3,33),(121,67,7,10,3,3,4,36),(122,68,7,11,4,5,0,20),(123,69,7,12,5,7,1,23),(124,70,7,13,6,9,2,26),(125,71,7,14,7,11,3,29),(126,72,7,15,8,3,4,32),(127,1,8,17,2,2,4,29),(128,2,8,18,3,4,0,32),(129,3,8,19,4,6,1,35),(130,4,8,20,5,8,2,19),(131,5,8,21,6,10,3,22),(132,6,8,22,7,2,4,25),(133,7,8,8,8,4,0,28),(134,8,8,9,1,6,1,31),(135,9,8,10,2,8,2,34),(136,73,8,17,2,6,1,36),(137,74,8,18,3,8,2,20),(138,75,8,19,4,10,3,23),(139,76,8,6,5,2,4,26),(140,77,8,7,6,4,0,29),(141,78,8,8,7,6,1,32),(142,79,8,9,8,8,2,35),(143,80,8,10,1,10,3,19),(144,81,8,11,2,2,4,22),(145,1,9,18,3,3,0,30),(146,2,9,19,4,5,1,33),(147,3,9,20,5,7,2,36),(148,4,9,21,6,9,3,20),(149,5,9,22,7,11,4,23),(150,6,9,8,8,3,0,26),(151,7,9,9,1,5,1,29),(152,8,9,10,2,7,2,32),(153,9,9,11,3,9,3,35),(154,82,9,13,4,5,1,26),(155,83,9,14,5,7,2,29),(156,84,9,15,6,9,3,32),(157,85,9,16,7,11,4,35),(158,86,9,17,8,3,0,19),(159,87,9,18,1,5,1,22),(160,88,9,19,2,7,2,25),(161,89,9,6,3,9,3,28),(162,90,9,7,4,11,4,31),(163,10,10,13,5,2,0,20),(164,11,10,14,6,4,1,23),(165,12,10,15,7,6,2,26),(166,13,10,16,8,8,3,29),(167,14,10,17,1,10,4,32),(168,15,10,18,2,2,0,35),(169,16,10,19,3,4,1,19),(170,17,10,20,4,6,2,22),(171,18,10,21,5,8,3,25),(172,19,10,7,6,10,4,28),(173,20,10,8,7,2,0,31),(174,21,10,9,8,4,1,34),(175,22,10,10,1,6,2,18),(176,23,10,11,2,8,3,21),(177,24,10,12,3,10,4,24),(178,25,10,13,4,2,0,27),(179,26,10,14,5,4,1,30),(180,27,10,15,6,6,2,33),(181,10,11,14,6,3,1,21),(182,11,11,15,7,5,2,24),(183,12,11,16,8,7,3,27),(184,13,11,17,1,9,4,30),(185,14,11,18,2,11,0,33),(186,15,11,19,3,3,1,36),(187,16,11,20,4,5,2,20),(188,17,11,21,5,7,3,23),(189,18,11,22,6,9,4,26),(190,28,11,17,8,9,4,18),(191,29,11,18,1,11,0,21),(192,30,11,19,2,3,1,24),(193,31,11,6,3,5,2,27),(194,32,11,7,4,7,3,30),(195,33,11,8,5,9,4,33),(196,34,11,9,6,11,0,36),(197,35,11,10,7,3,1,20),(198,36,11,11,8,5,2,23),(199,10,12,15,7,4,2,22),(200,11,12,16,8,6,3,25),(201,12,12,17,1,8,4,28),(202,13,12,18,2,10,0,31),(203,14,12,19,3,2,1,34),(204,15,12,20,4,4,2,18),(205,16,12,21,5,6,3,21),(206,17,12,22,6,8,4,24),(207,18,12,8,7,10,0,27),(208,37,12,13,2,8,4,27),(209,38,12,14,3,10,0,30),(210,39,12,15,4,2,1,33),(211,40,12,16,5,4,2,36),(212,41,12,17,6,6,3,20),(213,42,12,18,7,8,4,23),(214,43,12,19,8,10,0,26),(215,44,12,6,1,2,1,29),(216,45,12,7,2,4,2,32),(217,10,13,16,8,5,3,23),(218,11,13,17,1,7,4,26),(219,12,13,18,2,9,0,29),(220,13,13,19,3,11,1,32),(221,14,13,20,4,3,2,35),(222,15,13,21,5,5,3,19),(223,16,13,22,6,7,4,22),(224,17,13,8,7,9,0,25),(225,18,13,9,8,11,1,28),(226,46,13,9,4,7,4,36),(227,47,13,10,5,9,0,20),(228,48,13,11,6,11,1,23),(229,49,13,12,7,3,2,26),(230,50,13,13,8,5,3,29),(231,51,13,14,1,7,4,32),(232,52,13,15,2,9,0,35),(233,53,13,16,3,11,1,19),(234,54,13,17,4,3,2,22),(235,10,14,17,1,6,4,24),(236,11,14,18,2,8,0,27),(237,12,14,19,3,10,1,30),(238,13,14,20,4,2,2,33),(239,14,14,21,5,4,3,36),(240,15,14,22,6,6,4,20),(241,16,14,8,7,8,0,23),(242,17,14,9,8,10,1,26),(243,18,14,10,1,2,2,29),(244,55,14,19,6,6,4,26),(245,56,14,6,7,8,0,29),(246,57,14,7,8,10,1,32),(247,58,14,8,1,2,2,35),(248,59,14,9,2,4,3,19),(249,60,14,10,3,6,4,22),(250,61,14,11,4,8,0,25),(251,62,14,12,5,10,1,28),(252,63,14,13,6,2,2,31),(253,10,15,18,2,7,0,25),(254,11,15,19,3,9,1,28),(255,12,15,20,4,11,2,31),(256,13,15,21,5,3,3,34),(257,14,15,22,6,5,4,18),(258,15,15,8,7,7,0,21),(259,16,15,9,8,9,1,24),(260,17,15,10,1,11,2,27),(261,18,15,11,2,3,3,30),(262,64,15,15,8,5,4,35),(263,65,15,16,1,7,0,19),(264,66,15,17,2,9,1,22),(265,67,15,18,3,11,2,25),(266,68,15,19,4,3,3,28),(267,69,15,6,5,5,4,31),(268,70,15,7,6,7,0,34),(269,71,15,8,7,9,1,18),(270,72,15,9,8,11,2,21),(271,10,16,19,3,8,1,26),(272,11,16,20,4,10,2,29),(273,12,16,21,5,2,3,32),(274,13,16,22,6,4,4,35),(275,14,16,8,7,6,0,19),(276,15,16,9,8,8,1,22),(277,16,16,10,1,10,2,25),(278,17,16,11,2,2,3,28),(279,18,16,12,3,4,4,31),(280,73,16,11,2,4,4,25),(281,74,16,12,3,6,0,28),(282,75,16,13,4,8,1,31),(283,76,16,14,5,10,2,34),(284,77,16,15,6,2,3,18),(285,78,16,16,7,4,4,21),(286,79,16,17,8,6,0,24),(287,80,16,18,1,8,1,27),(288,81,16,19,2,10,2,30),(289,10,17,20,4,9,2,27),(290,11,17,21,5,11,3,30),(291,12,17,22,6,3,4,33),(292,13,17,8,7,5,0,36),(293,14,17,9,8,7,1,20),(294,15,17,10,1,9,2,23),(295,16,17,11,2,11,3,26),(296,17,17,12,3,3,4,29),(297,18,17,13,4,5,0,32),(298,82,17,7,4,3,4,34),(299,83,17,8,5,5,0,18),(300,84,17,9,6,7,1,21),(301,85,17,10,7,9,2,24),(302,86,17,11,8,11,3,27),(303,87,17,12,1,3,4,30),(304,88,17,13,2,5,0,33),(305,89,17,14,3,7,1,36),(306,90,17,15,4,9,2,20),(307,19,18,15,6,8,2,36),(308,20,18,16,7,10,3,20),(309,21,18,17,8,2,4,23),(310,22,18,18,1,4,0,26),(311,23,18,19,2,6,1,29),(312,24,18,20,3,8,2,32),(313,25,18,21,4,10,3,35),(314,26,18,22,5,2,4,19),(315,27,18,8,6,4,0,22),(316,28,18,10,7,6,1,25),(317,29,18,11,8,8,2,28),(318,30,18,12,1,10,3,31),(319,31,18,13,2,2,4,34),(320,32,18,14,3,4,0,18),(321,33,18,15,4,6,1,21),(322,34,18,16,5,8,2,24),(323,35,18,17,6,10,3,27),(324,36,18,18,7,2,4,30),(325,19,19,16,7,9,3,18),(326,20,19,17,8,11,4,21),(327,21,19,18,1,3,0,24),(328,22,19,19,2,5,1,27),(329,23,19,20,3,7,2,30),(330,24,19,21,4,9,3,33),(331,25,19,22,5,11,4,36),(332,26,19,8,6,3,0,20),(333,27,19,9,7,5,1,23),(334,37,19,6,1,5,1,34),(335,38,19,7,2,7,2,18),(336,39,19,8,3,9,3,21),(337,40,19,9,4,11,4,24),(338,41,19,10,5,3,0,27),(339,42,19,11,6,5,1,30),(340,43,19,12,7,7,2,33),(341,44,19,13,8,9,3,36),(342,45,19,14,1,11,4,20),(343,19,20,17,8,10,4,19),(344,20,20,18,1,2,0,22),(345,21,20,19,2,4,1,25),(346,22,20,20,3,6,2,28),(347,23,20,21,4,8,3,31),(348,24,20,22,5,10,4,34),(349,25,20,8,6,2,0,18),(350,26,20,9,7,4,1,21),(351,27,20,10,8,6,2,24),(352,46,20,16,3,4,1,24),(353,47,20,17,4,6,2,27),(354,48,20,18,5,8,3,30),(355,49,20,19,6,10,4,33),(356,50,20,6,7,2,0,36),(357,51,20,7,8,4,1,20),(358,52,20,8,1,6,2,23),(359,53,20,9,2,8,3,26),(360,54,20,10,3,10,4,29),(361,19,21,18,1,11,0,20),(362,20,21,19,2,3,1,23),(363,21,21,20,3,5,2,26),(364,22,21,21,4,7,3,29),(365,23,21,22,5,9,4,32),(366,24,21,8,6,11,0,35),(367,25,21,9,7,3,1,19),(368,26,21,12,8,5,2,22),(369,27,21,11,1,7,3,25),(370,55,21,12,5,3,1,33),(371,56,21,13,6,5,2,36),(372,57,21,14,7,7,3,20),(373,58,21,15,8,9,4,23),(374,59,21,16,1,11,0,26),(375,60,21,17,2,3,1,29),(376,61,21,18,3,5,2,32),(377,62,21,19,4,7,3,35),(378,63,21,6,5,9,4,19),(379,19,22,19,2,2,1,21),(380,20,22,20,3,4,2,24),(381,21,22,21,4,6,3,27),(382,22,22,22,5,8,4,30),(383,23,22,8,6,10,0,33),(384,24,22,9,7,2,1,36),(385,25,22,10,8,4,2,20),(386,26,22,11,1,6,3,23),(387,27,22,12,2,8,4,26),(388,64,22,8,7,2,1,23),(389,65,22,9,8,4,2,26),(390,66,22,10,1,6,3,29),(391,67,22,11,2,8,4,32),(392,68,22,12,3,10,0,35),(393,69,22,13,4,2,1,19),(394,70,22,14,5,4,2,22),(395,71,22,15,6,6,3,25),(396,72,22,16,7,8,4,28),(397,19,23,20,3,3,2,22),(398,20,23,21,4,5,3,25),(399,21,23,22,5,7,4,28),(400,22,23,8,6,9,0,31),(401,23,23,9,7,11,1,34),(402,24,23,10,8,3,2,18),(403,25,23,11,1,5,3,21),(404,26,23,12,2,7,4,24),(405,27,23,13,3,9,0,27),(406,73,23,18,1,11,1,32),(407,74,23,19,2,3,2,35),(408,75,23,6,3,5,3,19),(409,76,23,7,4,7,4,22),(410,77,23,8,5,9,0,25),(411,78,23,9,6,11,1,28),(412,79,23,10,7,3,2,31),(413,80,23,11,8,5,3,34),(414,81,23,12,1,7,4,18);
/*!40000 ALTER TABLE `estadisticas_partido` ENABLE KEYS */;
UNLOCK TABLES;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud_contacto` (
  `id_solicitud` int NOT NULL AUTO_INCREMENT,
  `correo_contacto` varchar(150) NOT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `mensaje` varchar(500) NOT NULL,
  `nombre_contacto` varchar(100) NOT NULL,
  `telefono_contacto` varchar(25) DEFAULT NULL,
  `id_usuario_creador` int DEFAULT NULL,
  PRIMARY KEY (`id_solicitud`),
  KEY `FK9cy3ldmhq6i8et6xci01x4sbe` (`id_usuario_creador`),
  CONSTRAINT `FK9cy3ldmhq6i8et6xci01x4sbe` FOREIGN KEY (`id_usuario_creador`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `solicitud_contacto` WRITE;
/*!40000 ALTER TABLE `solicitud_contacto` DISABLE KEYS */;
INSERT INTO `solicitud_contacto` (`id_solicitud`, `correo_contacto`, `fecha_creacion`, `mensaje`, `nombre_contacto`, `telefono_contacto`, `id_usuario_creador`) VALUES (1,'contacto@clubsanlucar.es','2026-04-22 19:19:58.000000','Nos gustaria inscribirnos en la proxima temporada.','Club Sanlucar','600123123',2),(2,'deporte@mairena.es','2026-04-22 19:19:58.000000','Queremos informacion sobre plazas disponibles para la liga.','Escuela Mairena','600234234',NULL),(3,'juventud@gines.es','2026-04-22 19:19:58.000000','Solicitamos informacion para colaborar con la competicion local.','Ayuntamiento de Gines','600345345',NULL);
/*!40000 ALTER TABLE `solicitud_contacto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

