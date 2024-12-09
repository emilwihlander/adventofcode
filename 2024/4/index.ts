import { run } from "../common.ts";

const DAY = 4

const DIRECTIONS = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'] as const

type Direction = (typeof DIRECTIONS)[number];
type Change = 'INC' | 'DEC' | 'STAY'

function first(lines: string[]) {
  const board = parseLines(lines)
  let count = 0
  for (let y = 0; y < board.height; y++) {
    for (let x = 0; x < board.width; x++) {
      if (board.get(x, y) == 'X') {
        for (const direction of DIRECTIONS) {
          if (board.hasMatch(direction, x, y)) {
            count++
          }
        }
      }
    }
  }

  console.log(count)
}

function second(lines: string[]) {
  const board = parseLines(lines)
  let count = 0
  for (let y = 1; y < board.height - 1; y++) {
    for (let x = 1; x < board.width - 1; x++) {
      if (board.matchXmas(x, y)) {
        count++
      }
    }
  }
  console.log(count)
}

function parseLines(lines: string[]) {
  return new Board(lines)
}

class Board {
  lines: string[]
  width: number
  height: number

  constructor(lines: string[]) {
    this.lines = lines
    this.width = lines[0].length
    this.height = lines.length
  }

  get(x: number, y: number) {
    return this.lines[y][x]
  }

  hasMatch(direction: Direction, x: number, y: number): boolean {
    switch (direction) {
      case "N": return this.hasMatchGeneric(x, y, 'STAY', 'DEC')
      case "NE": return this.hasMatchGeneric(x, y, 'INC', 'DEC')
      case "E": return this.hasMatchGeneric(x, y, 'INC', 'STAY')
      case "SE": return this.hasMatchGeneric(x, y, 'INC', 'INC')
      case "S": return this.hasMatchGeneric(x, y, 'STAY', 'INC')
      case "SW": return this.hasMatchGeneric(x, y, 'DEC', 'INC')
      case "W": return this.hasMatchGeneric(x, y, 'DEC', 'STAY')
      case "NW": return this.hasMatchGeneric(x, y, 'DEC', 'DEC')
    }
  }

  private hasMatchGeneric(x: number, y: number, changeX: Change, changeY: Change): boolean {
    if (!this.withinBound(x, changeX, this.width) || !this.withinBound(y, changeY, this.height))
      return false

    const letters = [...Array(4).keys()].map(i => {
      const dx = this.getDiff(i, changeX)
      const dy = this.getDiff(i, changeY)
      return this.lines[y+dy][x+dx]
    })

    return letters.join('') === 'XMAS'
  }

  private withinBound(v: number, change: Change, bound: number): boolean {
    switch (change) {
      case "INC": return v < bound - 3
      case "DEC": return v > 2
      case "STAY": return true
    }
  }

  private getDiff(i: number, change: Change): number {
    switch (change) {
      case "INC": return i
      case "DEC": return -i
      case "STAY": return 0
    }
  }

  matchXmas(x: number, y: number) {
    return this.get(x, y) === 'A'
      && this.test(this.get(x-1, y-1), this.get(x+1, y+1))
      && this.test(this.get(x+1, y-1), this.get(x-1, y+1))
  }

  private test(a: string, b: string) {
    const sorted = [a, b].toSorted()
    return sorted[0] == 'M' && sorted[1] == 'S'
  }
}

run(DAY, first, second)
