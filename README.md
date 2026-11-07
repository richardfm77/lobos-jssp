# lobos-jssp

Implementación en **Java 21** del **Wolf Search Algorithm con Memoria Efímera (WSA)** adaptado al **Job Shop Scheduling Problem (JSSP)**.

El proyecto utiliza una heurística poblacional con busqeuda local para buscar secuencias de operaciones que reduzcan el **makespan** de una instancia JSSP, considerando tanto soluciones factibles como infactibles durante el proceso de búsqueda.

## Características

- Implementación de WSA sobre un espacio de búsqueda discreto.
- Representación de soluciones mediante secuencias de operaciones por máquina.
- Evaluación de factibilidad mediante una gráfica dirigida.
- Detección de ciclos utilizando ordenamiento topológico.
- Cálculo del makespan para soluciones factibles.
- Penalización de soluciones con ciclos.
- Movimientos dirigidos hacia mejores lobos visibles.
- Exploración aleatoria mediante intercambios de operaciones.
- Memoria efímera para evitar regresar inmediatamente a posiciones recientes.
- Mecanismo de amenazas y escape.
- Conservación del mejor resultado global encontrado.
- Ejecuciones reproducibles mediante semillas pseudoaleatorias.
- Configuración externa mediante archivos `.properties`.
- Registro de evaluaciones para análisis posterior.
- Pruebas unitarias e integración con JUnit 5.

---

## Requisitos

- Java 21
- Maven
- Git

Puedes comprobar las versiones instaladas con:

```bash
java -version
```

```bash
mvn -version
```

---

## Instalación

Clona el repositorio:

```bash
git clone https://github.com/richardfm77/lobos-jssp.git
cd lobos-jssp
```

Compila el proyecto:

```bash
mvn clean install
```

El JAR ejecutable se genera en:

```text
target/lobos-jssp-1.0-SNAPSHOT.jar
```

---

## Ejecución

La aplicación recibe como argumento la ruta de un archivo de configuración `.properties`.

```bash
java -jar target/lobos-jssp-1.0-SNAPSHOT.jar parametros/config.properties
```

También puede consultarse la ayuda:

```bash
java -jar target/lobos-jssp-1.0-SNAPSHOT.jar --help
```

---

## Configuración

El repositorio incluye un archivo de configuración de ejemplo:

```text
parametros/config.properties
```

Actualmente tiene una estructura como la siguiente:

```properties
# Instancia JSSP
instancia.archivo=src/main/resources/instancias/JSSP_40_40.txt

# Salida
salida.ruta=resultados/
salida.nombreReporteEvaluacionesAceptadas=ReporteEvaluacionesAceptadas.txt
salida.nombreSolcucion=Solucion.txt

# Registro
registro.mostrarConsola=true
registro.frecuenciaIteraciones=1000

# Tipo de solución
solucion.tipo=FACTIBLE

# Parámetros WSA
wsa.tamanoPoblacion=24
wsa.numeroIteraciones=2500
wsa.numeroIteracionesGeneracionPoblacion=1000
wsa.rangoVisual=8
wsa.probabilidadAmenaza=0.003
wsa.semilla=628407772612
wsa.maxIntentosEscape=100
wsa.tamanoMemoria=10
wsa.maxIntentosMovimiento=20
```

> `salida.nombreSolcucion` conserva actualmente esa escritura porque es el nombre de la propiedad utilizada por la aplicación.

### Parámetros principales

