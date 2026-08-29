#!/usr/bin/env python3
"""Compile Trackie with Java 25 and run UI cases from the Markdown test plan."""

from __future__ import annotations

import difflib
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path


def find_java_command(command: str) -> str:
    """Return the Java 25 SDK command, falling back to the active PATH."""
    sdk_command = Path.home() / ".sdkman" / "candidates" / "java" / "25.0.3.fx-zulu" / "bin" / command
    if sdk_command.exists():
        return str(sdk_command)

    path_command = shutil.which(command)
    if path_command is None:
        raise RuntimeError(f"{command} is not installed")
    return path_command


def confirm_java_25(javac: str) -> None:
    """Raise an error unless the selected compiler is Java 25."""
    result = subprocess.run([javac, "-version"], capture_output=True, text=True, check=True)
    version_text = (result.stdout + result.stderr).strip()
    if not version_text.startswith("javac 25"):
        raise RuntimeError(f"Java 25 is required, but found: {version_text}")


def read_cases(plan_path: Path) -> list[tuple[str, Path, Path]]:
    """Read test case names and file paths from the Markdown table."""
    cases = []
    for line in plan_path.read_text(encoding="utf-8").splitlines():
        if not line.startswith("|"):
            continue
        cells = [cell.strip() for cell in line.strip("|").split("|")]
        if len(cells) != 3 or cells[0] == "Aim" or set(cells[0]) == {"-"}:
            continue
        cases.append((cells[0], plan_path.parents[0] / cells[1], plan_path.parents[0] / cells[2]))
    return cases


def normalize_newlines(text: str) -> str:
    """Normalize platform-specific newlines without hiding other differences."""
    return text.replace("\r\n", "\n")


def main() -> int:
    """Compile the application and run each planned test case."""
    repository = Path(sys.argv[1] if len(sys.argv) > 1 else ".").resolve()
    plan_path = repository / "test" / "ui-test-plan.md"
    source_files = sorted((repository / "src" / "main" / "java").glob("*.java"))
    cases = read_cases(plan_path)

    if not source_files:
        raise RuntimeError("No Java source files found")
    if not cases:
        raise RuntimeError("No UI test cases found in the test plan")

    javac = find_java_command("javac")
    java = find_java_command("java")
    confirm_java_25(javac)

    with tempfile.TemporaryDirectory(prefix="trackie-ui-test-") as build_directory:
        subprocess.run(
            [javac, "-d", build_directory, *map(str, source_files)],
            cwd=repository,
            check=True,
        )

        for name, input_path, expected_path in cases:
            user_input = input_path.read_text(encoding="utf-8")
            expected = normalize_newlines(expected_path.read_text(encoding="utf-8"))
            with tempfile.TemporaryDirectory(prefix="trackie-ui-case-") as case_directory:
                result = subprocess.run(
                    [java, "-cp", build_directory, "Trackie"],
                    cwd=case_directory,
                    input=user_input,
                    capture_output=True,
                    text=True,
                )
            actual = normalize_newlines(result.stdout)

            print(f"=== {name}: INPUT ===")
            print(user_input, end="" if user_input.endswith("\n") else "\n")
            print(f"=== {name}: OUTPUT ===")
            print(actual, end="" if actual.endswith("\n") else "\n")

            if result.returncode != 0 or actual != expected:
                print(f"FAILED: {name}", file=sys.stderr)
                if result.stderr:
                    print(result.stderr, file=sys.stderr)
                print("".join(difflib.unified_diff(
                    expected.splitlines(keepends=True),
                    actual.splitlines(keepends=True),
                    fromfile="expected",
                    tofile="actual",
                )), file=sys.stderr)
                return 1

            print(f"PASSED: {name}")

    print(f"All {len(cases)} UI test case(s) passed.")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, RuntimeError, subprocess.CalledProcessError) as error:
        print(f"UI test setup failed: {error}", file=sys.stderr)
        raise SystemExit(1)
