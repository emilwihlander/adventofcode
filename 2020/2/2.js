const fs = require('fs');

const file = fs.readFileSync('input.txt').toString().split('\n');

let valid = 0;

console.log("PART 1")
file.forEach(line => {
  const match = line.match(/(\d+)-(\d+) (\w): (\w+)/);
  if (!match) return;
  const [_, min, max, char, s] = match;
  const count = Array.from(s).filter(c => (c === char)).length;
  if (min <= count && count <= max) valid++;
})

console.log(valid)

console.log("PART 2")

let valid2 = 0;

file.forEach(line => {
  const match = line.match(/(\d+)-(\d+) (\w): (\w+)/);
  if (!match) return;
  const [_, first, second, char, s] = match;
  if ((s[first-1] == char || s[second-1] == char) && s[first-1] != s[second-1]) valid2++;
})
console.log(valid2)