| Parámetro | Descripción |
|---|---|
| `instancia.archivo` | Ruta de la instancia JSSP |
| `salida.ruta` | Directorio donde se generan los resultados |
| `registro.mostrarConsola` | Habilita o deshabilita el registro detallado en consola |
| `registro.frecuenciaIteraciones` | Frecuencia con la que se muestra el progreso |
| `solucion.tipo` | Tipo de solución inicial |
| `wsa.tamanoPoblacion` | Número de lobos |
| `wsa.numeroIteraciones` | Número de iteraciones principales |
| `wsa.numeroIteracionesGeneracionPoblacion` | Límite usado durante la generación inicial de población |
| `wsa.rangoVisual` | Distancia máxima para considerar visible a otro lobo |
| `wsa.probabilidadAmenaza` | Probabilidad de que un lobo sea afectado por una amenaza |
| `wsa.semilla` | Semilla del generador pseudoaleatorio |
| `wsa.maxIntentosEscape` | Máximo número de intentos durante un escape |
| `wsa.tamanoMemoria` | Capacidad de la memoria efímera de cada lobo |
| `wsa.maxIntentosMovimiento` | Número de candidatos considerados durante un movimiento |

---

## Tipos de solución inicial

La propiedad:

```properties
solucion.tipo=FACTIBLE
```

admite actualmente tres valores.

### `FACTIBLE`

Construye una solución inicial mediante un recorrido por capas de las operaciones de los trabajos.

Está pensada para iniciar la búsqueda desde una solución factible.

### `NORMAL`

Agrupa las operaciones por máquina según el orden en que aparecen al recorrer los trabajos.

La solución resultante no necesariamente es factible.

### `NO_FACTIBLE`

Construye deliberadamente una solución cuya gráfica contiene ciclos.

Este modo resulta útil para pruebas y para observar el comportamiento del algoritmo frente a soluciones infactibles.

---

## Formato de las instancias

El formato esperado es:

```text
n m
maquina tiempo maquina tiempo ...
maquina tiempo maquina tiempo ...
...
```

donde:

- `n` es el número de trabajos;
- `m` es el número de máquinas;
- cada fila posterior representa un trabajo;
- cada trabajo contiene exactamente `m` pares `máquina tiempo`.

Ejemplo:

```text
3 3
0 3 1 2 2 4
1 2 2 1 0 5
2 4 0 2 1 3
```

El repositorio incluye actualmente:

```text
src/main/resources/instancias/
├── JSSP_40_40.txt
├── JSSP_50_20.txt
└── JSSP_100_100.txt
```

---

## Representación de las soluciones

Una solución contiene una secuencia de operaciones para cada máquina.

Cada operación almacena:

```text
idTrabajo
indiceOperacion
idMaquina
tiempoProceso
```

El orden de las operaciones dentro de cada máquina determina las precedencias de máquina de la solución.

Para evaluar una solución se construye una gráfica dirigida que combina:

- precedencias entre operaciones de un mismo trabajo;
- precedencias definidas por el orden de las operaciones en cada máquina.

Las soluciones sin ciclos se consideran factibles.

---

## Vecindad

El movimiento aleatorio utilizado actualmente genera una solución vecina seleccionando una máquina y realizando un intercambio entre dos operaciones adyacentes.

Ejemplo:

```text
A B C D E
```

puede transformarse en:

```text
A B D C E
```

Los movimientos dirigidos utilizan la distancia entre soluciones para generar cambios orientados hacia la posición de un lobo visible con mejor costo.

Durante cada movimiento se pueden generar varios candidatos y conservar el de menor costo.

---

## Memoria efímera

Cada lobo mantiene una colección limitada de posiciones visitadas recientemente.

Esta memoria evita que el algoritmo regrese inmediatamente a soluciones recientes y ayuda a reducir oscilaciones durante la búsqueda.

Su capacidad se controla mediante:

```properties
wsa.tamanoMemoria=10
```

---

## Amenazas

Cada lobo puede experimentar una amenaza de acuerdo con:

```properties
wsa.probabilidadAmenaza
```

Cuando ocurre una amenaza, el algoritmo intenta mover al lobo fuera de su rango visual actual.

El número máximo de intentos se controla mediante:

```properties
wsa.maxIntentosEscape
```

---

## Reproducibilidad

La aplicación utiliza una semilla configurable:

```properties
wsa.semilla=628407772612
```

Durante una ejecución se utiliza un generador pseudoaleatorio compartido por los componentes que requieren aleatoriedad.

Esto permite repetir experimentos utilizando la misma configuración y semilla.

