# Build on Windows (no global Gradle install)

## 1) Install Java 21
Install JDK 21 and make sure `java` works in Command Prompt:

```bat
java -version
```

## 2) Build with the Gradle Wrapper (auto-downloads its jar)
This project includes `gradlew.bat`. If the wrapper jar is missing, it will download it automatically.

## 3) Build the mod
Open Command Prompt in this project folder (where `build.gradle` is) and run:

```bat
.\gradlew.bat build
```

## 4) Find the jar
After a successful build, your mod jar will be in:

```
build\libs\
```

Put that jar in your Minecraft client `mods` folder.
