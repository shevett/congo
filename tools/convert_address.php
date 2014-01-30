#!/usr/bin/php
<?

# This script converts an existing CONGO v1 database address table
# into temporary address data, readying for import into CONGO v2
#
# This should ONLY be run against an existing v1 DB, and for all our
# vast customer base, should only be needed once or twice. 
#
# I hope.
#-----------------------------

# Database definitions
$mhost = 'localhost';
$muser = 'arisia';
$mpass = 'arisia';
$mname = 'arisia';

# connect up...
$link = mysql_connect($mhost, $mname, $mpass);
if (! $link) {
	die("Error connecting to db: " . mysql_error() . "\n");
}

mysql_select_db($mname);

# Create the target address table that we'll use to build from the 
# old location data - this structure is taken from the v2 structs.
mysql_query("DROP TABLE IF EXISTS reg_address");

$ra_create = "CREATE TABLE reg_address (
	add_rid int(8) not null default '0',
	add_location ENUM('Home','Work','Other'),
	add_line1 varchar(40),
	add_line2 varchar(40),
	add_city varchar(40),
	add_state varchar(20),
	add_zipcode varchar(10),
	add_primary boolean,
	KEY rid (add_rid)
) TYPE=InnoDB;";

$result = mysql_query($ra_create);
if (! $result) {
	die("Error creating table : " . mysql_error() . "\n");
}

# Get the locations rows we want...
$sql = "select * from reg_locations where loc_type='POSTAL'" ;
$result = mysql_query($sql);
while ($row = mysql_fetch_assoc($result)) {
	$elements = preg_split("/\/\//", $row['loc_data']);
	switch (sizeof($elements)) {
		case 4 : 
			dealWithFour($row['loc_rid'],$elements);
			break;
		case 5 : 
			dealWithFive($row['loc_rid'],$elements);
			break;
		default :
			echo $row['loc_rid'] . "\t" . sizeof($elements) ."\t" . $row['loc_data'] . "   Unmatchable!\n";
	}
}

#---------
function dealWithFour($rid,$elements) {
	# echo print_r($elements);
	$sql = "insert into reg_address 
		(add_rid,add_location,add_line1,add_city,add_state,add_zipcode,add_primary) 
		values 
		($rid,'HOME',$elements[0],$elements[1],$elements[2],$elements[3],1)";
	mysql_query($sql);
	echo "Inserted 4 fields for $ID # $rid " . mysql_error() . "\n";
}

#---------
function dealWithFive($elements) {
	if (preg_match("/\d+/",$elements[4])) {
		# echo $row['loc_rid'] . "\t" . sizeof($elements) ."\t" . $row['loc_data'] . "   **** ZIPCODE\n";
		$sql = "insert into reg_address 
			(add_rid,add_location,add_line1,add_line2,add_city,add_state,add_zipcode,add_primary) 
			values 
			($rid,'HOME',$elements[0],$elements[1],$elements[2],$elements[3],$elements[4],1)";
		mysql_query($sql);
		echo "Inserted 5 fields for RID # $rid " . mysql_error() . "\n";
		return;
	}
#	if ($elements[4] == 'CANADA') {
		# echo $row['loc_rid'] . "\t" . sizeof($elements) ."\t" . $row['loc_data'] . "   **** CANADA\n";
#		return;
#	} 
#	if ($elements[4] == 'UK') {
		# echo $row['loc_rid'] . "\t" . sizeof($elements) ."\t" . $row['loc_data'] . " *** UK\n";
#	}
	echo $row['loc_rid'] . "\t" . sizeof($elements) ."\t" . $row['loc_data'] . " \tSKIPPED\n";
}

mysql_close($link);

?>
