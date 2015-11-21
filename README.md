# JAVA CSV Transform
------------------

Transforms Similar CSV Files in Sub Directories into a single CSV Database

>Author: gggordon <https://github.com/gggordon>

>Version: 1.0.0

### Usage
```
java -jar build\javacsvtransform.jar [options]
```

### Options

| Short Option 	| Long Option 	| Description                                                               	| Default            	|
|--------------	|-------------	|---------------------------------------------------------------------------	|--------------------	|
| -h           	| -help       	| Display Help                                                              	|                    	|
| -d           	| -dir        	| Base Directory of CSV files                                               	| Current Directory  	|
| -db          	| -database   	| Database File Name                                                        	| DB-{timestamp}.csv 	|
| -depth       	| -depth      	| Recursive Depth. Set -1 to recurse into directories indefinitely.         	| -1                 	|
| -keepTemp    	| -keepTemp   	| Keep Temporary Buffer File. All files are concatenated before CSV parsing 	|                    	|


### Notes
This is an Eclipse Project Directory
