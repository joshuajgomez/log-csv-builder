@echo off

set outputDir=%TEMP%\Log_CSV_Output\
set inputFile=%outputDir%input
set jarFile=E:\projects\log-csv-builder\out\artifacts\log_csv_builder_jar\log-csv-builder.jar

if exist %outputDir% RMDIR /S /Q %outputDir%

mkdir %outputDir%
echo.>%inputFile%

notepad %inputFile%

java -jar %jarFile% log-file=%inputFile% output-dir=%outputDir%

del %inputFile%

%SystemRoot%\explorer.exe %outputDir%