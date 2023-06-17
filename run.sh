
mvn compile

CLASS_PATHS="target/classes"
for JAR in lib/*; do CLASS_PATHS="${CLASS_PATHS}:${JAR}"; done

java -cp "$CLASS_PATHS" main.Cryptid