# CSA09 – Programming in Java – Assignment 1 (CO1)

## Campus Smart-Parking System: Design, Implementation and Validation of an Object-Oriented Model

### Student Details

| Field | Details |
|---|---|
| Name | S Susmitha |
| Register Number | 192571068 |
| Course | Programming in Java |
| Course Code | CSA09 |
| Assignment | Assignment 1 – CO1 |
| Java Version | OpenJDK 21 (LTS) |
| Repository | `CSA09-192571068-Assignment1` |

---

## 1. Project Overview

This repository contains the implementation and experimental work completed for Programming in Java – Assignment 1 (CO1).

The assignment focuses on applying Object-Oriented Programming concepts through the development of a Campus Smart-Parking System.

The work covers object creation, constructors, inheritance, polymorphism, abstraction, encapsulation, packages, interfaces, static members, method overriding, and system-level implementation.

The final tasks extend the parking system with additional vehicle types, pass categories, validation, testing and benchmarking.

---

## 2. Technologies Used

- Java – OpenJDK 21 (LTS)
- Java Compiler (`javac`)
- Java Runtime (`java`)
- Visual Studio Code
- Windows Command Prompt / Terminal
- Git and GitHub

---

## 3. Repository Structure

```text
CSA09-192571068-Assignment1/
│
├── README.md
├── .gitignore
│
├── Task01/
│   ├── VehicleStorageDemo.java
│   └── output/
│       ├── Task01.png
│       └── VehicleStorageDemo_output.txt
│
├── Task02/
│   ├── CastingDemo.java
│   └── output/
│       ├── Task02.png
│       └── CastingDemo_output.txt
│
├── Task03/
│   ├── ParkingFacility.java
│   ├── FastParkingFacility.java
│   ├── AllocBenchmark.java
│   └── output/
│
├── Task04/
│   ├── ConstructorDemo.java
│   └── output/
│
├── Task05/
│   ├── StaticDemo.java
│   └── output/
│
├── Task06/
│   ├── ConstructorTrace.java
│   └── output/
│
├── Task07/
│   ├── OverrideDemo.java
│   └── output/
│
├── Task08/
│   ├── FeeDemo.java
│   ├── trace/
│   │   └── DispatchTrace.java
│   └── output/
│
├── Task09/
│   ├── AbstractionDemo.java
│   └── output/
│
├── Task10/
│   ├── EncapsulationDemo.java
│   └── output/
│
├── Task11/
│   ├── input/
│   │   └── scenario.txt
│   ├── src/
│   │   └── parking/
│   │       ├── app/
│   │       ├── model/
│   │       └── service/
│   └── output/
│
└── Task12/
    ├── input/
    │   └── scenario12.txt
    ├── src/
    │   └── parking/
    │       ├── app/
    │       ├── model/
    │       └── service/
    └── output/
```

Each task contains its corresponding Java source files. The output folders preserve the execution results and screenshots used as implementation evidence.

---

## 4. Task-wise Implementation and Results

| Task | Topic / Program | Implementation and Result |
|---|---|---|
| Task 01 | Vehicle Storage – Arrays vs Objects | Compared parallel arrays with object-based storage. Both approaches use linear search, while objects provide better data organisation. |
| Task 02 | Data Types and Type Casting | Demonstrated type conversion and identified the correct ceiling-based billing calculation for parking duration. |
| Task 03 | ParkingFacility – Complete the Code | Implemented the parking facility and compared the original implementation with an optimised version using allocation benchmarking. |
| Task 04 | Constructors and Object Creation | Demonstrated constructors, constructor chaining, copy constructors, setters and factory-based object creation. |
| Task 05 | Static Members and Object Lifecycle | Compared instance-level and static counters to demonstrate shared ticket-number generation across multiple facilities. |
| Task 06 | Inheritance and Constructor Execution | Demonstrated constructor execution order in a Vehicle–Car–ElectricCar hierarchy and compared inheritance with composition. |
| Task 07 | Method Overriding and Object Methods | Demonstrated overriding, final methods, equals(), hashCode() and toString(). |
| Task 08 | Polymorphism and FeeDemo | Implemented fee calculation using polymorphism and examined method dispatch through an additional trace program. |
| Task 09 | Abstraction | Demonstrated abstract classes, interfaces, default methods and static interface methods. |
| Task 10 | Encapsulation and Packages | Demonstrated data hiding, validation through setters and the consequences of directly accessing public fields. |
| Task 11 | Complete Smart-Parking System | Integrated the parking application using model, service and application packages. Included scenario execution, automated tests and lookup benchmarking. |
| Task 12 | Design Challenge Extension | Extended the system with EBike, DeliveryVan and OneDayPass. Added execution scenarios and automated tests while preserving existing files. |

