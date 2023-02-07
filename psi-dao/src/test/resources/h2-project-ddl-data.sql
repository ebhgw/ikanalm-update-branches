-- --------------------------------------------------------
-- Host:                         sqlvaridatadr.rmasede.grma.net
-- Versione server:              Microsoft SQL Server 2008 R2 (SP3) - 10.50.6220.0
-- S.O. server:                  Windows NT 6.1 <X64> (Build 7601: Service Pack 1)
-- HeidiSQL Versione:            9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dump della struttura di tabella IKALM_APP.PROJECT
CREATE TABLE IF NOT EXISTS "PROJECT" (
	"OID" INT(10,0) NOT NULL,
	"NAME" VARCHAR(50) NOT NULL,
	"DESCRIPTION" VARCHAR(512) NOT NULL,
	"VCRPROJECTNAME" VARCHAR(50) NULL DEFAULT NULL,
	"BUILDSCRIPT" VARCHAR(255) NULL DEFAULT NULL,
	"DEPLOYSCRIPT" VARCHAR(255) NULL DEFAULT NULL,
	"TESTSCRIPT" VARCHAR(255) NULL DEFAULT NULL,
	"BUILDTOOLTYPE" INT(10,0) NOT NULL,
	"DEPLOYTOOLTYPE" INT(10,0) NULL DEFAULT NULL,
	"TESTTOOLTYPE" INT(10,0) NULL DEFAULT NULL,
	"VCROID" INT(10,0) NULL DEFAULT NULL,
	"ISSUETRACKINGSYSTEMOID" INT(10,0) NULL DEFAULT NULL,
	"LOCKED" BIT NOT NULL,
	"PROJECTTYPE" INT(10,0) NOT NULL,
	"USERGROUPOID" INT(10,0) NULL DEFAULT NULL,
	"ADMINGROUPOID" INT(10,0) NULL DEFAULT NULL,
	"ARCHIVED" BIT NULL DEFAULT NULL,
	"ARCHIVEDDATETIME" DATETIME(3) NULL DEFAULT NULL,
	"VERSION" INT(10,0) NOT NULL,
	PRIMARY KEY ("OID")
);

