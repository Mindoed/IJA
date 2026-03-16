JUNIT="../junit-platform-console-standalone-1.11.4.jar"

# Smazani predchozich kompilaci:
echo "Cleaning previous builds..."
rm -rf cls

# Kompilace s vyuzitim JUnit:
echo ""
echo "Compiling tests..."
javac -cp .:${JUNIT} -d cls ija/homework1/scheme/Homework1Test.java

# Spusteni testu s vyuzitim JUnit:
echo ""
echo "Running tests..."
java -ea -jar ./${JUNIT} execute --cp cls --scan-class-path