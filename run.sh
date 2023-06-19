
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

# Run the program in GUI mode
java -classpath "target/classes:lib/*" main.java.main.Cryptid
