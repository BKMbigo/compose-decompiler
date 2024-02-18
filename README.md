# Compose Decompiler

Ever wondered what happens to your code when the Compose Compiler gets its hands on it? This tool is here to help you
see the changes made by the Compose Compiler, giving you a peek into the transformed code.

[Screencast from 2024-02-17 21-04-11.webm](https://github.com/BKMbigo/compose-decompiler/assets/102836149/35e82547-8950-4add-ab46-cca788a5cc8c)


Just keep in mind that the code you'll see isn't Kotlin/Java or JVM bytecode.

## Size

Currently, the application bundles a Kotlin compiler (version `1.9.22`) and a compose compiler (unspecified version).
This significantly increases the size of the application. I will be looking at ways to reduce the size of the
application. If you can help (have a suggestion/idea), please open an issue.

## How to Use

For your convenience, native applications for select platforms (Currently, only `windows`) will be available on the
releases page.

## Similar Tools

If you're interested in exploring other tools that dig into Kotlin or Jetpack Compose code, take a look at these:

- [kotlin-explorer](https://www.github.com/romainguy/kotlin-explorer) - A handy desktop tool for diving into
  disassembled Kotlin code.
- [decomposer](https://www.github.com/takahirom/decomposer) - A Gradle plugin that translates bytecode compiled with
  Jetpack Compose into Java.

## Dependencies

The Compose Decompiler relies on a couple of libraries:

- [Compose Desktop](https://jb.gg/start-cmp)
- [RSyntaxTextArea](https://bobbylight.github.io/RSyntaxTextArea)

## Contribution

We welcome contributions! If you have ideas, found bugs, or want new features, open an issue and consider sending a pull
request.

### Building Locally

To make changes and test the Compose Decompiler locally, follow these steps:

1. Clone the repository: `git clone https://www.github.com/BKMbigo/compose-decompiler.git`
2. Open the code in an IntelliJ-Based IDE. (Android Studio or IntelliJ)
3. Run the desktop version from the IDE

