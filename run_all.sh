#!/bin/bash
# Compiles and runs every task (Linux / macOS). Output goes to the screen.
set -e
for t in Task01 Task02 Task04 Task05 Task06 Task07 Task09 Task10; do
  (cd $t && for f in *.java; do javac -d /tmp/out_$t "$f"; java -cp /tmp/out_$t "${f%.java}"; done)
done
(cd Task03 && javac -d /tmp/out_t3 *.java && java -cp /tmp/out_t3 ParkingFacility && java -cp /tmp/out_t3 FastParkingFacility && java -cp /tmp/out_t3 AllocBenchmark)
(cd Task08 && javac -d /tmp/out_t8 FeeDemo.java trace/DispatchTrace.java && java -cp /tmp/out_t8 FeeDemo && java -cp /tmp/out_t8 trace.DispatchTrace)
(cd Task11 && rm -rf out && javac -d out $(find src -name "*.java") && java -cp out parking.app.TestRunner input/scenario.txt)
(cd Task12 && rm -rf out && javac -d out $(find src -name "*.java") && java -cp out parking.app.Task12Tests input/scenario12.txt)
