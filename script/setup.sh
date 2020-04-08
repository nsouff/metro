#!/bin/bash


init_working_directory()
{
    if [[ ! -e $1 ]] ; then
	echo "Creating $1 directory"
	mkdir $1
    fi

    return $?
}

download_data_zip()
{
    if [[ -e $1 ]] ; then
	echo "$1 already exists"
	while [[ -z $ans ]] || [[ $ans != 'y' && $ans != 'n' ]] ; do
            read -p 'Download again ? [y/n] ' ans
	done
    fi
    
    if [[ ! -e $1 ]] || [[ $ans == y ]] ; then
	echo "Downloading data to $1"
	curl -o $1 http://dataratp.download.opendatasoft.com/RATP_GTFS_LINES.zip
    fi

    return $?
}

echo "Starting setup"


#We create working directory
WORKING_DIRECTORY="metro_lines"
init_working_directory $WORKING_DIRECTORY
if [[ $? -ne 0 ]] ; then
    exit 1
fi


#Downloading the data file
DATA_ZIP="$WORKING_DIRECTORY/RATP_GTFS_LINES.zip"
download_data_zip $DATA_ZIP
if [[ $? -ne 0 ]] ; then
    exit 2
fi


# Extract data about metro lines
TMP_WORKING_DIRECTORY="$WORKING_DIRECTORY/TMP"
unzip -o $DATA_ZIP "*METRO_[1-9]*" -d $TMP_WORKING_DIRECTORY
if [[ $? -ne 0 ]] ; then
    exit 3
fi


for LINE_ZIP in `ls $TMP_WORKING_DIRECTORY` ; do
    LINE=$( echo $LINE_ZIP | cut -d "_" -f 4 | head -c -5)
    unzip -o $TMP_WORKING_DIRECTORY/$LINE_ZIP "stop_times.txt" "stops.txt" -d $WORKING_DIRECTORY/$LINE
done

echo "Ending setup"