---

## Archivos de salida

La ubicación se define mediante:

```properties
salida.ruta=resultados/
```

### `Solucion.txt`

Contiene la mejor solución encontrada.

Cada línea representa la secuencia asociada a una máquina y cada operación utiliza el formato:

```text
[trabajo, operacion, tiempo]
```

Ejemplo:

```text
[ [15, 2, 1], [33, 2, 2], [20, 3, 2], ... ]
```

### `ReporteEvaluacionesAceptadas.txt`

Contiene los costos registrados durante la ejecución:

```text
E:0.0324015248
E:0.0317662008
E:0.0308132147
...
```

Este archivo puede utilizarse posteriormente para generar gráficas o analizar el comportamiento de una ejecución.

---

## Salida en consola

Cuando:

```properties
registro.mostrarConsola=true
```

la aplicación informa sobre:

- configuración utilizada;
- características de la instancia;
- parámetros WSA;
- costo y makespan inicial;
- movimientos y amenazas;
- nuevos mejores globales;
- progreso periódico;
- mejor solución encontrada;
- makespan final;
- tiempo total de ejecución.

Ejemplo de progreso:

```text
[NUEVO MEJOR][Iteración 24] 0.0282719187

[Iteración 1000/2500] Mejor costo: 0.0282719187
```

---

## Estructura del proyecto

```text
.
├── LICENSE
├── README.md
├── pom.xml
├── parametros/
│   └── config.properties
│
├── src/
│   ├── main/
│   │   ├── java/mx/unam/heuristicas/jssp/
│   │   │   ├── App.java
│   │   │   ├── configuracion/
│   │   │   ├── estructura/
│   │   │   ├── excepcion/
│   │   │   ├── problema/
│   │   │   │   └── modelo/
│   │   │   ├── util/
│   │   │   └── wsa/
│   │   │
│   │   └── resources/
│   │       └── instancias/
│   │
│   └── test/
│       ├── java/
│       └── resources/
```

### `configuracion`

Lectura y validación del archivo `.properties`.

### `estructura`

Contiene las estructuras auxiliares utilizadas por el proyecto, principalmente la implementación de la gráfica dirigida.

### `problema`

Implementación específica del Job Shop Scheduling Problem:

- lectura de instancias;
- cálculo de cotas;
- generación de soluciones;
- construcción de gráficas;
- evaluación de soluciones;
- generación de vecinos;
- distancia entre soluciones;
- escritura de resultados.

### `problema.modelo`

Clases y registros que representan:

- instancias;
- trabajos;
- operaciones;
- secuencias de máquina;
- soluciones;
- resultados de evaluación.

### `wsa`

Implementación de la heurística:

```text
AlgoritmoWsa
AmenazaWsa
GeneradorPoblacionWsa
IteracionWsa
Lobo
MemoriaEfimeraWsa
MovimientoWsa
ParametrosWsa
PoblacionWsa
ProblemaHeuristico
RegistroEjecucionWsa
ResultadoWsa
VisionWsa
```

`ProblemaHeuristico<S>` permite separar la lógica general del WSA de la implementación concreta del JSSP.

---

## Pruebas

El proyecto utiliza **JUnit 5**.

Para ejecutar todas las pruebas:

```bash
mvn test
```

Actualmente existen pruebas para componentes como:

- lectura de instancias;
- cálculo de cotas;
- gráfica dirigida;
- generación de soluciones;
- construcción y evaluación de gráficas;
- generación de vecinos;
- distancia entre soluciones;
- memoria efímera;
- población;
- visión;
- movimiento;
- amenazas;
- iteraciones WSA;
- ejecución completa del algoritmo.

---

## Estado

El proyecto continúa en desarrollo.

La implementación actual permite ejecutar experimentos reproducibles sobre distintas instancias JSSP y modificar los parámetros principales del WSA sin recompilar la aplicación.

---

## Licencia

Consulta el archivo [`LICENSE`](LICENSE) para conocer los términos de distribución del proyecto.
