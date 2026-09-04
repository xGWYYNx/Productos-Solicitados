<!-- # Evidencias de Persistencia

## 1. ¿Qué información desapareció?

Antes de utilizar persistencia, la información de los productos se encontraba almacenada únicamente en una lista en memoria.

Al reiniciar la aplicación, los productos que había registrado desaparecieron, debido a que la lista se creó nuevamente desde cero al iniciar el programa.

Por ejemplo, si tenía registrados productos como:

* Arroz
* Papa
* Tomate
* Leche
* Maíz

después de reiniciar la aplicación estos datos ya no estaban disponibles.

## 2. ¿Dónde estaba almacenada?

La información estaba almacenada en una **lista en memoria de la aplicación**, generalmente una estructura como `List<Producto>`.
Esta lista pertenece a la ejecución actual del programa y se encuentra en la memoria RAM mientras la aplicación está funcionando.
Por esta razón, los datos no estaban guardados permanentemente en una base de datos.

## 3. ¿Por qué reiniciar la aplicación afecta a una lista en memoria?

Reiniciar la aplicación provoca que se cierre la ejecución anterior del programa y se cree una nueva.

Cuando esto ocurre, la memoria utilizada por la aplicación anterior se libera y la lista que contenía los productos deja de existir.

Al iniciar nuevamente Spring Boot, se crea una nueva lista vacía o con los datos iniciales definidos en el código.

Por eso, los productos agregados durante la ejecución anterior desaparecen.

---

## 4. ¿Qué debería cambiar para conservarla?

Para conservar la información se debe utilizar **persistencia**, es decir, almacenar los productos en una base de datos en lugar de mantenerlos únicamente en una lista en memoria.

En este proyecto se utiliza **Spring Data JPA** junto con **H2** para guardar y consultar los productos.

Para esto, se utiliza una entidad `Producto` y un repositorio basado en `JpaRepository`.

De esta manera, los datos pueden ser guardados en la base de datos mediante operaciones como `save()` y consultados posteriormente mediante métodos como `findById()`.

---

# Preguntas sobre ProductoRepository y JpaRepository

## 1. ¿ProductoRepository es una clase o una interfaz?

`ProductoRepository` es una **interfaz**.

Por ejemplo:

```java
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
```

Es una interfaz porque define el repositorio que utilizará Spring Data JPA para realizar operaciones sobre los productos.

No es necesario implementar manualmente los métodos básicos como guardar, buscar, actualizar o eliminar, porque Spring Data JPA proporciona estas funcionalidades automáticamente.

---

## 2. ¿Qué representa Producto en `JpaRepository<Producto, Long>`?

`Producto` representa la **entidad o clase que se va a administrar mediante el repositorio**.

En este caso, `Producto` es la clase que representa los productos almacenados en la base de datos.

Por ejemplo:

```java
JpaRepository<Producto, Long>
```

significa que `ProductoRepository` trabajará con objetos de tipo `Producto`.

---

## 3. ¿Qué representa Long?

`Long` representa el **tipo de dato del identificador (ID) de la entidad `Producto`**.

Por ejemplo, si la clase `Producto` tiene:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

el identificador de cada producto es de tipo `Long`.

Por eso se escribe:

```java
JpaRepository<Producto, Long>
```

El primer parámetro indica la entidad y el segundo indica el tipo de su identificador.

---

## 4. ¿Por qué ya no es necesario recorrer una lista para buscar por id?

Cuando los productos estaban almacenados en una lista, era necesario recorrerla para encontrar un producto específico.

Por ejemplo:

```java
for (Producto producto : productos) {
    if (producto.getId().equals(id)) {
        return producto;
    }
}
```

Con `JpaRepository` ya no es necesario hacer este recorrido manualmente.

Podemos utilizar directamente:

```java
productoRepository.findById(id);
```

Spring Data JPA se encarga de realizar la consulta correspondiente en la base de datos utilizando el identificador.

Esto hace que el código sea más sencillo y permite delegar las operaciones de persistencia a Spring Data JPA.

---

# Conclusión

La principal diferencia es que anteriormente los productos estaban almacenados solamente en memoria, por lo que se perdían al reiniciar la aplicación.
Al utilizar **JPA, `ProductoRepository` y H2**, los productos pasan a ser almacenados mediante persistencia.

`ProductoRepository` es una interfaz que extiende `JpaRepository<Producto, Long>`, donde `Producto` representa la entidad que se administra y `Long` representa el tipo de dato de su identificador.

Además, Spring Data JPA permite realizar búsquedas directamente con métodos como `findById()`, evitando tener que recorrer manualmente una lista. -->
