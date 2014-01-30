$Id$
------------------------------------------------------
This set of tools is used to import external comma delimited
data into an existing CONGO instance. 


clean.sh
--------
This script will 'clean out' a CONGO event of existing registrant data
and prepare it for import.

clean.sh -e eventid -u user -p password [--hostname=host]

Where eventid is what eventid to be used.
