@echo off
copy ..\sfc-web\\src\main\resources\application.sample.yml ..\sfc-web\\src\main\resources\application.yml
if exist ..\target\*.jar (
	del ..\target\*.jar
)
cd ..\sfc-compress
call mvn clean
call mvn install
cd ..\sfc-web
call mvn clean
call mvn install
cd ..
call mvn package
if errorlevel 1 (
    echo ����ʧ��
) else (
    echo ������ɣ���һ�����Բο�start.bat.template�ļ�������Ŀ�����ű���������Ŀ��
)