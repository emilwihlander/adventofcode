import { run } from "../common.ts";
import { sumOf, zip } from '@std/collections'

function first(lines: string[]) {
  const [left, right] = parseLines(lines)

  left.sort()
  right.sort()

  console.log(sumOf(zip(left, right).map(([a, b]) => Math.abs(a - b)), v => v))

  let total = 0

  for (let i in left) {
    total += Math.abs(left[i] - right[i])
  }

  // console.log(total)
}

function second(lines: string[]) {
  const [left, right] = parseLines(lines)

  const occurances: Record<number, number> = {}

  for (const n of right) {
    if (!(n in occurances)) {
      occurances[n] = 1
    } else {
      occurances[n]++
    }
  }

  console.log(sumOf(left.map(n => n * (occurances[n] ?? 0)), n => n))
}

function parseLines(lines: string[]): [number[], number[]] {
  const left = []
  const right = []

  for (const line of lines) {
    const [l, r] = line.split('   ').map(v => parseInt(v))
    left.push(l)
    right.push(r)
  }

  return [left, right]
}

run(1, first, second)
