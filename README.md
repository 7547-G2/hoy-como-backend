# Hoy Como Backend
https://hoy-como-backend.herokuapp.com/swagger-ui.html

## Para filtrar comercios
Case insensitive, separado por comas filtros que acepta:
* tipoId
* tipo
* estado
* nombre
* razonSocial

Ejemplo:
```
/api/comercios?search=tipoId:1,tipo:tipo,estado:estado,nombre:nombre,razonSocial:rAzOnSocIAL
```

## ¿Cómo correrlo localmente?

Bajarse PostgreSQL versión 10.
Luego ejecutar
```
psql -U postgres -h localhost
```

Ingresar contraseña para el mobileUser postgres. Luego en el prompt de PostgreSQL ejecutar lo siguiente:
```
postgres=# create database testdb;
```

Luego modificar el archivo hoy-como-backend/src/main/resources/application-dev.yml

Cambiar el password por el seteado en la instalación (default "1").

