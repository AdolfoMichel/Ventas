set pgport=3389

set pgdata=\\192.168.110.1\servicio\Prueba DB

pg_isready -h %1
