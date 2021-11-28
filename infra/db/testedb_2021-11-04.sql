# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 0.0.0.0 (MySQL 5.7.36)
# Database: testedb
# Generation Time: 2021-11-05 02:59:18 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Positions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Positions`;

CREATE TABLE `Positions` (
  `id` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `longitude` FLOAT(24) DEFAULT NULL,
  `latitude` FLOAT(24) DEFAULT NULL
);

-- LOCK TABLES `Positions` WRITE;
/*!40000 ALTER TABLE `Positions` DISABLE KEYS */;

INSERT INTO `Positions` (`id`, `date`, `longitude`, `latitude`)
VALUES
	('1111','2021-11-04',-23.0208077,-43.4683931),
	('1112','2021-11-04',-23.1208077,-43.4683931),
	('1111','2021-11-04',-23.0208077,-43.4683931),
	('1112','2021-11-04',-23.0008077,-41.4683931);
	
/*!40000 ALTER TABLE `Positions` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Fences
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Fences`;

CREATE TABLE `Fences` (
  `up` FLOAT(24) DEFAULT NULL,
  `right` FLOAT(24) DEFAULT NULL,
  `bottom` FLOAT(24) DEFAULT NULL,
  `left` FLOAT(24) DEFAULT NULL
);

-- LOCK TABLES `Fences` WRITE;
/*!40000 ALTER TABLE `Positions` DISABLE KEYS */;

INSERT INTO `Fences`(`up`, `right`, `bottom`, `left`)
VALUES
	(-22.927271836395280, -43.17771782650461, -22.946273929306827, -43.19634344082100);

/*!40000 ALTER TABLE `Positions` ENABLE KEYS */;
UNLOCK TABLES;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
