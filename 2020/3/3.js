const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n');

const res = [[1,1], [3,1], [5,1], [7,1], [1,2]]
  .map(([r, d]) => lines.filter((_,i) => !(i % d)).filter((l,i) => l[i*r % l.length] == '#').length)
  .reduce((a,b) => a*b);

console.log(res)