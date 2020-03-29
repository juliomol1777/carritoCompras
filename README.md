
CARRITO DE COMPRAS WEB REALIZADO EN MAVEN JAVA EE 7
JSF 2.1, JPA, EJB
PRIMEFACES 5.0
BASE DE DATOS MySQL
SERVIDOR GLASSFISH 5

Configuracion de JAAS

En la base de datos crear 2 tablas, una con usuario, password. y otra tabla con usuario, grupo(administrador, invitado, etc) 
En el panel de administracion del servidor ir a configurations/server-config/Security/Realms 
crear un new, con nombre jdbcRealm, en nombre del pool de conecciones y los datos de las tablas creadas en la BD (usuario,password, grupo)
Luego en el proyecto configurar en la pestaña seguridad del archivo glassfich-web.xml, agregar un rol mapping
y asignar grupos a los roles(esos grupos son los de la tabla usuario, grupo). Luego en el archivo web.xml en security configurar.. tipo de ligin y poner el
Realm name creado en el servidor. agregar los roles y paginas a donde se va a aplicar la seguridad en esta ultima poner enable a las 2 opciones.
Es conveniente que las paginas con acceso restringido esten dentro de una carpeta, entonces en paginas de administracion pones directamente la ruta de esa carpeta 

Para acceder al panel de administracion, login con usuario= Admin , password= admin 