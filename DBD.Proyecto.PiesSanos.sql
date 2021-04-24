-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: piessanos
-- ------------------------------------------------------
-- Server version	8.0.23

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

--
-- Table structure for table `cita`
--

DROP TABLE IF EXISTS `cita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cita` (
  `numCita` int NOT NULL,
  `hora` time NOT NULL,
  `a_domicilio` tinyint(1) NOT NULL,
  `domicilio` varchar(255) DEFAULT NULL,
  `id_paciente` varchar(30) NOT NULL,
  PRIMARY KEY (`numCita`),
  KEY `id_paciente` (`id_paciente`),
  CONSTRAINT `cita_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cita`
--

LOCK TABLES `cita` WRITE;
/*!40000 ALTER TABLE `cita` DISABLE KEYS */;
/*!40000 ALTER TABLE `cita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consulta`
--

DROP TABLE IF EXISTS `consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consulta` (
  `numConsulta` int NOT NULL,
  `indicaciones` varchar(255) NOT NULL,
  `observaciones` varchar(255) NOT NULL,
  `temperatura_covid` decimal(4,2) NOT NULL,
  `costo_total` float NOT NULL,
  `id_expediente` varchar(30) NOT NULL,
  `id_paciente` varchar(30) NOT NULL,
  `numCita` int NOT NULL,
  PRIMARY KEY (`numConsulta`),
  UNIQUE KEY `indexNumCita` (`numCita`),
  KEY `id_expediente` (`id_expediente`),
  KEY `id_paciente` (`id_paciente`),
  CONSTRAINT `consulta_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id`),
  CONSTRAINT `consulta_ibfk_2` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`),
  CONSTRAINT `consulta_ibfk_3` FOREIGN KEY (`numCita`) REFERENCES `cita` (`numCita`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consulta`
--

LOCK TABLES `consulta` WRITE;
/*!40000 ALTER TABLE `consulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `consulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expediente`
--

DROP TABLE IF EXISTS `expediente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expediente` (
  `id` varchar(30) NOT NULL,
  `fecha_creacion` date NOT NULL,
  `id_paciente` varchar(30) NOT NULL,
  `numCita` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `indexIdPaciente` (`id_paciente`),
  KEY `numCita` (`numCita`),
  CONSTRAINT `expediente_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`),
  CONSTRAINT `expediente_ibfk_2` FOREIGN KEY (`numCita`) REFERENCES `cita` (`numCita`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expediente`
--

LOCK TABLES `expediente` WRITE;
/*!40000 ALTER TABLE `expediente` DISABLE KEYS */;
/*!40000 ALTER TABLE `expediente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expediente_alergia`
--

DROP TABLE IF EXISTS `expediente_alergia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expediente_alergia` (
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `id_expediente` varchar(30) NOT NULL,
  PRIMARY KEY (`nombre`),
  KEY `id_expediente` (`id_expediente`),
  CONSTRAINT `expediente_alergia_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expediente_alergia`
--

LOCK TABLES `expediente_alergia` WRITE;
/*!40000 ALTER TABLE `expediente_alergia` DISABLE KEYS */;
/*!40000 ALTER TABLE `expediente_alergia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expediente_enfermedad`
--

DROP TABLE IF EXISTS `expediente_enfermedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expediente_enfermedad` (
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `id_expediente` varchar(30) NOT NULL,
  PRIMARY KEY (`nombre`),
  KEY `id_expediente` (`id_expediente`),
  CONSTRAINT `expediente_enfermedad_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expediente_enfermedad`
--

LOCK TABLES `expediente_enfermedad` WRITE;
/*!40000 ALTER TABLE `expediente_enfermedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `expediente_enfermedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expediente_medicamentoprescrito`
--

DROP TABLE IF EXISTS `expediente_medicamentoprescrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expediente_medicamentoprescrito` (
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `id_expediente` varchar(30) NOT NULL,
  PRIMARY KEY (`nombre`),
  KEY `id_expediente` (`id_expediente`),
  CONSTRAINT `expediente_medicamentoprescrito_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expediente_medicamentoprescrito`
--

LOCK TABLES `expediente_medicamentoprescrito` WRITE;
/*!40000 ALTER TABLE `expediente_medicamentoprescrito` DISABLE KEYS */;
/*!40000 ALTER TABLE `expediente_medicamentoprescrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiales_consulta`
--

DROP TABLE IF EXISTS `materiales_consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiales_consulta` (
  `numInventario` int NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `cantidadInventario` int NOT NULL,
  PRIMARY KEY (`numInventario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales_consulta`
--

LOCK TABLES `materiales_consulta` WRITE;
/*!40000 ALTER TABLE `materiales_consulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `materiales_consulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamento`
--

DROP TABLE IF EXISTS `medicamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamento` (
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` float NOT NULL,
  `cantidadInventario` int NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamento`
--

LOCK TABLES `medicamento` WRITE;
/*!40000 ALTER TABLE `medicamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medico`
--

DROP TABLE IF EXISTS `medico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medico` (
  `cedula_profesional` varchar(50) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `nomPaterno` varchar(50) NOT NULL,
  `nomMaterno` varchar(50) NOT NULL,
  `calle` varchar(50) NOT NULL,
  `num_ext` int NOT NULL,
  `num_int` int DEFAULT NULL,
  `colonia` varchar(50) NOT NULL,
  `codigoPostal` int NOT NULL,
  `ciudad` varchar(50) NOT NULL,
  `fecha_registro` date NOT NULL,
  `contrasena` varchar(50) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `isAdmin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`cedula_profesional`),
  UNIQUE KEY `indexUsuario` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medico`
--

LOCK TABLES `medico` WRITE;
/*!40000 ALTER TABLE `medico` DISABLE KEYS */;
/*!40000 ALTER TABLE `medico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medico_expediente`
--

DROP TABLE IF EXISTS `medico_expediente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medico_expediente` (
  `id_expediente` varchar(30) NOT NULL,
  `cedula_profesional` varchar(50) NOT NULL,
  `fecha_edicion` date DEFAULT NULL,
  PRIMARY KEY (`id_expediente`,`cedula_profesional`),
  KEY `cedula_profesional` (`cedula_profesional`),
  CONSTRAINT `medico_expediente_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id`),
  CONSTRAINT `medico_expediente_ibfk_2` FOREIGN KEY (`cedula_profesional`) REFERENCES `medico` (`cedula_profesional`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medico_expediente`
--

LOCK TABLES `medico_expediente` WRITE;
/*!40000 ALTER TABLE `medico_expediente` DISABLE KEYS */;
/*!40000 ALTER TABLE `medico_expediente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medico_paciente`
--

DROP TABLE IF EXISTS `medico_paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medico_paciente` (
  `id_paciente` varchar(30) NOT NULL,
  `cedula_profesional` varchar(50) NOT NULL,
  PRIMARY KEY (`id_paciente`,`cedula_profesional`),
  KEY `cedula_profesional` (`cedula_profesional`),
  CONSTRAINT `medico_paciente_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`),
  CONSTRAINT `medico_paciente_ibfk_2` FOREIGN KEY (`cedula_profesional`) REFERENCES `medico` (`cedula_profesional`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medico_paciente`
--

LOCK TABLES `medico_paciente` WRITE;
/*!40000 ALTER TABLE `medico_paciente` DISABLE KEYS */;
/*!40000 ALTER TABLE `medico_paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medico_telefono`
--

DROP TABLE IF EXISTS `medico_telefono`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medico_telefono` (
  `numTelefono` int NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `cedula_profesional` varchar(50) NOT NULL,
  PRIMARY KEY (`numTelefono`),
  KEY `cedula_profesional` (`cedula_profesional`),
  CONSTRAINT `medico_telefono_ibfk_1` FOREIGN KEY (`cedula_profesional`) REFERENCES `medico` (`cedula_profesional`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medico_telefono`
--

LOCK TABLES `medico_telefono` WRITE;
/*!40000 ALTER TABLE `medico_telefono` DISABLE KEYS */;
/*!40000 ALTER TABLE `medico_telefono` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `id` varchar(30) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `nomPaterno` varchar(50) NOT NULL,
  `nomMaterno` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente_telefono`
--

DROP TABLE IF EXISTS `paciente_telefono`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente_telefono` (
  `numTelefono` int NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `id_paciente` varchar(30) NOT NULL,
  PRIMARY KEY (`numTelefono`),
  KEY `id_paciente` (`id_paciente`),
  CONSTRAINT `paciente_telefono_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente_telefono`
--

LOCK TABLES `paciente_telefono` WRITE;
/*!40000 ALTER TABLE `paciente_telefono` DISABLE KEYS */;
/*!40000 ALTER TABLE `paciente_telefono` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promocion`
--

DROP TABLE IF EXISTS `promocion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promocion` (
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `dia` int NOT NULL,
  `mes` varchar(25) NOT NULL,
  `porcentaje_descuento` float NOT NULL,
  `clave_tratamiento` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `clave_tratamiento` (`clave_tratamiento`),
  CONSTRAINT `promocion_ibfk_1` FOREIGN KEY (`clave_tratamiento`) REFERENCES `tratamiento` (`clave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promocion`
--

LOCK TABLES `promocion` WRITE;
/*!40000 ALTER TABLE `promocion` DISABLE KEYS */;
/*!40000 ALTER TABLE `promocion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `id` varchar(30) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `calle` varchar(50) NOT NULL,
  `num_ext` int NOT NULL,
  `num_int` int DEFAULT NULL,
  `colonia` varchar(50) NOT NULL,
  `codigoPostal` int NOT NULL,
  `ciudad` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor_materialesconsulta`
--

DROP TABLE IF EXISTS `proveedor_materialesconsulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor_materialesconsulta` (
  `numInventario` int NOT NULL,
  `id_proveedor` varchar(30) NOT NULL,
  `fecha_venta` date NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`numInventario`,`id_proveedor`),
  KEY `id_proveedor` (`id_proveedor`),
  CONSTRAINT `proveedor_materialesconsulta_ibfk_1` FOREIGN KEY (`numInventario`) REFERENCES `materiales_consulta` (`numInventario`),
  CONSTRAINT `proveedor_materialesconsulta_ibfk_2` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor_materialesconsulta`
--

LOCK TABLES `proveedor_materialesconsulta` WRITE;
/*!40000 ALTER TABLE `proveedor_materialesconsulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `proveedor_materialesconsulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor_medicamento`
--

DROP TABLE IF EXISTS `proveedor_medicamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor_medicamento` (
  `codigo_medicamento` varchar(50) NOT NULL,
  `id_proveedor` varchar(30) NOT NULL,
  `precio_proveedor` float NOT NULL,
  `fecha_venta` date NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`codigo_medicamento`,`id_proveedor`),
  KEY `id_proveedor` (`id_proveedor`),
  CONSTRAINT `proveedor_medicamento_ibfk_1` FOREIGN KEY (`codigo_medicamento`) REFERENCES `medicamento` (`codigo`),
  CONSTRAINT `proveedor_medicamento_ibfk_2` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor_medicamento`
--

LOCK TABLES `proveedor_medicamento` WRITE;
/*!40000 ALTER TABLE `proveedor_medicamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `proveedor_medicamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tratamiento`
--

DROP TABLE IF EXISTS `tratamiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tratamiento` (
  `clave` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` float NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  PRIMARY KEY (`clave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tratamiento`
--

LOCK TABLES `tratamiento` WRITE;
/*!40000 ALTER TABLE `tratamiento` DISABLE KEYS */;
/*!40000 ALTER TABLE `tratamiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tratamiento_consulta`
--

DROP TABLE IF EXISTS `tratamiento_consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tratamiento_consulta` (
  `clave_tratamiento` varchar(50) NOT NULL,
  `numConsulta` int NOT NULL,
  PRIMARY KEY (`clave_tratamiento`,`numConsulta`),
  KEY `numConsulta` (`numConsulta`),
  CONSTRAINT `tratamiento_consulta_ibfk_1` FOREIGN KEY (`clave_tratamiento`) REFERENCES `tratamiento` (`clave`),
  CONSTRAINT `tratamiento_consulta_ibfk_2` FOREIGN KEY (`numConsulta`) REFERENCES `consulta` (`numConsulta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tratamiento_consulta`
--

LOCK TABLES `tratamiento_consulta` WRITE;
/*!40000 ALTER TABLE `tratamiento_consulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tratamiento_consulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tratamiento_medicamento`
--

DROP TABLE IF EXISTS `tratamiento_medicamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tratamiento_medicamento` (
  `clave_tratamiento` varchar(50) NOT NULL,
  `codigo_medicamento` varchar(50) NOT NULL,
  PRIMARY KEY (`clave_tratamiento`,`codigo_medicamento`),
  KEY `codigo_medicamento` (`codigo_medicamento`),
  CONSTRAINT `tratamiento_medicamento_ibfk_1` FOREIGN KEY (`clave_tratamiento`) REFERENCES `tratamiento` (`clave`),
  CONSTRAINT `tratamiento_medicamento_ibfk_2` FOREIGN KEY (`codigo_medicamento`) REFERENCES `medicamento` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tratamiento_medicamento`
--

LOCK TABLES `tratamiento_medicamento` WRITE;
/*!40000 ALTER TABLE `tratamiento_medicamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `tratamiento_medicamento` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-18 15:12:32