-- Dump dei dati della tabella IKALM_APP.PROJECT: -1 rows
/*!40000 ALTER TABLE "PROJECT" DISABLE KEYS */;
INSERT INTO "PROJECT" ("OID", "NAME", "DESCRIPTION", "VCRPROJECTNAME", "BUILDSCRIPT", "DEPLOYSCRIPT", "TESTSCRIPT", "BUILDTOOLTYPE", "DEPLOYTOOLTYPE", "TESTTOOLTYPE", "VCROID", "ISSUETRACKINGSYSTEMOID", "LOCKED", "PROJECTTYPE", "USERGROUPOID", "ADMINGROUPOID", "ARCHIVED", "ARCHIVEDDATETIME", "VERSION") VALUES
	(1, 'ALM_Custom', 'IKAN ALM System Customization', 'ALM_Custom', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 1, NULL, 'False', 0, NULL, NULL, 'False', NULL, 8),
	(2, 'RMAZOS_DEFAULT', 'Default project for RMA z/OS', 'DEFAULT', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 1, NULL, 'True', 1, 12, 12, 'False', NULL, 1),
	(3, 'DW01', 'Data Warehouse DISMESSO febbraio 2017', 'DW01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 34, 12, 'False', NULL, 40),
	(4, 'EURO', 'EURO project for RELEASE management. Per gestire Deploy in Pre-Produzione e Deploy in Produzione di un intero Projects Stream di Release', 'EURO', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 0, 18, 12, 'False', NULL, 20),
	(5, 'DW11', 'Data Warehouse con diversi DB2 rispetto a DW01 - DISMESSO febbraio 2017', 'DW11', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 34, 12, 'False', NULL, 22),
	(6, 'SR01', 'Data Warehouse SR01 DISMESSO febbraio 2017', 'SR01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 34, 12, 'False', NULL, 14),
	(7, 'SR11', 'Data Warehouse SR11 DISMESSO febbraio 2017', 'SR11', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 34, 12, 'False', NULL, 14),
	(9, 'G901', 'GIAUID Diagnostico AUI', 'G901', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 35, 12, 'False', NULL, 13),
	(10, 'G301', 'GIANOS', 'G301', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 35, 12, 'False', NULL, 15),
	(12, 'AR01', 'ANTITERRORISMO', 'AR01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 35, 12, 'False', NULL, 14),
	(14, 'SE01', 'SERVIZIO', 'SE01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 36, 12, 'False', NULL, 13),
	(15, 'TA01', 'Tabelle', 'TA01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 36, 12, 'False', NULL, 11),
	(16, 'CE01SIGE', 'Cross Project CE01 for SIGEA', 'CE01SIGE', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 11, 12, 'False', NULL, 22),
	(17, 'CQ01', 'CQ01 COntabilità premi (sono solo 3 programmi!)', 'CQ01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 36, 12, 'False', NULL, 14),
	(18, 'VI01', 'Applicazioni VITA AP01-Vita Rapp.interb.dir, UV01-Utility Vita, VA01-Sistema Info Vita, VI01-Polizze vita, VR01-Vita Rendite, V101-Sistema Info Vita.', 'VI01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 33, 12, 'False', NULL, 19),
	(19, 'SZ01', 'SZ01 Servizi Sicurezza     SIGEA', 'SZ01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 18),
	(20, 'VC01', 'VC01 - Sistema Info Vita', 'VC01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 33, 12, 'False', NULL, 11),
	(21, 'SS01', 'SS01', 'SS01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 12),
	(22, 'GI01', 'GI01', 'GI01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 36, 12, 'False', NULL, 10),
	(23, 'MF01', 'MF01', 'MF01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 35, 12, 'False', NULL, 15),
	(24, 'CJ01', 'CJ01 jcl ', 'CJ01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'True', 1, 11, 12, 'False', NULL, 23),
	(25, 'OR01', 'OR01', 'OR01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 36, 12, 'False', NULL, 12),
	(26, 'TD01', 'TD01', 'TD01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 34, 12, 'False', NULL, 18),
	(27, 'TD51', 'TD51', 'TD51', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 34, 12, 'False', NULL, 11),
	(28, 'TR01', 'TR01', 'TR01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 36, 12, 'False', NULL, 11),
	(29, 'RMA_Copybooks', 'RMA_Copybooks technical Project', 'RMA_Copybooks', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'True', 1, 12, 12, 'False', NULL, 0),
	(30, 'CE01EURO', 'Cross Project CE01 for EURO part', 'CE01EURO', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 11, 12, 'False', NULL, 13),
	(31, 'SP01', 'Portafoglio', 'SP01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 14),
	(35, 'SG01', 'Servizi SIGEA', 'SG01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 9),
	(36, 'GG01', 'GP Regole', 'GG01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 19),
	(37, 'SC01', 'SIGEA', 'SC01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 8),
	(38, 'SV01', 'SIGEA', 'SV01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 7),
	(39, 'GP01', 'GPA Batch', 'GP01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 10),
	(40, 'SG51', 'SIGEA', 'SG51', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 8),
	(41, 'SV51', 'SIGEA Anagrafe di gruppo', 'SV51', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 37, 12, 'False', NULL, 9),
	(42, 'CJ01SIGE', 'CJ01 SIGEA', 'CJ01SIGE', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'True', 1, 38, 12, 'False', NULL, 14),
	(43, 'SIGE', 'SIGEA RELEASE', 'SIGE', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 0, 11, 12, 'False', NULL, 14),
	(44, 'EURO_Copybooks', 'Technical project for EURO Copybooks', 'EURO_Copybooks', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'True', 1, 12, 12, 'False', NULL, 2),
	(45, 'CK01', 'Utility di sistema', 'CK01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 4, NULL, 'False', 1, 40, 12, 'False', NULL, 8),
	(46, 'EE01', 'project di test sistemistico ambiente euro clonato da VC01 . Per ora punto a VC01 in SVM coi relativi branches', 'VC01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 12, 12, 'False', NULL, 6),
	(47, 'VITA', 'Rehosting VITA - inserire come User Access g_ikalm_life', 'VITA', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 6, NULL, 'False', 1, 33, 12, 'False', NULL, 38),
	(49, 'DIM', 'DIM - Progetto Pilota DataStage', 'DIM', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 42, 45, 'False', NULL, 11),
	(51, 'CJ01VITA', 'Jcl per VITA', 'CJ01VITA', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 6, NULL, 'True', 1, 11, 12, 'False', NULL, 28),
	(52, 'CZPOQ', 'Datastage CZPOQ', 'CZPOQ', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 50, 45, 'False', NULL, 4),
	(53, 'PassGenerico', 'Datastage PassGenerico', 'PassGenerico', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 59, 45, 'False', NULL, 4),
	(54, 'SPDanniAuto', 'Datastage SPDanniAuto', 'SPDanniAuto', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 65, 45, 'False', NULL, 6),
	(55, 'ReHostingDataSync', 'Datastage ReHostingDataSync', 'ReHostingDataSync', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 62, 45, 'False', NULL, 5),
	(57, 'er5days', 'Datastage er5days', 'er5days', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 68, 45, 'False', NULL, 4),
	(58, 'FunzioneAziendale', 'Datastage FunzioneAziendale', 'FunzioneAziendale', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 53, 45, 'False', NULL, 4),
	(59, 'BOA', 'Datastage BOA', 'BOA', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 47, 12, 'False', NULL, 5),
	(60, 'Grandine', 'Datastage Grandine', 'Grandine', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 56, 45, 'False', NULL, 4),
	(61, 'SigeaAnagrafe', 'Datastage SigeaAnagrafe', 'SigeaAnagrafe', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 71, 45, 'False', NULL, 4),
	(62, 'Miscellanea', 'Datastage Miscellanea', 'Miscellanea', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 74, 45, 'False', NULL, 6),
	(63, 'SVTabelleComuni', 'Datastage SVTabelleComuni', 'SVTabelleComuni', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 77, 45, 'False', NULL, 6),
	(64, 'Riassi', 'Datastage Riassi', 'Riassi', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 80, 45, 'False', NULL, 6),
	(65, 'SicsAlim', 'Datastage SicsAlim', 'SicsAlim', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 83, 45, 'False', NULL, 5),
	(66, 'PatrimonioImmobiliare', 'Datastage PatrimonioImmobiliare', 'PatrimonioImmobiliare', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 86, 45, 'False', NULL, 2),
	(67, 'DwAntiterro', 'Datastage DwAntiterro ', 'DwAntiterro', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 89, 45, 'False', NULL, 13),
	(68, 'DWSAP', 'Datastage DWSAP', 'DWSAP', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 92, 45, 'False', NULL, 6),
	(69, 'DWUtenze', 'Datastage DWUtenze', 'DWUtenze', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 95, 45, 'False', NULL, 5),
	(70, 'JA01VITA', 'Jcl rehostati in sh per VITA', 'JA01VITA', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 6, NULL, 'False', 1, 11, 12, 'False', NULL, 22),
	(71, 'PPsuOracle', 'PPsuOracle Datastage project', 'PPsuOracle', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 98, 45, 'False', NULL, 2),
	(72, 'SinistriDanni', 'Datastage SinistriDanni', 'SinistriDanni', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 101, 45, 'False', NULL, 1),
	(73, 'DWSolvency', 'Datastage DWSolvency', 'DWSolvency', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 104, 45, 'False', NULL, 2),
	(74, 'DataQualitySolvencyII', 'Datastage DataQualitySolvencyII', 'DataQualitySolvencyII', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 107, 45, 'False', NULL, 2),
	(75, 'ProjManOffice', 'Datastage ProjManOffice', 'ProjManOffice', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 110, 45, 'False', NULL, 1),
	(76, 'ConTe', 'Datastage ConTe', 'ConTe', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 113, 45, 'False', NULL, 1),
	(77, 'AnagrafeTributaria', 'Datastage Anagrafe Tributaria', 'AnagrafeTributaria', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 119, 12, 'False', NULL, 5),
	(78, 'EDW', 'Datastage EDV', 'EDW', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 116, 45, 'False', NULL, 2),
	(79, 'AdapterAnagrafe', 'Datastage AdapterAnagrafe
Duplicato ADAPTER_ANAGRAFE
Nascosto in attesa di verifiche
Rimuovere il 31/12/18 se non rimesso visibile', 'AdapterAnagrafe', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 122, 45, 'True', NULL, 3),
	(80, 'AdapterAuto', 'Datastage AdapterAuto
Duplicato ADAPTER_AUTO
Nascosto in attesa di verifiche
Rimuovere il 31/12/18 se non rimesso visibile', 'AdapterAuto', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 125, 45, 'True', NULL, 3),
	(81, 'AdapterSinistri', 'Datastage AdapterSinistri', 'AdapterSinistri', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 128, 45, 'True', NULL, 2),
	(82, 'AdapterVita', 'Datastage AdapterVita', 'AdapterVita', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 131, 45, 'True', NULL, 2),
	(83, 'ADAPTER_ANAGRAFE', 'Datastage ADAPTER_ANAGRAFE', 'AdapterAnagrafe', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 122, 12, 'False', NULL, 5),
	(84, 'ADAPTER_AUTO', 'Datastage ADAPTER_AUTO', 'AdapterAuto', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 125, 12, 'False', NULL, 3),
	(85, 'ADAPTER_SINISTRI', 'Datastage ADAPTER_SINISTRI', 'AdapterSinistri', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 128, 12, 'False', NULL, 4),
	(86, 'ADAPTER_VITA', 'Datastage ADAPTER_VITA', 'AdapterVita', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 7, NULL, 'False', 1, 131, 12, 'False', NULL, 3),
	(87, 'SP01OTX', 'SP01 rehosted in ambiente Oracle TUXEDO', 'SP01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 54),
	(88, 'SIGEOTX', 'Release SIGE Rehosted', 'SIGEOTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 0, 11, 12, 'False', NULL, 18),
	(89, 'SS01OTX', 'SS01 rehosted in ambiente Oracle TUXEDO', 'SS01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 35),
	(90, 'JA01SIGE', 'Jade Project per progetti SIGEA. Utilizza Jmodel e non Smodel (come fa JA01VITA) essi generano JCL e non KSH, i JCL sono poi gestiti dal TuxJES e translati al volo in Ksh', 'JA01SIGE', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 11, 12, 'False', NULL, 28),
	(91, 'JA01EURO', 'Jmodel EURO non vita (ovvero MF01, G30, G901, OR01 etc) migrazione da Jman a JADE  Utilizza Jmodel e non Smodel (come fa JA01VITA) essi generano JCL e non KSH, i JCL sono poi gestiti dal Mainframe come in CJ01', 'JA01EURO', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 3, NULL, 'False', 1, 11, 12, 'False', NULL, 16),
	(93, 'DQNTOTX', 'Gestione pacchetti Dataquant', 'DQNTOTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 4),
	(94, 'SV01OTX', 'Project SV01 in versione rehosting Tuxedo', 'SV01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 4),
	(95, 'SG01OTX', 'Project SG01 Anagrafica in versione rehosting Tuxedo', 'SG01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 2),
	(96, 'GG01OTX', 'Project GP01 Regole, in versione rehosting Tuxedo', 'GG01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 2),
	(97, 'GP01OTX', 'Project GPA (GP01) Batch in versione rehosting Tuxedo', 'GP01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 3),
	(98, 'SC01OTX', 'Project SC01 in versione rehosting Tuxedo', 'SC01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 3),
	(99, 'SZ01OTX', 'Project SZ01 Servizi Sicurezza SIGEA in versione Rehosting Tuxedo', 'SZ01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 2),
	(100, 'CE01OTX', 'Cross Project CE01 for SIGEA  in versione rehosting Tuxedo', 'CE01OTX', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 37, 12, 'False', NULL, 2),
	(101, 'ZZREL', 'Demo REL project', 'ZZREL', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 10, NULL, 'False', 0, 12, 12, 'False', NULL, 9),
	(102, 'ZZPRJ1', 'Demo project', 'ZZPRJ1', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 10, NULL, 'False', 1, 37, 12, 'False', NULL, 6),
	(103, 'ZZPRJ2', 'Demo project', 'ZZPRJ2', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 10, NULL, 'False', 1, 37, 12, 'False', NULL, 7),
	(105, 'SY01', 'Gestione di oggetti di tipo sistemistico (comandi, script, shell etc) ad uso in varie piattaforme + REDHAT fine 2018 da completare ', 'SY01', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'False', 1, 11, 12, 'False', NULL, 6),
	(106, 'SIGEDEVOPS', 'test devops su Sigea Rehosted', 'SIGEDEVOPS', 'build.xml', 'deploy.xml', NULL, 0, 0, NULL, 9, NULL, 'True', 0, 11, 12, 'False', NULL, 0);
/*!40000 ALTER TABLE "PROJECT" ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
