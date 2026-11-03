echo "Limpiando resultados anteriores.."
rm -rf resultados/

echo "Compilando el proyecto..."
mvn clean install

echo "Ejecutando el programa..."
mkdir resultados
java -jar target/lobos-jssp-1.0-SNAPSHOT.jar parametros/config.properties > resultados/console.log
echo "Ejecución finalizada. Los resultados se encuentran en la carpeta resultados/ y el log de la consola en resultados/console.log"