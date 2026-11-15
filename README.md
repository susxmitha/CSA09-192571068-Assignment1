# CSA09 - Programming in Java - Assignment 1 (CO1)

**Campus Smart-Parking System: design, implementation and validation of an object-oriented model**

| | |
|---|---|
| Name | Susmitha |
| Register number | 192571068 |
| Java version | OpenJDK 21 (LTS) |

## How to compile and run

Open a terminal in the folder of the task. Single-file tasks are compiled with `javac` and run with `java`.
Task 11 and Task 12 are packaged projects and need the `-d out` / `-cp out` form.

**Linux / macOS**
```
cd Task11
javac -d out $(find src -name "*.java")
java -cp out parking.app.Main input/scenario.txt
```
**Windows PowerShell**
```
cd Task11
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out parking.app.Main input/scenario.txt
```

## Tasks

| Folder | Program(s) | Compile and run | Result |
|---|---|---|---|
| Task01 | `VehicleStorageDemo.java` | `javac VehicleStorageDemo.java` then `java VehicleStorageDemo` | Parallel arrays and Vehicle objects both search in O(n); objects keep the data together |
| Task02 | `CastingDemo.java` | `javac CastingDemo.java` then `java CastingDemo` | `minutes / 60` gives Rs 60 for 130 min; the integer ceiling `(minutes + 59) / 60` gives the correct Rs 90 |
| Task03 | `ParkingFacility.java` (Complete the Code), `FastParkingFacility.java`, `AllocBenchmark.java` | `javac *.java` then `java ParkingFacility`, `java FastParkingFacility`, `java AllocBenchmark` | Output matches the expected six lines exactly; counts + stack allocation is much faster on large facilities |
| Task04 | `ConstructorDemo.java` | `javac ConstructorDemo.java` then `java ConstructorDemo` | Constructors, chaining, copy constructor, setters and factory compared |
| Task05 | `StaticDemo.java` | `javac StaticDemo.java` then `java StaticDemo` | Instance counter gives duplicate ticket numbers for two facilities; static counter does not |
| Task06 | `ConstructorTrace.java` | `javac ConstructorTrace.java` then `java ConstructorTrace` | Constructor order Vehicle, Car, ElectricCar traced; composition alternative compared |
| Task07 | `OverrideDemo.java` | `javac OverrideDemo.java` then `java OverrideDemo` | Overriding, final, equals/hashCode/toString; switch-based alternative compared |
| Task08 | `FeeDemo.java` (Complete the Code), `trace/DispatchTrace.java` | `javac FeeDemo.java trace/DispatchTrace.java` then `java FeeDemo` and `java trace.DispatchTrace` | Output matches the expected four lines exactly; last line is `true` because equality depends on the registration number only |
| Task09 | `AbstractionDemo.java` | `javac AbstractionDemo.java` then `java AbstractionDemo` | Abstract class + interfaces with default and static methods |
| Task10 | `EncapsulationDemo.java` | `javac EncapsulationDemo.java` then `java EncapsulationDemo` | Validating setters reject bad data; public fields accept it; Integer `==` pitfall shown |
| Task11 | Package project `parking.model`, `parking.service`, `parking.app` | see above, then `java -cp out parking.app.TestRunner input/scenario.txt` and `java -cp out parking.app.LookupBenchmark` | 44 of 44 tests pass; hand-calculated scenario Rs 590 equals program output |
| Task12 | Task 11 project plus 3 model classes (`EBike`, `DeliveryVan`, `OneDayPass`) and `Task12Main` | `javac -d out ...` as above, then `java -cp out parking.app.Task12Main input/scenario12.txt` and `java -cp out parking.app.Task12Tests input/scenario12.txt` | 25 of 25 tests pass; hand-calculated scenario Rs 580 equals program output; 0 existing files changed |

Each task folder also has an `output/` folder with the console output and the screenshots of my run.
