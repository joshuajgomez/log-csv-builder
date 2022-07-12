@echo off

set outputDir=%TEMP%\Log_CSV_Output\
set inputFile=%outputDir%input_list_of_logId_file_paths
set jarFile=E:\projects\log-csv-builder\out\artifacts\log_csv_builder_jar\log-csv-builder.jar
set fileNaming=E:\projects\log-csv-builder\script\file_naming.txt

if exist %outputDir% RMDIR /S /Q %outputDir%

mkdir %outputDir%
echo.>%inputFile%

notepad %inputFile%

java -jar %jarFile% log-file=%inputFile% output-dir=%outputDir% file-naming=%fileNaming%

del %inputFile%

%SystemRoot%\explorer.exe %outputDir%