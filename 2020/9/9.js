const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n').map(l => parseInt(l));

for (i = 25; i < lines.length; i++) {
  const numbers = lines.slice(i-25,i);
  const current = lines[i];
  let found = false;
  for (a = 0; a < 25; a++) {
    for (b = a + 1; b < 25; b++) {
      if (numbers[a] + numbers[b] == current) found = true;
    }
  }
  if (!found ) console.log(current);
}

const res = 776203571;

for (i = 0; i < lines.length; i++) {
  let sum = 0;

  let j = i
  while (sum < res) {
    sum += lines[j];
    j++;
  }

  if (sum == res) {
    const part = lines.slice(i,j);
    console.log(Math.min(...part) + Math.max(...part))
  }
}