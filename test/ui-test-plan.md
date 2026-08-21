# UI Test Plan

Each case runs in a fresh Trackie process. Input and expected-output paths are relative to this `test` directory.

| Aim | Input | Expected output |
| --- | --- | --- |
| Verify the complete Level 4 workflow | ui/level4-input.txt | ui/level4-expected.txt |
| Verify minimal Level 5 errors preserve task state | ui/level5-minimal-input.txt | ui/level5-minimal-expected.txt |
