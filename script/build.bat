@echo off
if exist ..\target\*.jar (
	del ..\target\*.jar
)
cd ..
echo ================�����ϴδ������================
call mvn clean
echo ================   ��ʼ���    ================
call mvn package
if errorlevel 1 (
    echo ����ʧ��
) else (
    echo ������ɣ���һ�����Բο�start.bat.template�ļ�������Ŀ�����ű���������Ŀ��
)