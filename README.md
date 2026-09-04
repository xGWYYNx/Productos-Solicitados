# Solución - Spring Boot Persistence Challenge

Proyecto base: Operación Última Milla.
Tecnologías: Spring Boot, Spring Data JPA y H2.

## Ejecutar
Windows:
```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

## H2 Console
http://localhost:8080/h2-console

JDBC URL:
jdbc:h2:file:./data/inventario
Usuario: sa
Contraseña: vacía

## Endpoints principales
- GET /productos
- GET /productos/{id}
- POST /productos
- PUT /productos/{id}
- DELETE /productos/{id}
- GET /productos/buscar?nombre=...
- GET /productos/categoria?nombre=...
- GET /productos/stock?limite=...
- GET /productos/precio-maximo?precio=...
- GET /pedidos
- POST /pedidos
- PUT /pedidos/{id}/confirmar
- PUT /pedidos/{id}/cancelar
- PUT /pedidos/{id}/despachar
- GET /pedidos/pendientes
- GET /pedidos/urgentes
- GET /pedidos/estado?estado=CONFIRMADO
- GET /pedidos/prioridad?prioridad=URGENTE
- GET /pedidos/cliente?nombre=Ana
- GET /pedidos/resumen
- GET /pedidos/siguiente
- GET /pedidos/en-riesgo

La base se guarda en ./data/inventario.mv.db y sobrevive al reinicio.
