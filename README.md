## Requisitos Previos e Instalación de Llaves

Para que el proyecto funcione (tanto en local como en Docker), es **obligatorio** contar con el archivo de credenciales de Firebase:

1.  Este archivo lo voy a compartir mediante el correo de notificacion de finalizacion de la prueba.
2.  **IMPORTANTE**: Coloca el archivo directamente en la **raíz del proyecto** (en la misma carpeta donde ves el archivo `pom.xml`). 

---

##  1. Cómo correr la App en tu PC (Modo Manual)

Si prefieres usar tu IDE (VS Code, IntelliJ, Eclipse):

1.  Asegúrate de tener el archivo `service-account.json` en la raíz.
2.  Abre una terminal en la carpeta del proyecto.
3.  Ejecuta:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  La API iniciará en `http://localhost:8080`.

---

##  2. Cómo correr la App con Docker (Modo Automático)

Si tienes Docker instalado, no necesitas configurar nada más:

1.  Verifica que el archivo `service-account.json` esté en la raíz del proyecto.
2.  Desde la terminal, ejecuta:
    ```bash
    docker-compose up --build
    ```

## Despliegue en Vivo (Live Demo)

El proyecto se encuentra actualmente desplegado y operativo en la nube de **Render**. Puedes acceder a la API a través de los enlaces de la documentacion de Backend que estan disfribuidos por APIs.


> **Nota para el revisor**: La aplicación utiliza el plan gratuito de Render. Si es la primera vez que accedes después de un periodo de inactividad, es posible que la primera petición tarde entre 30 y 60 segundos.

---

###  Detalles del Web Service (Render)

Para lograr el despliegue, se configuraron los siguientes puntos en el panel de Render:

1.  **Secret Files (Seguridad)**: Las credenciales de Firebase no están en el código. Se configuró un **Secret File** en la ruta `/etc/secrets/service-account.json`. Esto garantiza que la llave privada de la base de datos esté protegida y solo sea accesible por el contenedor en ejecución.
2.  **Variables de Entorno**: Se inyectaron variables clave como `FIREBASE_CONFIG_PATH` y `PORT` para que la aplicación se adapte automáticamente al entorno de producción.