---

## 5. Compilation and Execution Instructions

### Prerequisites

Install Java Development Kit (JDK) 21 or a compatible newer version.

Verify the installation:

```bash
java -version
javac -version
```

### Tasks 01–02 and 04–07, 09–10

Open a terminal in the respective task folder.

Compile:

```bash
javac FileName.java
```

Run:

```bash
java ClassName
```

Replace `FileName.java` and `ClassName` with the corresponding source file and public class name.

### Task 03 – Parking Facility

Navigate to `Task03`:

```bash
cd Task03
```

Compile:

```bash
javac *.java
```

Run the programs:

```bash
java ParkingFacility
java FastParkingFacility
java AllocBenchmark
```

### Task 08 – Polymorphism

Navigate to `Task08`:

```bash
cd Task08
```

Compile:

```bash
javac FeeDemo.java trace/DispatchTrace.java
```

Run:

```bash
java FeeDemo
java trace.DispatchTrace
```

### Task 11 – Complete Smart-Parking System

Navigate to `Task11`:

```bash
cd Task11
```

Compile all source files:

```bash
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

Run the main application:

```bash
java -cp out parking.app.Main input/scenario.txt
```

Run the automated tests:

```bash
java -cp out parking.app.TestRunner input/scenario.txt
```

Run the lookup benchmark:

```bash
java -cp out parking.app.LookupBenchmark
```

### Task 12 – Design Challenge Extension

Navigate to `Task12`:

```bash
cd Task12
```

Compile all source files:

```bash
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

Run the extended application:

```bash
java -cp out parking.app.Task12Main input/scenario12.txt
```

Run the extension tests:

```bash
java -cp out parking.app.Task12Tests input/scenario12.txt
```

**Note:** The package-project compilation commands above use Windows PowerShell syntax. In Linux or macOS terminals, use:

```bash
javac -d out $(find src -name "*.java")
```

---

## 6. Testing and Validation Summary

The final implementation includes execution evidence, scenario-based validation and automated testing.

| Validation Area | Recorded Result |
|---|---|
| Task 03 | Output compared with the expected result |
| Task 08 | Output compared with the expected result |
| Task 11 | 44 out of 44 tests passed |
| Task 11 | Scenario total: ₹590 |
| Task 12 | 25 out of 25 tests passed |
| Task 12 | Scenario total: ₹580 |
| Task 12 | Existing project files preserved |

The scenario totals were checked against manually calculated values.

The output screenshots and console-output files are available inside the respective task folders.

---

## 7. Evidence and Documentation

The repository includes:

- Java source code for all 12 tasks.
- Completed programs and supporting classes.
- Input scenario files for Tasks 11 and 12.
- Console output files.
- Execution screenshots.
- Automated test results.
- Benchmark output files.
- Supporting project packages and classes.

The `output/` directories contain the recorded execution evidence for the corresponding tasks.

---

## 8. Conclusion

This assignment demonstrates the practical application of Java Object-Oriented Programming concepts through the development of a Campus Smart-Parking System.

The implementation progresses from individual programming concepts to an integrated, package-based application. The final extension demonstrates how an existing system can be expanded with additional vehicle categories and pass types while maintaining modularity, validation and compatibility.

The completed repository provides the source code, execution evidence, test results and supporting files required for academic verification.
