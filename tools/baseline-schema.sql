-- MySQL dump 10.11
--
-- Host: boomer    Database: congov2
-- ------------------------------------------------------
-- Server version	5.0.32-Debian_7etch12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `MD5SUM` varchar(32) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `COMMENTS` varchar(255) default NULL,
  `TAG` varchar(255) default NULL,
  `LIQUIBASE` varchar(10) default NULL,
  PRIMARY KEY  (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime default NULL,
  `LOCKEDBY` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `con_detail`
--

DROP TABLE IF EXISTS `con_detail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `con_detail` (
  `con_cid` int(8) NOT NULL auto_increment,
  `con_name` varchar(40) default NULL,
  `con_location` varchar(50) default NULL,
  `con_start` datetime default NULL,
  `con_end` datetime default NULL,
  `con_comment` text,
  `con_website` varchar(80) default NULL,
  `con_email` varchar(60) default NULL,
  `con_description` text,
  `con_stylesheet` text,
  `con_badgelayout` text,
  KEY `cid` (`con_cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `con_regtypes`
--

DROP TABLE IF EXISTS `con_regtypes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `con_regtypes` (
  `reg_cid` int(8) NOT NULL default '0',
  `reg_name` varchar(15) default NULL,
  `reg_desc` varchar(40) default NULL,
  `reg_print` varchar(40) default NULL,
  `reg_cost` decimal(8,2) default NULL,
  `reg_expire` datetime default NULL,
  `reg_sequence` int(2) default NULL,
  `reg_active` tinyint(1) default NULL,
  `reg_discountcode` varchar(20) default NULL,
  `reg_banner` varchar(30) default NULL,
  UNIQUE KEY `idx_regtypes_cid_name` (`reg_cid`,`reg_name`),
  KEY `cid` (`reg_cid`),
  KEY `idx_regtypes_cid_discountcode` (`reg_cid`,`reg_discountcode`),
  CONSTRAINT `con_regtypes_ibfk_1` FOREIGN KEY (`reg_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE,
  CONSTRAINT `fk_regtype_discountcode` FOREIGN KEY (`reg_cid`, `reg_discountcode`) REFERENCES `config_discountcodes` (`dc_cid`, `dc_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_categories`
--

DROP TABLE IF EXISTS `config_categories`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_categories` (
  `cat_id` int(11) NOT NULL auto_increment,
  `cat_cid` int(8) default '0',
  `cat_name` varchar(15) default NULL,
  `cat_active` bit(1) default NULL,
  `cat_desc` varchar(50) default '',
  PRIMARY KEY  (`cat_id`),
  KEY `config_categories_ibfk_1` (`cat_cid`),
  KEY `cat_name` (`cat_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_departments`
--

DROP TABLE IF EXISTS `config_departments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_departments` (
  `dep_id` int(11) NOT NULL auto_increment,
  `dep_cid` int(8) default '0',
  `dep_name` varchar(15) default NULL,
  `dep_owner` int(11) default NULL,
  `dep_desc` varchar(50) default '',
  PRIMARY KEY  (`dep_id`),
  KEY `config_departments_ibfk_1` (`dep_cid`),
  KEY `dep_name` (`dep_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_discountcodes`
--

DROP TABLE IF EXISTS `config_discountcodes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_discountcodes` (
  `dc_cid` int(8) default NULL,
  `dc_name` varchar(20) default NULL,
  `dc_desc` varchar(255) default NULL,
  `dc_note` varchar(255) default NULL,
  `dc_type` enum('discount','percent','absolute') default NULL,
  `dc_factor` decimal(10,2) default NULL,
  `dc_lastmodified` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `dc_active` tinyint(1) default NULL,
  KEY `idx_discountcodes_cid_name` (`dc_cid`,`dc_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_events`
--

DROP TABLE IF EXISTS `config_events`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_events` (
  `event_id` int(11) NOT NULL auto_increment,
  `event_cid` int(8) NOT NULL default '0',
  `event_name` varchar(20) default NULL,
  `event_description` varchar(100) default NULL,
  `event_type` varchar(20) default NULL,
  `event_category` varchar(20) default NULL,
  `event_start` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `event_end` timestamp NOT NULL default '0000-00-00 00:00:00',
  `event_maxattendees` int(11) default NULL,
  `event_location` varchar(100) default NULL,
  `event_detail` text,
  `event_cost` decimal(8,2) default NULL,
  `event_owner` int(8) default NULL,
  `event_lastupdated` timestamp NOT NULL default '0000-00-00 00:00:00',
  `event_lasteditedby` int(11) default NULL,
  PRIMARY KEY  (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_properties`
--

DROP TABLE IF EXISTS `config_properties`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_properties` (
  `prop_cid` int(8) NOT NULL default '0',
  `prop_name` varchar(20) default NULL,
  `prop_default` varchar(100) default NULL,
  `prop_type` enum('boolean','string','float','date','numeric','picker') default NULL,
  `prop_regprompt` tinyint(1) default NULL,
  `prop_cost` decimal(8,2) default NULL,
  `prop_description` varchar(40) default NULL,
  `prop_sequence` int(2) default NULL,
  `prop_scope` enum('Event','Global') default NULL,
  `prop_format` varchar(255) default NULL,
  KEY `type` (`prop_cid`),
  KEY `name` (`prop_name`),
  CONSTRAINT `config_properties_ibfk_1` FOREIGN KEY (`prop_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE,
  CONSTRAINT `config_properties_ibfk_2` FOREIGN KEY (`prop_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_roomlayout`
--

DROP TABLE IF EXISTS `config_roomlayout`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_roomlayout` (
  `layout_id` int(11) NOT NULL auto_increment,
  `layout_name` varchar(15) default NULL,
  `layout_tables` smallint(6) default NULL,
  `layout_notes` varchar(50) default '',
  PRIMARY KEY  (`layout_id`),
  KEY `layout_name` (`layout_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_rooms`
--

DROP TABLE IF EXISTS `config_rooms`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_rooms` (
  `room_id` int(11) NOT NULL auto_increment,
  `room_venue` int(8) default NULL,
  `room_name` varchar(40) default NULL,
  `room_locationinvenue` varchar(40) default NULL,
  `room_width` smallint(6) default NULL,
  `room_length` smallint(6) default NULL,
  `room_height` smallint(6) default NULL,
  `room_capacity` smallint(6) default NULL,
  `room_phone` varchar(20) default NULL,
  PRIMARY KEY  (`room_id`),
  KEY `room_name` (`room_venue`),
  KEY `room_venue` (`room_venue`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_sessions`
--

DROP TABLE IF EXISTS `config_sessions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_sessions` (
  `session_id` int(11) NOT NULL auto_increment,
  `session_cid` int(8) default '0',
  `session_title` varchar(100) default NULL,
  `session_description` text,
  `session_detail` text,
  `session_notes` varchar(200) default NULL,
  `session_tags` varchar(200) default NULL,
  `session_department` int(11) default '0',
  `session_category` int(11) default '0',
  `session_room` int(11) default '0',
  `session_start` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `session_end` timestamp NOT NULL default '0000-00-00 00:00:00',
  `session_duration` int(11) default '0',
  `session_maxattendees` int(11) default NULL,
  `session_cost` decimal(8,2) default NULL,
  `session_owner` int(8) default NULL,
  `session_status` enum('PROPOSED','VETTED','SCHEDULED','DROPPED','CANCELLED','NEEDS EDIT','ASSIGNED','DUPLICATE') default 'PROPOSED',
  `session_public` bit(1) default '\0',
  `session_lastupdated` timestamp NOT NULL default '0000-00-00 00:00:00',
  `session_lasteditedby` int(11) default NULL,
  PRIMARY KEY  (`session_id`),
  KEY `config_sessions_ibfk_1` (`session_cid`),
  KEY `config_sessions_ibfk_3` (`session_lasteditedby`),
  KEY `config_sessions_ibfk_2` (`session_owner`),
  KEY `indexid` (`session_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_settings`
--

DROP TABLE IF EXISTS `config_settings`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_settings` (
  `setting_id` int(11) NOT NULL auto_increment,
  `setting_name` varchar(20) default NULL,
  `setting_type` enum('boolean','string','float','date','numeric','picker') default NULL,
  `setting_value` varchar(255) default NULL,
  PRIMARY KEY  (`setting_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config_venues`
--

DROP TABLE IF EXISTS `config_venues`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config_venues` (
  `venue_id` int(11) NOT NULL auto_increment,
  `venue_name` varchar(50) NOT NULL default '',
  `venue_website` varchar(50) default '',
  `venue_location` varchar(50) default '',
  `venue_notes` text,
  `venue_phone` varchar(20) default '',
  `venue_contact` varchar(50) default '',
  `venue_rooms` smallint(6) default NULL,
  `venue_footage` int(11) default '0',
  PRIMARY KEY  (`venue_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `invoice_detail`
--

DROP TABLE IF EXISTS `invoice_detail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `invoice_detail` (
  `detail_id` bigint(20) NOT NULL auto_increment,
  `detail_rid` int(11) default NULL,
  `detail_cid` int(11) default NULL,
  `detail_invoice` bigint(20) default NULL,
  `detail_sequence` int(11) default NULL,
  `detail_operator` int(11) default NULL,
  `detail_type` varchar(20) default NULL,
  `detail_type2` varchar(20) default NULL,
  `detail_description` varchar(100) default NULL,
  `detail_comment` text,
  `detail_amount` decimal(10,2) default NULL,
  `detail_discount` decimal(10,2) default NULL,
  `detail_final` decimal(10,2) default NULL,
  `detail_activity` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `detail_postprocess` tinyint(1) default NULL,
  PRIMARY KEY  (`detail_id`),
  KEY `idx_invoice_detail_detail_invoice` (`detail_invoice`),
  KEY `fk_detail_cid` (`detail_cid`),
  KEY `fk_detail_rid` (`detail_rid`),
  CONSTRAINT `fk_detail_cid` FOREIGN KEY (`detail_cid`) REFERENCES `con_detail` (`con_cid`),
  CONSTRAINT `fk_detail_invoice_invoice` FOREIGN KEY (`detail_invoice`) REFERENCES `invoices` (`invoice_id`),
  CONSTRAINT `fk_detail_rid` FOREIGN KEY (`detail_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `invoices` (
  `invoice_id` bigint(20) NOT NULL auto_increment,
  `invoice_activity` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `invoice_creator` int(11) default NULL,
  `invoice_amount` decimal(10,2) default NULL,
  `invoice_payer` int(11) default NULL,
  `invoice_paytype` varchar(10) default NULL,
  `invoice_paydate` datetime default NULL,
  `invoice_operator` int(11) default NULL,
  `invoice_comment` text,
  `invoice_authcode` varchar(20) default NULL,
  `invoice_status` enum('READY','VOID','PAID') default NULL,
  PRIMARY KEY  (`invoice_id`),
  KEY `idx_invoices_invoice_creator` (`invoice_creator`),
  KEY `idx_invoices_invoice_payer` (`invoice_payer`),
  KEY `idx_invoices_invoice_operator` (`invoice_operator`),
  CONSTRAINT `fk_invoices_creator` FOREIGN KEY (`invoice_creator`) REFERENCES `reg_master` (`master_rid`) ON DELETE SET NULL,
  CONSTRAINT `fk_invoices_operator` FOREIGN KEY (`invoice_operator`) REFERENCES `reg_master` (`master_rid`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_address`
--

DROP TABLE IF EXISTS `reg_address`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_address` (
  `add_rid` int(8) NOT NULL default '0',
  `add_location` enum('Home','Work','Other') default NULL,
  `add_line1` varchar(40) default NULL,
  `add_line2` varchar(40) default NULL,
  `add_city` varchar(40) default NULL,
  `add_state` varchar(20) default NULL,
  `add_zipcode` varchar(10) default NULL,
  `add_primary` tinyint(1) default NULL,
  `add_country` varchar(40) default NULL,
  UNIQUE KEY `reg_address_rid_location` (`add_rid`,`add_location`),
  KEY `rid` (`add_rid`),
  CONSTRAINT `reg_address_ibfk_1` FOREIGN KEY (`add_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_email`
--

DROP TABLE IF EXISTS `reg_email`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_email` (
  `email_rid` int(8) NOT NULL default '0',
  `email_location` enum('Home','Work','Mobile') default NULL,
  `email_address` varchar(40) default NULL,
  `email_primary` tinyint(1) default NULL,
  UNIQUE KEY `reg_email_rid_location` (`email_rid`,`email_location`),
  KEY `rid` (`email_rid`),
  CONSTRAINT `reg_email_ibfk_1` FOREIGN KEY (`email_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_events`
--

DROP TABLE IF EXISTS `reg_events`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_events` (
  `event_rid` int(8) NOT NULL,
  `event_eventid` int(11) NOT NULL,
  `event_cid` int(8) NOT NULL,
  KEY `reg_events_ibfk_3` (`event_cid`),
  KEY `reg_events_ibfk_1` (`event_rid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_history`
--

DROP TABLE IF EXISTS `reg_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_history` (
  `hist_id` int(20) NOT NULL auto_increment,
  `hist_rid` int(4) default NULL,
  `hist_cid` int(4) default NULL,
  `hist_activity` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `hist_operator` varchar(20) default NULL,
  `hist_actcode` varchar(10) default NULL,
  `hist_comment` varchar(250) default NULL,
  `hist_arg1` varchar(40) default NULL,
  `hist_arg2` varchar(40) default NULL,
  `hist_tid` int(10) default NULL,
  PRIMARY KEY  (`hist_id`),
  KEY `id` (`hist_rid`),
  KEY `cid` (`hist_cid`),
  KEY `idx_reg_history_activity` (`hist_activity`),
  CONSTRAINT `reg_history_ibfk_1` FOREIGN KEY (`hist_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE,
  CONSTRAINT `reg_history_ibfk_2` FOREIGN KEY (`hist_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_links`
--

DROP TABLE IF EXISTS `reg_links`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_links` (
  `link_id` int(11) NOT NULL auto_increment,
  `link_rid1` int(8) default NULL,
  `link_rid2` int(8) default NULL,
  `link_requestkey` varchar(255) default NULL,
  `link_requestdate` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `link_date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `link_status` enum('Ok','Pending','Active') default NULL,
  PRIMARY KEY  (`link_id`),
  KEY `idx_reg_friends_rid_friendrid` (`link_rid1`,`link_rid2`),
  KEY `fk_reglink_rid2` (`link_rid2`),
  CONSTRAINT `fk_reglink_rid1` FOREIGN KEY (`link_rid1`) REFERENCES `reg_master` (`master_rid`),
  CONSTRAINT `fk_reglink_rid2` FOREIGN KEY (`link_rid2`) REFERENCES `reg_master` (`master_rid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_master`
--

DROP TABLE IF EXISTS `reg_master`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_master` (
  `master_rid` int(8) NOT NULL auto_increment,
  `master_badgename` varchar(40) default NULL,
  `master_password` varchar(41) default NULL,
  `master_firstname` varchar(30) default NULL,
  `master_lastname` varchar(40) default NULL,
  `master_company` varchar(30) default NULL,
  `master_enabled` tinyint(1) default NULL,
  `master_mergedto` int(8) default NULL,
  `master_comment` varchar(255) default NULL,
  KEY `id` (`master_rid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_notes`
--

DROP TABLE IF EXISTS `reg_notes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_notes` (
  `note_id` int(8) NOT NULL auto_increment,
  `note_rid` int(8) default NULL,
  `note_cid` int(8) default NULL,
  `note_postrid` int(8) default NULL,
  `note_postdate` timestamp NULL default NULL,
  `note_ackrid` int(8) default NULL,
  `note_ackdate` timestamp NULL default NULL,
  `note_type` enum('NORMAL','NOTICE') NOT NULL,
  `note_message` varchar(100) default NULL,
  KEY `id` (`note_id`),
  KEY `rid` (`note_rid`),
  KEY `note_cid` (`note_cid`),
  KEY `note_postrid` (`note_postrid`),
  KEY `note_ackrid` (`note_ackrid`),
  CONSTRAINT `reg_notes_ibfk_1` FOREIGN KEY (`note_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE,
  CONSTRAINT `reg_notes_ibfk_2` FOREIGN KEY (`note_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE,
  CONSTRAINT `reg_notes_ibfk_3` FOREIGN KEY (`note_postrid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE,
  CONSTRAINT `reg_notes_ibfk_4` FOREIGN KEY (`note_ackrid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_phone`
--

DROP TABLE IF EXISTS `reg_phone`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_phone` (
  `phone_rid` int(8) NOT NULL default '0',
  `phone_location` enum('Home','Work','Mobile','Fax') default NULL,
  `phone_phone` varchar(40) default NULL,
  `phone_primary` tinyint(1) default NULL,
  UNIQUE KEY `reg_phone_rid_location` (`phone_rid`,`phone_location`),
  KEY `rid` (`phone_rid`),
  CONSTRAINT `reg_phone_ibfk_1` FOREIGN KEY (`phone_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_properties`
--

DROP TABLE IF EXISTS `reg_properties`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_properties` (
  `prop_rid` int(8) NOT NULL default '0',
  `prop_cid` int(8) default NULL,
  `prop_name` varchar(20) default NULL,
  `prop_value` varchar(100) default NULL,
  KEY `rid` (`prop_rid`),
  KEY `cid` (`prop_cid`),
  CONSTRAINT `0_62` FOREIGN KEY (`prop_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE,
  CONSTRAINT `0_64` FOREIGN KEY (`prop_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reg_state`
--

DROP TABLE IF EXISTS `reg_state`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reg_state` (
  `state_rid` int(8) default NULL,
  `state_cid` int(8) default NULL,
  `state_registered` tinyint(1) default NULL,
  `state_badged` tinyint(1) default NULL,
  `state_checkedin` tinyint(1) default NULL,
  `state_regtype` varchar(15) default NULL,
  `state_activity` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `state_subscribed` tinyint(1) default NULL,
  UNIQUE KEY `ridcid` (`state_rid`,`state_cid`),
  KEY `rid` (`state_rid`),
  KEY `dt` (`state_activity`),
  KEY `state_cid` (`state_cid`),
  CONSTRAINT `reg_state_ibfk_3` FOREIGN KEY (`state_rid`) REFERENCES `reg_master` (`master_rid`) ON DELETE CASCADE,
  CONSTRAINT `reg_state_ibfk_4` FOREIGN KEY (`state_cid`) REFERENCES `con_detail` (`con_cid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `templates`
--

DROP TABLE IF EXISTS `templates`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `templates` (
  `template_cid` int(8) NOT NULL auto_increment,
  `template_name` varchar(40) default NULL,
  `template_desc` varchar(60) default NULL,
  `template_body` text,
  `template_lastmod` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  KEY `cid` (`template_cid`),
  KEY `name` (`template_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-03-15  0:41:15
