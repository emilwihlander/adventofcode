import { minBy, sumOf } from "@std/collections";
import { run } from "../common.ts";

const DAY = 5

function first(lines: string[]) {
  const { rules, updates } = parseLines(lines)
  const res = sumOf(updates.filter(u => isValid(u, rules)).map(u => u[u.length/2|0]), p => parseInt(p))
  console.log(res)
}


function second(lines: string[]) {
  const { rules, updates } = parseLines(lines)
  const ordered = updates.filter(u => !isValid(u, rules)).map(u => correctOrder(u, rules))
  const res = sumOf(ordered.map(u => u[u.length/2|0]), p => parseInt(p))
  console.log(res)
}

function isValid(update: string[], rules: Record<string, string[]>): boolean {
  for (const [i, v] of update.entries()) {
    const rule = rules[v] ?? []
    const previous = update.slice(0, i)
    for (const prev of previous) {
      if (rule.some(r => r == prev)) {
        return false
      }
    }
  }
  return true
}

function correctOrder(update: string[], rules: Record<string, string[]>): string[] {
  const corrected: string[] = []
  update.forEach(p => {
    const rule = rules[p] ?? []

    const prev = rule.map(r => corrected.indexOf(r)).filter(i => i != -1)

    if (prev.length == 0) {
      corrected.push(p)
    } else {
      const insertAtIndex = minBy(prev, n => n)!
      corrected.splice(insertAtIndex, 0, p)
    }
  })
  return corrected
}

function parseLines(lines: string[]) {
  const cutoff = lines.indexOf('');

  const rules = parseRules(lines.slice(0, cutoff))
  const updates = parseUpdates(lines.slice(cutoff + 1))
  return {
    rules,
    updates,
  }
}

function parseRules(lines: string[]) {
  const rules: Record<string, string[]> = {}
  lines.forEach(l => {
    const [before, after] = l.split('|')
    if (!(before in rules)) {
      rules[before] = [after]
    } else {
      rules[before].push(after)
    }
  })
  return rules
}

function parseUpdates(lines: string[]) {
  return lines.map(l => l.split(','))
}

run(DAY, first, second)

