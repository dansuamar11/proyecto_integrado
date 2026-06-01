# Basket Aljarafe

Aplicación de gestión para una liga local de baloncesto con backend en Spring Boot y frontend en Angular.

## Estado actual

El proyecto está funcionando con dos partes principales:

- Backend Java 17 + Spring Boot.
- Frontend Angular 21 en `frontend/basketAljarafe`.

Ahora mismo incluye:

- Página pública de inicio, calendario, clasificación, estadísticas y contacto.
- Paneles privados para `admin`, `gerente`, `árbitro` y `entrenador`.
- Gestión de usuarios, equipos, entrenadores y jugadores.
- Actas de partidos para árbitros.
- Cambio de contraseña desde los paneles privados.
- Redirección a `404` cuando se intenta entrar en rutas privadas sin sesión o sin rol válido.

## Reglas funcionales ya implementadas

- En fichas de partidos abiertos solo aparecen jugadores activos.
- En partidos ya jugados se conservan los jugadores que ya tienen estadísticas registradas aunque después se den de baja.
- Los árbitros no pueden añadir a un acta abierta jugadores inactivos que no formen parte válida de ese partido.

## Estructura del repositorio

- `src/main/java`: backend Spring Boot.
- `src/main/resources`: configuración y plantillas legacy Thymeleaf.
- `frontend/basketAljarafe`: SPA Angular actual.
- `docker/`: soporte para base de datos y entorno local.
- `scripts/`: arranque y utilidades de desarrollo.

## Cómo arrancarlo

### Opción rápida

Desde la raíz:

```powershell
npm run dev:full
```

Para detenerlo:

```powershell
npm run dev:stop
```

### Arranque manual

Backend:

```powershell
.\mvnw.cmd spring-boot:run
```

Frontend:

```powershell
cd .\frontend\basketAljarafe
npm install
npm start
```

## Verificación

Frontend:

```powershell
cd .\frontend\basketAljarafe
npm run build
```

Backend:

```powershell
.\mvnw.cmd test
```

## Observaciones

