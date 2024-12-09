import { slidingWindows, sumOf } from "@std/collections";
import { run } from "../common.ts";

const DAY = 2

function first(lines: string[]) {
  const parsed = parseLines(lines)
  console.log(sumOf(parsed.filter(r => isSafe(r)), _ => 1))
}

function second(lines: string[]) {
  const parsed = parseLines(lines)

  console.log(sumOf(parsed.filter(report => {
    const options = [report]
    for (const [i] of report.entries()) {
      options.push(report.toSpliced(i, 1))
    }

    for (const option of options) {
      if (isSafe(option)) {
        return true
      }
    }
    return false
  }), _ => 1))
}

function parseLines(lines: string[]) {
  return lines.map(l => l.split(' ').map(d => parseInt(d)))
}

function isSafe(report: number[]): boolean {
  if (report.length <= 1) {
    return true
  }

  let increasing: boolean;

  const firstDiff = report[1] - report[0]

  if (firstDiff > 0) {
    increasing = true
  } else if (firstDiff < 0) {
    increasing = false
  } else {
    return false
  }

  for (const [l, r] of slidingWindows(report, 2)) {
    const change = r-l
    if (increasing && (change < 1 || change > 3)) {
      return false
    }
    if (!increasing && (change > -1 || change < -3)) {
      return false
    }
  }

  return true
}

run(DAY, first, second)
