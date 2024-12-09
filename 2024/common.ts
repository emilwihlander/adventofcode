export function run(day: number, first: (lines: string[]) => void, second: (lines: string[]) => void) {
  const test = readTest(day)
  const main = readInput(day)
  console.log()
  console.log("=== TEST ===")
  console.log("First:")
  first(test)
  console.log()
  console.log("Second:")
  second(test)
  console.log()
  console.log("=== MAIN ===")
  console.log("First:")
  first(main)
  console.log()
  console.log("Second:")
  second(main)
  console.log()
}

function readTest(day: number): string[] {
  return readLines(day + '/test.txt')
}

function readInput(day: number): string[] {
  return readLines(day + '/input.txt')
}

function readLines(path: string): string[] {
  const lines = Deno.readTextFileSync(path).split('\n')
  if (lines.at(-1) === '') {
    return lines.slice(0,-1)
  } else {
    return lines
  }
}