# UI Test Plan

Each case runs in a fresh Trackie process. Input and expected-output paths are relative to this `test` directory.

| Aim | Input | Expected output |
| --- | --- | --- |
| Verify the complete Level 4 workflow | ui/level4-input.txt | ui/level4-expected.txt |
| Verify minimal Level 5 errors preserve task state | ui/level5-minimal-input.txt | ui/level5-minimal-expected.txt |
| Verify malformed Level 5 commands preserve valid state | ui/level5-errors-input.txt | ui/level5-errors-expected.txt |
| Verify deletion and automatic task renumbering | ui/level6-delete-input.txt | ui/level6-delete-expected.txt |
| Verify saved tasks load after restarting Trackie | ui/level7-load-input.txt | ui/level7-load-expected.txt |
| Verify corrupted saved data is handled and recoverable | ui/level7-corrupt-input.txt | ui/level7-corrupt-expected.txt |
| Verify Level 8 date parsing, formatting, validation, and persistence | ui/level8-dates-input.txt | ui/level8-dates-expected.txt |
| Verify Level 9 keyword search and missing-keyword handling | ui/level9-find-input.txt | ui/level9-find-expected.txt |
