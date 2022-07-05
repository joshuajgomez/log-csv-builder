@echo off

set outputDir=%TEMP%\Log_CSV_Output\
set inputFile=%outputDir%input
set jarFile=E:\projects\log-csv-builder\out\artifacts\log_csv_builder_jar\log-csv-builder.jar

echo outputDir %outputDir%
echo inputFile %inputFile%
echo jarFile %jarFile%

if not exist %outputDir% mkdir %outputDir%
echo.>%inputFile%

notepad %inputFile%

java -jar %jarFile% log-file=%inputFile% output-dir=%outputDir%

del %inputFile%

%SystemRoot%\explorer.exe %outputDir%