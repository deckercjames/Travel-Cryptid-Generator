
# Check iv Maven exists to compile the code
if ! command -v mvn &> /dev/null
then
    echo "maven (mvn) could not be found. See https://maven.apache.org/install.html"
    exit 1
fi

# Compile
mvn clean compile

# Check return code of compilation
if [ $? -ne 0 ]; then
    echo "Failed to build"
    exit 1
fi

# Build all the class paths, with references to all jar dependencies
CLASS_PATHS="target/classes"
for JAR in lib/*; do CLASS_PATHS="${CLASS_PATHS}:${JAR}"; done

# Run the program in GUI mode
java -cp "$CLASS_PATHS" main.java.main.Cryptid
