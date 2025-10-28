import { sumOf } from "@std/collections";
import { run } from "../common.ts";

const DAY = 7

function first(lines: string[]) {
  const equations = parseLines(lines)

  const res = sumOf(equations.filter(e => fixable(e)), e => e.sum)

  console.log(res)
}

type Equation = {
  sum: number,
  numbers: number[],
  sumBigInt: bigint,
  numbersBigInt: bigint[],
}

function fixable(e: Equation): boolean {
  return recFixable(e, e.numbers[0], 1)
}

function recFixable(e: Equation, currentSum: number, index: number): boolean {
  if (currentSum > e.sum) {
    return false
  }
  if (index == e.numbers.length) {
    return currentSum == e.sum
  }

  const addition = currentSum + e.numbers[index]
  const multiplication = currentSum * e.numbers[index]

  return recFixable(e, addition, index + 1) || recFixable(e, multiplication, index + 1)
}


function second(lines: string[]) {
  const equations = parseLines(lines)
  const res = equations.filter(e => fixable2(e)).reduce((prev, curr) => prev + curr.sumBigInt, BigInt(0))

  console.log(concat(BigInt(123), BigInt(45)))
  console.log(concat(BigInt(123), BigInt(1)))
  console.log(concat(BigInt(123), BigInt(100)))
  console.log(res)
}

function fixable2(e: Equation): boolean {
  return recFixable2(e, BigInt(e.numbers[0]), 1)
}

function concat(a: bigint, b: bigint): bigint {
  let mult = BigInt(10)
  while (mult <= b) {
    mult *= BigInt(10)
  }
  return a * mult + b
}

function recFixable2(e: Equation, currentSum: bigint, index: number): boolean {
  if (currentSum > e.sum) {
    return false
  }
  if (index == e.numbers.length) {
    return currentSum == e.sumBigInt
  }

  const concatenation = concat(currentSum, e.numbersBigInt[index])
  const multiplication = currentSum * e.numbersBigInt[index]
  const addition = currentSum + e.numbersBigInt[index]

  return recFixable2(e, concatenation, index + 1) || recFixable2(e, multiplication, index + 1) || recFixable2(e, addition, index + 1)
}


function parseLines(lines: string[]): Equation[] {
  return lines.map(l => {
    const [sum, ...rest] = l.split(/:? /)
    return {
      sum: parseInt(sum),
      sumBigInt: BigInt(sum),
      numbers: rest.map(n => parseInt(n)),
      numbersBigInt: rest.map(n => BigInt(n))
    }
  })
}

run(DAY, first, second)
