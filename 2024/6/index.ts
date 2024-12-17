import { run } from "../common.ts";

const DAY = 6

let visitedPositions: number[][] = []

function first(lines: string[]) {
  const guard = parseLines(lines)

  while (guard.insideMap()) {
    if(guard.obstructionInfront()){
      guard.turn()
    } else {
      guard.keepWalking()
    }
  }
  visitedPositions = guard.getVisitedPositions()
  console.log(guard.numberOfVisitedPositions())
}

function second(lines: string[]) {
  const grid = lines.map(l => l.split(''))
  let count = 0
  for (const [x, y] of visitedPositions) {
    if (grid[y][x] == '^') {
      continue
    }

    grid[y][x] = '#'

    const guard = new GuardMap2(grid)

    while (guard.insideMap() && !guard.insideLoop()) {
      if(guard.obstructionInfront()){
        guard.turn()
      } else {
        guard.keepWalking()
      }
    }

    if (guard.insideLoop()) {
      count++
    }

    grid[y][x] = '.'

  }
  console.log(count)
}

function parseLines(lines: string[]) {
  return new GuardMap(lines)
}

type Direction = 'N' | 'E' | 'S' | 'W'

class GuardMap {
  private map: string[]
  private height: number
  private width: number

  private positionX: number
  private positionY: number
  private direction: Direction

  private visited: Set<string>

  constructor (lines: string[]) {
    this.map = lines
    this.height = lines.length
    this.width = lines[0].length

    this.positionY = lines.findIndex(line => (line.includes('^')))
    this.positionX = lines[this.positionY].indexOf('^')
    this.direction = 'N'

    this.visited = new Set()
    this.visited.add(this.posKey(this.positionX, this.positionY))
  }

  numberOfVisitedPositions(): number {
    return this.visited.size
  }

  getVisitedPositions(): number[][] {
    return this.visited.values().map(s => s.split('|').map(v => parseInt(v))).toArray()
  }

  keepWalking(): void {
    const [x, y] = this.nextPosition()
    if (this._insideMap(x, y)) {
      this.visited.add(this.posKey(x, y))
    }

    this.positionX = x
    this.positionY = y
  }

  turn(): void {
    switch (this.direction) {
      case "N":
        this.direction = 'E'
        break
      case "E":
        this.direction = 'S'
        break
      case "S":
        this.direction = 'W'
        break
      case "W":
        this.direction = 'N'
        break
    }
  }

  obstructionInfront(): boolean {
    const [x, y] = this.nextPosition()
    return this.get(x, y) == '#'
  }

  insideMap(): boolean {
    return this._insideMap(this.positionX, this.positionY)
  }

  private nextPosition(): [number, number] {
    switch (this.direction) {
      case "N": return [this.positionX, this.positionY - 1]
      case "E": return [this.positionX + 1, this.positionY]
      case "S": return [this.positionX, this.positionY + 1]
      case "W": return [this.positionX - 1, this.positionY]
    }
  }

  private get(x: number, y: number): '.' | '#' {
    if (!this._insideMap(x, y))
      return '.'
    if (this.map[y][x] == '#') {
      return '#'
    } else {
      return '.'
    }
  }

  private _insideMap(x: number, y: number): boolean {
    return x >= 0 && x < this.width && y >= 0 && y < this.height
  }

  private posKey(x: number, y: number) {
    return `${x}|${y}`
  }
}

class GuardMap2 {
  private grid: string[][]
  private height: number
  private width: number

  private positionX: number
  private positionY: number
  private direction: Direction

  private visited: Set<string>
  private _insideLoop: boolean

  constructor (grid: string[][]) {
    this.grid = grid
    this.height = grid.length
    this.width = grid[0].length

    this.positionY = grid.findIndex(line => (line.includes('^')))
    this.positionX = grid[this.positionY].indexOf('^')
    this.direction = 'N'

    this.visited = new Set()
    this._insideLoop = false
    this.visited.add(this.posKey(this.positionX, this.positionY, this.direction))
  }

  numberOfVisitedPositions(): number {
    return this.visited.size
  }

  keepWalking(): void {
    const [x, y] = this.nextPosition()
    if (this._insideMap(x, y)) {
      this.addVisited(x, y, this.direction)
    }

    this.positionX = x
    this.positionY = y
  }

  turn(): void {
    switch (this.direction) {
      case "N":
        this.direction = 'E'
        break
      case "E":
        this.direction = 'S'
        break
      case "S":
        this.direction = 'W'
        break
      case "W":
        this.direction = 'N'
        break
    }
    this.addVisited(this.positionX, this.positionY, this.direction)
  }

  obstructionInfront(): boolean {
    const [x, y] = this.nextPosition()
    return this.get(x, y) == '#'
  }

  insideMap(): boolean {
    return this._insideMap(this.positionX, this.positionY)
  }

  insideLoop(): boolean {
    return this._insideLoop
  }

  private addVisited(x: number, y: number, direction: Direction): void {
    const key = this.posKey(x, y, direction)
    if (this.visited.has(key)) {
      this._insideLoop = true
    }

    this.visited.add(key)
  }

  private nextPosition(): [number, number] {
    switch (this.direction) {
      case "N": return [this.positionX, this.positionY - 1]
      case "E": return [this.positionX + 1, this.positionY]
      case "S": return [this.positionX, this.positionY + 1]
      case "W": return [this.positionX - 1, this.positionY]
    }
  }

  private get(x: number, y: number): '.' | '#' {
    if (!this._insideMap(x, y))
      return '.'
    if (this.grid[y][x] == '#') {
      return '#'
    } else {
      return '.'
    }
  }

  private _insideMap(x: number, y: number): boolean {
    return x >= 0 && x < this.width && y >= 0 && y < this.height
  }

  private posKey(x: number, y: number, direction: Direction) {
    return `${x}|${y}|${direction}`
  }
}

run(DAY, first, second)
