#!/usr/bin/perl

# This script is a crunchy one off hack that takes an old CONGO v1 instance containing 
# the old format name//name//name location data, converts it to individual fields, and
# inserts it into a CONGO v2 reg_address table.  
#
# WARNING - this isn't pretty.  It takes about a minute per 2000 records to do the insert,
# and doesn't care about existing data.  Clean out reg_address in the v2 install first.
#
# Caveats and notes:
# - Any addresses that are not 4 fields are discardsed.
# - Any duplicate entries are discarded
# - Any addresses with funky characters that may break the SQL are discarded.
#
# On an Arisia database update, with 16,200 'address' entries to convert, 15,886 addresses
# converted successfully.
#
# This script depends on 2 database instances on the local machine.  'congov1' and 'congov2'.
# ----------------------------------------------

$sql1 = "select loc_rid,loc_data from reg_locations where loc_type='POSTAL' order by loc_rid" ;

print "Opening SQL connection...";
open(SOURCE,"mysql -uroot --delimiter=++ -e \"$sql1\" congov1 |");
open(OUT,">out.sql");
while (<SOURCE>) {
        chop;
        ($rid,$locdata) = split("\t");
        @fields = split(/\/\//,$locdata);
        $n = scalar(@fields);
        if ($n < 4) {
                print "rid is $rid, data is $locdata, number of fields is " . scalar(@fields) . "\n";
        } else {
                $sql = "insert into reg_address (add_rid,add_location,add_line1,add_city,add_state,add_zipcode,add_primary) values ($rid,'Home','$fields[0]','$fields[1]','$fields[2]','$fields[3]',1)";
                system("echo \"$sql\" | mysql -uroot congov2");
                print $rid . " inserted.\n";
        }
        $c[$n]++;

}

for ($i=0; $i<=5; $i++) {
        print "Fields with " . $i . " elements: " . $c[$i] . "\n";
}

close(SOURCE);
close(OUT);
