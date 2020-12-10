const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n').map(l => parseInt(l));

let ones = 0;
let threes = 0;

const res = [0, ...lines.sort((a,b) => a - b)]

console.log(res)

for (i = 0; i < res.length - 1; i++ ) {
  if (res[i+1] - res[i] == 1) ones++;
  if (res[i+1] - res[i] == 3) threes++;

}

console.log(ones, threes, ones * (threes+1));
console.log(res.slice(50))

const p = res.map(v => 0);

p[p.length-1] = 1;
p[p.length-2] = 1;
p[p.length-3] = 2;

for (i = res.length - 4; i >= 0; i--) {
  let sum = 0;
  if (res[i+3] - res[i] <= 3) sum += p[i+3]
  if (res[i+2] - res[i] <= 3) sum += p[i+2]
  if (res[i+1] - res[i] <= 3) sum += p[i+1]
  p[i] = sum;
}
console.log(p)
console.log(p[0]);