# Hoy Como Backend
https://hoy-como-backend.herokuapp.com/swagger-ui.html

## ¿Cómo correrlo localmente?

Bajarse PostgreSQL versión 10.
Luego ejecutar
```
psql -U postgres -h localhost
```

Ingresar contraseña para el usuario postgres. Luego en el prompt de PostgreSQL ejecutar lo siguiente:
```
postgres=# create database testdb;
```

Luego modificar el archivo hoy-como-backend/src/main/resources/application-dev.yml

Cambiar el password por el seteado en la instalación (default "1").
