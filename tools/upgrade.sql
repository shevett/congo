-- Work in progress SQL script to take a congov1 table and import into the current database.
--
-- con_detail...

delete from con_detail;
insert into con_detail (con_cid,con_name,con_location,con_start,con_end,con_comment,con_stylesheet,con_website,con_email,con_badgelayout) (select * from congov1.con_detail) ;

delete from con_regtypes;
insert into con_regtypes (reg_cid,reg_name,reg_desc,reg_cost,reg_expire,reg_print) (select * from congov1.con_regtypes) ;

-- purge the invoice tables
delete from invoices;
delete from invoice_detail;

insert into pending_attendee (select * from congov1.pending_attendee) ;

-- Master data - all the registrants.  Also convert the password fields.
insert ignore into reg_master (select * from congov1.reg_master) ;
update reg_master set master_password=password(master_password);

-- Import the state records, and update state_subscribed, which is not in the old version.
insert into reg_state (state_rid,state_cid,state_registered,state_badged,state_checkedin,state_regtype,state_activity) (select * from congov1.reg_state) ;
update reg_state set state_subscribed=1 where state_registered=1 ;

-- Location data will need to be updated manually i think...
insert ignore into reg_email (email_rid,email_location,email_address) (select loc_rid,loc_name,loc_data from congov1.reg_locations where loc_type='EMAIL' and loc_rid IN (select master_rid from reg_master));

insert ignore into reg_phone (phone_rid,phone_location,phone_phone) (select loc_rid,loc_name,loc_data from congov1.reg_locations where loc_type='PHONE' and loc_rid IN (select master_rid from reg_master));

-- History table
insert ignore into reg_history (hist_rid,hist_tid,hist_cid,hist_activity,hist_operator,hist_actcode,hist_comment,hist_arg1,hist_arg2) (select hist_rid,hist_tid,hist_cid,hist_activity,hist_operator,hist_actcode,hist_comment,hist_arg1,hist_arg2 from congov1.reg_history where hist_rid IN (select master_rid from reg_master) and hist_cid IN (select con_cid from con_detail));

-- properties
-- to avoid a foreign key error, insert a 0 record first
insert into con_detail (con_cid,con_name) values (0,'Meta Name');
-- this will auto-increment con_detail.con_cid - reset that to 0:
update con_detail set con_cid=0 where con_cid=1006 ;

-- load the config_properties:
insert ignore into config_properties (prop_cid,prop_name,prop_default,prop_type,prop_regprompt,prop_cost,prop_description,prop_sequence)  (select prop_cid,prop_name,prop_default,prop_type,prop_regprompt,prop_cost,prop_description,prop_sequence from congov1.config_properties) ;

-- reg_properties:
insert ignore into reg_properties (prop_rid,prop_cid,prop_name,prop_value) (select prop_rid,prop_cid,prop_name,prop_value from congov1.reg_properties ) ;

-- Fix config_properties to have the 'scope' column set right...
update config_properties set prop_scope='Global' where prop_cid=0 ;
update config_properties set prop_scope='Event' where prop_cid>0 ;

-- Look for any config_properties set to Global that also have local names, and purge the local names.
--ARISIA SPECIFIC CLEANUP:
delete from reg_properties where prop_name='contact_method'
delete from config_properties where prop_name='contact_method'
delete from reg_properties where prop_name='Addr_Sharing' and prop_cid > 0
delete from config_properties where prop_name='Addr_Sharing' and prop_cid > 0
delete from config_properties where prop_name='db_program' and prop_cid > 0
delete from reg_properties where prop_name='db_program' and prop_cid > 0

-- Last but not least, we need to convert the mailing addresses.  See 'update.sh' script.

