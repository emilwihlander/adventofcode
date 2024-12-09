import { sumOf } from "@std/collections";
import { run } from "../common.ts";

const DAY = 3

const REGEX = new RegExp('mul\\((\\d{1,3}),(\\d{1,3})\\)', 'g')
const REGEX2 = new RegExp("(mul\\((\\d{1,3}),(\\d{1,3})\\)|don't\\(\\)|do\\(\\))", 'g')

function first(lines: string[]) {
  const parsed = parseLines(lines)
  const res = sumOf(parsed.matchAll(REGEX), ([_, a, b]) => parseInt(a) * parseInt(b))
  console.log(res)
}

function second(lines: string[]) {
  const parsed = parseLines(lines)

  let enabled = true
  let sum = 0

  for (const [action, _, a, b] of parsed.matchAll(REGEX2)) {
    if (action == 'do()') {
      enabled = true
    } else if (action == "don't()") {
      enabled = false
    } else if (enabled) {
      sum += parseInt(a) * parseInt(b)
    }
  }
  console.log(sum)
}

function parseLines(lines: string[]): string {
  return lines.join('\n')
}

run(DAY, first, second)
