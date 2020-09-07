# Blog

En este proyecto se desarrolla un sistema de gestión de contenidos basado en una API REST (headless CMS) que permite a los usuarios gestionar todo el proceso de creación y publicación de contenidos en un blog, como son la redacción de posts, la subida de elementos multimedia para acompañar las publicaciones, la escritura de comentarios u opiniones, la moderación de los contenidos...

La aplicación permite interactuar con los diferentes servicios desarrollados y se encuentra totalmente desacoplada del backend. 

El blog está formado por los siguientes componentes:

* Usuarios: Son los creadores y consumidores de los contenidos y pueden tener los siguientes roles:

o Usuarios no registrados

o Lectores

o Moderadores

o Redactores

o Administradores

Además de los roles, los usuarios deben tener un nombre, un Nick, una fecha de registro, una contraseña y una cuenta de correo.

* Publicaciones: Son los contenidos que crean y consumen los usuarios. Son creados por los usuarios redactores, y pueden ser leídos por cualquier usuario.

* Comentarios: Los usuarios registrados pueden hacer comentarios sobre una publicación de forma que se creen discusiones entre los usuarios que enriquezcan el contenido del sitio.

Todos los servicios sobre colecciones deben permitir realizar búsquedas o filtrados, además de permitir la obtención de los resultados paginados y ordenados por el campo que decida el usuario.

* Servicios disponibles para todos los usuarios (sin autenticación):

o Obtener un listado con todas las publicaciones

o Obtener una publicación concreta

o Obtener un listado con todos los redactores

o Obtener un redactor concreto

o Registrarse como usuario lector del blog

Servicios con restricción de rol (con autenticación): Cada rol debe tener acceso a los servicios de su rol y a los de todos los roles anteriores.

* Lectores:

▪ Eliminar su propia cuenta de usuario

▪ Añadir un comentario a una publicación

▪ Eliminar sus comentarios

▪ Obtener todos sus comentarios

▪ Crear una suscripción a un tema o autor concreto

▪ Obtener todas sus suscripciones

▪ Eliminar una de sus suscripciones

▪ Modificar una de sus suscripciones

* Moderadores:

▪ Eliminar comentarios de otros usuarios

▪ Suspender la cuenta de otros usuarios (suspender una cuenta implica que se bloquee el acceso a la plataforma, no que se elimine al usuario)

* Redactores:

▪ Crear nuevas publicaciones

▪ Obtener sus publicaciones

▪ Editar sus publicaciones

▪ Eliminar sus publicaciones

* Administradores:

▪ Cambiar el rol de otros usuarios

▪ Eliminar la cuenta de otros usuarios

▪ Eliminar publicaciones de otros usuarios
