@echo off
copy ..\src\main\resources\application.sample.yml ..\src\main\resources\application.yml
if exist ..\target\*.jar (
	del ..\target\*.jar
)
cd ..
call mvn package
if errorlevel 1 (
    echo ����ʧ��
) else (
    echo ������ɣ���һ�����Բο�start.bat.template�ļ�������Ŀ�����ű���������Ŀ��
)