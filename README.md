# Tracking App
Aplicación que  muestra las coordenadas de un usuario, las va almacenando en una base de datos,
permitiendo recuperar la información si el usuario cierra la aplicación. Ademas cuenta con un servicio en segundo plano.

Detalles importantes:

- Está desarrollada con arquitectura MVVM (Modelo-Vista-Vista-Modelo).
- Cuenta con un servicio en segundo plano para actualizar la información(coordenadas).
- Guarda la información en una base de datos de Firebase.

Vistas importantes:

- Splash Screen: Es la primera vista que ve el usuario. Muestra el logo de la aplicación y luego envía al usuario a la vista de introducción.
- Vista Introducción:  Una descripción de la aplicación.
- Vista Principal: Muestra las coordenadas en formato lista.


Librerías más importantes utilizadas:

- Firebase Database:  para guardar información de un usuario en la base de datos al momento de iniciar sesión (El código está oculto).
- EvenBus: Permite enviar eventos desde el servicio a la vista principal, por ejemplo las nuevas coordenadas.
