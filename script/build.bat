@echo off
if exist ..\sfc-core\target\*.jar (
	del ..\sfc-core\target\*.jar
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