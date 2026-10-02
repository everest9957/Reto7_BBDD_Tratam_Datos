# ⚔️ Reto 7 — Configuración de Hibernate y CRUD de Usuarios

**Curso**: Desarrollo Web con Frameworks 4  
**Alumno**: Judit Giravent Pineda  
**Fecha**: Octubre 2026  
**Repositorio**: [GitHub - everest9957/Reto7_BBDD_Tratam_Datos](https://github.com/everest9957/Reto7_BBDD_Tratam_Datos)

---

## 🛠️ Tecnologías utilizadas

![Java](https://img.shields.io/badge/Java-21_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.11-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4.4-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-2.2.224-1E4B8F?style=for-the-badge&logo=databricks&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.2-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![draw.io](https://img.shields.io/badge/draw.io-Diagramas-F08705?style=for-the-badge&logo=diagramsdotnet&logoColor=white)

| Tecnología  | Versión   | Propósito                          |
|-------------|-----------|-------------------------------------|
| Java        | 21 LTS    | Lenguaje principal                  |
| Maven       | 3.9.11    | Gestión de dependencias             |
| Hibernate   | 6.4.4     | ORM (mapeo objeto-relacional)       |
| H2 Database | 2.2.224   | Base de datos en memoria            |
| JUnit       | 5.10.2    | Framework de pruebas unitarias      |
| draw.io     | —         | Diagrama del mapeo ORM              |

---

## 🎯 Objetivo del reto

Configurar **Hibernate** en un proyecto Java con **Maven** para mapear la
entidad `Usuario` a una tabla en base de datos, e implementar las
operaciones **CRUD** (Crear, Leer, Actualizar, Eliminar) usando
`SessionFactory`, `Session` y transacciones.

El proyecto forma parte del contexto narrativo "Código Samurái", donde los
descendientes del clan **Minamoto** están reconstruyendo su historia
creando una base de datos de guerreros.

---

## 📂 Estructura del proyecto

### Figura 1 — Estructura del proyecto

![Estructura del proyecto](docs/capturas/01-estructura-proyecto.png)

**Figura 1.** Estructura Maven estándar del proyecto. Se distinguen tres
zonas principales: `src/main/java` (código de producción dividido en
`entity`, `dao` y `util`), `src/main/resources` (archivo de configuración
de Hibernate) y `src/test/java` (pruebas unitarias JUnit 5).

```
Reto7_BBDD_Tratam_Datos/
├── pom.xml
├── .gitignore
├── README.md
├── docs/
│   ├── capturas/
│   ├── diagramas/
│   └── INFORME_PRUEBAS.md
└── src/
    ├── main/
    │   ├── java/com/codigosamurai/
    │   │   ├── entity/Usuario.java
    │   │   ├── util/HibernateUtil.java
    │   │   └── dao/UsuarioDAO.java
    │   └── resources/hibernate.cfg.xml
    └── test/java/com/codigosamurai/UsuarioCRUDTest.java
```

---

## ⚙️ Configuración paso a paso

### 1. Dependencias Maven

#### Figura 2 — Dependencias declaradas en `pom.xml`

![Dependencias del pom.xml](docs/capturas/02-pom-dependencias.png)

**Figura 2.** El `pom.xml` declara las dependencias principales:

- **`org.hibernate.orm:hibernate-core:6.4.4.Final`**: núcleo del ORM
  Hibernate. Proporciona `SessionFactory`, `Session`, `Transaction` y las
  anotaciones JPA (`@Entity`, `@Table`, `@Column`).
- **`com.h2database:h2:2.2.224`**: motor de base de datos embebido en
  memoria. Se usa como base de datos de pruebas y desarrollo, evitando
  instalar un SGBD externo.
- **`org.junit.jupiter:junit-jupiter:5.10.2`** (scope `test`): framework
  de pruebas unitarias. Solo se incluye en el classpath de test.

También se define la propiedad `maven.compiler.source` y
`maven.compiler.target` a **Java 21**, que es la versión LTS utilizada
como base del proyecto.

### 2. Configuración de Hibernate

#### Figura 3 — Archivo `hibernate.cfg.xml`

![hibernate.cfg.xml](docs/capturas/03-hibernate-cfg.png)

**Figura 3.** El archivo `hibernate.cfg.xml` en `src/main/resources`
define la sesión de Hibernate mediante el elemento `<session-factory>`:

- **Conexión a la base de datos**:
  - `hibernate.connection.driver_class` → `org.h2.Driver`
  - `hibernate.connection.url` → `jdbc:h2:mem:reto7db;DB_CLOSE_DELAY=-1`
  - `hibernate.connection.username` → `sa`
  - `hibernate.connection.password` → vacío
- **Dialecto SQL**: `hibernate.dialect` → `org.hibernate.dialect.H2Dialect`.
- **Autocommit**: `hibernate.connection.autocommit` → `false` (transacciones
  explícitas con `session.beginTransaction()`).
- **Logging**: `show_sql=true` y `format_sql=true`.
- **Generación de esquema**: `hbm2ddl.auto=update`.
- **Second-level cache**: `cache.use_second_level_cache=false`.
- **Mapeo**: `<mapping class="com.codigosamurai.entity.Usuario"/>`.

### 3. Entidad `Usuario`

#### Figura 4 — Entidad con anotaciones JPA/Hibernate

![Entidad Usuario](docs/capturas/04-entidad-usuario.png)

**Figura 4.** Clase `Usuario` anotada como entidad JPA. Cada elemento
contribuye al mapeo objeto-relacional:

- **`@Entity`**: marca la clase como entidad gestionada por Hibernate.
- **`@Table(name = "usuarios")`**: indica el nombre exacto de la tabla.
- **`@Id`**: señala el atributo `id` como clave primaria.
- **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: delega la
  generación del ID a la base de datos (columna autoincremental).
- **`@Column(name = "nombre", nullable = false, length = 100)`**: aplica
  `NOT NULL` y `VARCHAR(100)`.
- **`@Column(name = "email", nullable = false, unique = true, length = 150)`**:
  aplica `NOT NULL`, `UNIQUE` y `VARCHAR(150)`.

Se usa **`jakarta.persistence.*`** (en lugar de `javax.persistence.*`)
porque Hibernate 6.x adoptó la especificación Jakarta EE 9+.

### 4. Diagrama del mapeo ORM

#### Figura 5 — Diagrama entidad ↔ tabla

![Diagrama entidad-tabla](docs/capturas/05-diagrama-entidad-tabla.png)

**Figura 5.** Diagrama del mapeo objeto-relacional entre la clase Java
`Usuario` (izquierda) y la tabla `usuarios` en la base de datos H2
(derecha). Cada flecha representa la correspondencia entre un atributo y
su columna equivalente:

| Atributo Java   | Anotación JPA                                                       | Columna SQL                          |
|-----------------|---------------------------------------------------------------------|--------------------------------------|
| `Long id`       | `@Id @GeneratedValue(strategy = IDENTITY) @Column(name = "id")`     | `id BIGINT PK AUTO_INCREMENT`        |
| `String nombre` | `@Column(name = "nombre", nullable = false, length = 100)`          | `nombre VARCHAR(100) NOT NULL`       |
| `String email`  | `@Column(name = "email", nullable = false, unique = true, length = 150)` | `email VARCHAR(150) NOT NULL UNIQUE` |

Las **anotaciones JPA** actúan como puente entre el mundo de objetos
(Java) y el mundo relacional (SQL), evitando escribir manualmente las
sentencias DDL y DML: Hibernate las genera automáticamente a partir de
las anotaciones.

> El archivo editable del diagrama está disponible en
> [`docs/diagramas/diagrama-entidad-tabla.drawio`](docs/diagramas/diagrama-entidad-tabla.drawio).

### 5. Operaciones CRUD

El DAO `UsuarioDAO` implementa las cuatro operaciones:

```java
public void guardar(Usuario usuario);          // CREATE - session.persist()
public Usuario obtenerPorId(Long id);          // READ   - session.get()
public List<Usuario> obtenerTodos();           // READ   - createQuery
public void actualizar(Usuario usuario);       // UPDATE - session.merge()
public void eliminar(Long id);                 // DELETE - session.remove()
```

Cada operación abre una `Session` desde la `SessionFactory` de
`HibernateUtil`, ejecuta la operación dentro de una transacción y cierra
la sesión automáticamente mediante `try-with-resources`.

---

## 🧪 Ejecución de pruebas

```bash
mvn clean test
```

### Figura 6 — Resultado de los tests

![Tests ejecutados](docs/capturas/06-tests-ejecutados.png)

**Figura 6.** Resultado de ejecutar `mvn clean test`. Se observa:

- **`Tests run: 4`**: se ejecutaron los cuatro casos de prueba definidos
  en `UsuarioCRUDTest`: `testGuardarUsuario`, `testObtenerTodos`,
  `testActualizarUsuario` y `testEliminarUsuario`.
- **`Failures: 0`**: ninguna aserción falló.
- **`Errors: 0`**: no se lanzaron excepciones inesperadas.
- **`Skipped: 0`**: todos los tests fueron ejecutados.
- **`BUILD SUCCESS`**: el ciclo `clean → compile → test` se completó
  correctamente.

El orden de ejecución está garantizado por
`@TestMethodOrder(OrderAnnotation.class)` junto con `@Order(1..4)`, de
modo que los tests se ejecutan secuencialmente y cada uno depende del
estado dejado por el anterior:

1. **Test 1 (CREATE)** → inserta un `Usuario` ("Kenshin").
2. **Test 2 (READ)**   → consulta la lista completa.
3. **Test 3 (UPDATE)** → modifica el nombre a "Kenshin Himura".
4. **Test 4 (DELETE)** → elimina el usuario y comprueba que ya no existe.

Esta captura es la evidencia principal de que el mapeo ORM, la
configuración de Hibernate y las operaciones CRUD funcionan correctamente
contra la base de datos H2 en memoria.

### Figura 7 — SQL generado por Hibernate

![Consola SQL](docs/capturas/07-consola-hibernate-sql.png)

**Figura 7.** Salida de consola con `show_sql=true` y `format_sql=true`.
Hibernate genera automáticamente el SQL específico del dialecto
(`H2Dialect`) a partir de las anotaciones JPA:

**DDL** (`hibernate.hbm2ddl.auto=update`):

```sql
create table usuarios (
    id bigint generated by default as identity,
    email varchar(150) not null,
    nombre varchar(100) not null,
    primary key (id)
)

alter table if exists usuarios
   add constraint UK_kfsp0s1tflm1cwlj8idhqsad0 unique (email)
```

**DML** (uno por operación CRUD):

```sql
-- INSERT (crear)
insert into usuarios (email, nombre, id) values (?, ?, default)

-- SELECT (leer)
select u1_0.id, u1_0.email, u1_0.nombre from usuarios u1_0

-- UPDATE (actualizar)
update usuarios set email=?, nombre=? where id=?

-- DELETE (eliminar)
delete from usuarios where id=?
```

Esto demuestra el rol del ORM: el desarrollador trabaja con objetos Java
y Hibernate traduce a SQL parametrizado (con `?`), evitando la inyección
SQL y desacoplando la aplicación del SGBD concreto.

---

## 🚀 Cómo ejecutar el proyecto

```bash
# 1. Clonar el repositorio
git clone https://github.com/everest9957/Reto7_BBDD_Tratam_Datos.git
cd Reto7_BBDD_Tratam_Datos

# 2. Compilar y ejecutar tests
mvn clean test

# 3. (Opcional) Empaquetar
mvn package
```

**Requisitos**:

- JDK 17+ (probado con **JDK 21**)
- Maven 3.8+ (probado con **Maven 3.9.11**)

---

## 📄 Informe de pruebas

Ver [`docs/INFORME_PRUEBAS.md`](docs/INFORME_PRUEBAS.md) para el detalle
completo de los casos de prueba, resultados y conclusiones.

---

## 📚 Documentación adicional

- [Hibernate 6.4 User Guide](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html)
- [JSR 338 - JPA 2.2](https://jcp.org/en/jsr/detail?id=338)
- [H2 Database](https://www.h2database.com/)

---

## ✅ Cumplimiento de los requisitos del reto

Esta sección mapea **cada requisito del enunciado** del Reto 7 con la
solución implementada y su evidencia correspondiente.

### 1. Configuración general (JDK + IDE)

> *"Asegúrate de tener instalado el JDK y un IDE adecuado... Crea un nuevo
> proyecto Java y configura Hibernate. Puedes hacer esto manualmente o
> utilizando herramientas como Maven o Gradle."*

**Gestión**:

- **JDK**: instalado **Oracle JDK 21.0.8 LTS** en
  `C:\Program Files\Java\jdk-21`.
- **IDE**: **Visual Studio Code** para edición de código y **PowerShell**
  para la ejecución de Maven y Git.
- **Proyecto**: creado desde cero con estructura Maven estándar.
- **Gestor de dependencias**: **Maven 3.9.11** (se eligió Maven frente a
  Gradle por su integración natural con el IDE y su amplia documentación).

**Evidencia**: Figura 1 (estructura del proyecto).

### 2. Configuración de Hibernate (dependencias)

> *"Añade las dependencias necesarias de Hibernate en tu archivo de
> configuración de Maven o Gradle."*

**Gestión**: en `pom.xml` se declaran las dependencias:

- `org.hibernate.orm:hibernate-core:6.4.4.Final` — núcleo del ORM.
- `com.h2database:h2:2.2.224` — motor de base de datos embebido.
- `org.junit.jupiter:junit-jupiter:5.10.2` (scope `test`) — pruebas.

**Evidencia**: Figura 2 (`pom.xml`).

### 3. Archivo `hibernate.cfg.xml` en `src/main/resources`

> *"Crea un archivo de configuración de Hibernate, que se llamará
> `hibernate.cfg.xml`. Lo debes poner en el directorio
> `src/main/resources`."*

**Gestión**: el archivo se ubica en
`src/main/resources/hibernate.cfg.xml`, tal como especifica el reto.

**Evidencia**: Figura 3.

### 4. Propiedades de conexión, dialecto, autocommit y cache

> *"Define las propiedades de conexión a la base de datos, el dialecto
> SQL que se utilizará y otras configuraciones. ¿Cómo cuáles? Pues, por
> ejemplo, el modo de autocommit y el uso de second-level cache, si es
> necesario."*

**Gestión** en `hibernate.cfg.xml`:

| Propiedad | Valor | Justificación |
|-----------|-------|---------------|
| `hibernate.connection.driver_class` | `org.h2.Driver` | Driver JDBC de H2 |
| `hibernate.connection.url` | `jdbc:h2:mem:reto7db;DB_CLOSE_DELAY=-1` | BD en memoria persistente entre conexiones |
| `hibernate.connection.username` | `sa` | Usuario por defecto de H2 |
| `hibernate.connection.password` | *(vacío)* | H2 en memoria no requiere password |
| `hibernate.dialect` | `org.hibernate.dialect.H2Dialect` | Genera SQL específico para H2 |
| `hibernate.connection.autocommit` | `false` | Transacciones explícitas controladas por el código |
| `hibernate.cache.use_second_level_cache` | `false` | No se necesita para este proyecto |
| `hibernate.show_sql` | `true` | Visibilidad del SQL generado |
| `hibernate.format_sql` | `true` | SQL legible en consola |
| `hibernate.hbm2ddl.auto` | `update` | Crear/actualizar tablas según entidades |

**Evidencia**: Figura 3.

### 5. Entidad `Usuario` con anotaciones JPA

> *"Elige una entidad simple para mapear, como Usuario. Este podría tener
> atributos como id, nombre e email. Crea una clase Java para esta entidad
> y utiliza anotaciones de Hibernate como @Entity, @Table y @Column."*

**Gestión**: clase `com.codigosamurai.entity.Usuario` con los tres
atributos solicitados y las anotaciones requeridas:

- `@Entity` → marca la clase como entidad gestionada.
- `@Table(name = "usuarios")` → nombre de la tabla destino.
- `@Id` + `@GeneratedValue(strategy = IDENTITY)` → clave primaria autoincremental.
- `@Column` en cada atributo → mapeo y restricciones (`NOT NULL`, `UNIQUE`,
  `length`).

**Evidencia**: Figura 4 (código) y Figura 5 (diagrama del mapeo).

### 6. Operaciones CRUD con `SessionFactory`, `Session` y transacciones

> *"Implementa las operaciones CRUD utilizando la sesión de Hibernate.
> Puedes usar SessionFactory para crear sesiones y transacciones."*

**Gestión**:

- `HibernateUtil`: clase utilitaria que expone una `SessionFactory`
  singleton construida a partir de `hibernate.cfg.xml`.
- `UsuarioDAO`: implementa las cuatro operaciones:
  - `guardar(Usuario)` → `session.persist()` dentro de `beginTransaction()/commit()`.
  - `obtenerPorId(Long)` → `session.get()`.
  - `obtenerTodos()` → `session.createQuery("FROM Usuario", Usuario.class).list()`.
  - `actualizar(Usuario)` → `session.merge()`.
  - `eliminar(Long)` → `session.remove()`.
- Cada operación usa **try-with-resources** para cerrar la `Session`
  automáticamente y **rollback** en caso de excepción.

**Evidencia**: código fuente en `src/main/java/com/codigosamurai/`.

### 7. Testing con base de datos en memoria (H2)

> *"Escribe pruebas unitarias para verificar que el mapeo y las
> operaciones CRUD funcionan como se espera. Utiliza una base de datos en
> memoria, como H2 para las pruebas."*

**Gestión**:

- Clase `UsuarioCRUDTest` con **4 tests JUnit 5**, ordenados con
  `@TestMethodOrder(OrderAnnotation.class)` y `@Order(1..4)`.
- Se ejecutan contra **H2 en memoria** (`jdbc:h2:mem:reto7db`), sin
  necesidad de instalar un SGBD externo.
- Resultado: **4 tests pasados, 0 fallos, BUILD SUCCESS**.

**Evidencia**: Figura 6 (resultado) y Figura 7 (SQL generado).

### 8. Documentación de la entidad y la configuración

> *"Documenta tu clase de entidad y la configuración de Hibernate. Debes
> explicar cómo cada configuración y anotación contribuye al mapeo y a la
> gestión de la base de datos."*

**Gestión**:

- **Javadoc** en `Usuario.java` que explica:
  - La función de la clase y su tabla destino.
  - El efecto de cada anotación (`@Entity`, `@Table`, `@Id`,
    `@GeneratedValue`, `@Column`).
  - Las restricciones aplicadas y su traducción a SQL
    (`NOT NULL`, `UNIQUE`, `VARCHAR(n)`, `AUTO_INCREMENT`).
- **Secciones 3, 4 y 5 del README** documentan cada propiedad del
  `hibernate.cfg.xml` y cada anotación de la entidad.
- **Sección "Cumplimiento de los requisitos del reto"** (esta misma)
  documenta la relación requisito → solución.

**Evidencia**: Javadoc + secciones 3, 4 y 5 del README + Figura 5.

### 9. Aportación al portfolio

> *"Sube el código fuente completo del proyecto a un repositorio en
> GitHub, incluyendo archivos de configuración, clases de entidad y test.
> Además, incluye capturas de pantalla o diagramas que ilustren la
> estructura de la entidad y cómo se mapea a la tabla de la base de datos.
> Y también sube un informe de pruebas."*

**Gestión**:

| Elemento del portfolio | Ubicación |
|------------------------|-----------|
| Código fuente | `src/` subido a GitHub |
| Configuración Maven | `pom.xml` |
| Configuración Hibernate | `src/main/resources/hibernate.cfg.xml` |
| Entidad | `src/main/java/com/codigosamurai/entity/Usuario.java` |
| DAO | `src/main/java/com/codigosamurai/dao/UsuarioDAO.java` |
| Utilidad | `src/main/java/com/codigosamurai/util/HibernateUtil.java` |
| Tests | `src/test/java/com/codigosamurai/UsuarioCRUDTest.java` |
| Capturas | `docs/capturas/01..07.png` |
| Diagrama | `docs/capturas/05-diagrama-entidad-tabla.png` + `.drawio` editable |
| Informe de pruebas | `docs/INFORME_PRUEBAS.md` |
| README | `README.md` (este archivo) |

**Repositorio**: [https://github.com/everest9957/Reto7_BBDD_Tratam_Datos](https://github.com/everest9957/Reto7_BBDD_Tratam_Datos)

### 10. Apoyo de la IA (documentado)

> *"Este reto es muy ambicioso y es posible que la inteligencia artificial
> te pueda ser de utilidad. Úsala si lo consideras, pero ten en cuenta que
> el prompt que le des es clave para el logro del resultado que buscas."*

**Gestión**: se empleó asistencia de IA (Claude) para:

- Diseñar la estructura Maven del proyecto.
- Guiar la instalación de Maven y configuración de `JAVA_HOME`.
- Redactar los Javadoc y las explicaciones del README.
- Documentar el diagrama ORM y el informe de pruebas.

Todo el código ha sido **revisado, comprendido y ejecutado** localmente,
y cada configuración ha sido validada con los tests en verde. El uso de
la IA se documenta aquí en aras de la transparencia.

---

## 📋 Resumen del cumplimiento

| # | Requisito del reto | Estado |
|---|--------------------|--------|
| 1 | JDK + IDE + proyecto Java con Maven | ✅ |
| 2 | Dependencias Hibernate en `pom.xml` | ✅ |
| 3 | `hibernate.cfg.xml` en `src/main/resources` | ✅ |
| 4 | Propiedades: conexión, dialecto, autocommit, cache | ✅ |
| 5 | Entidad `Usuario` con `@Entity`, `@Table`, `@Column` | ✅ |
| 6 | CRUD con `SessionFactory`, `Session`, transacciones | ✅ |
| 7 | Tests JUnit 5 con H2 en memoria | ✅ |
| 8 | Documentación de entidad y configuración | ✅ |
| 9 | Portfolio: código + capturas + diagramas + informe | ✅ |
| 10 | Uso de IA documentado | ✅ |

**Todos los requisitos del Reto 7 han sido cumplidos.**
