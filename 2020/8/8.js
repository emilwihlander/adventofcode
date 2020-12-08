const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n').map(l => {const [i, n] = l.split(' '); return [i, parseInt(n)]}).filter(c => !isNaN(c[1]));

//let accumulator = 0;
//let counter = 0;
//
//const visited = lines.map(l => false);
//
//while (!visited[counter]) {
//  const [i, n] = lines[counter];
//  visited[counter] = true;
//  if (i == 'nop') {
//    counter++;
//  }
//  if (i == 'acc') {
//    accumulator += n;
//    counter++;
//  }
//  if (i == 'jmp') {
//    counter += n;
//  }
//}

for (j = 0; j < lines.length; j++) {
  let accumulator = 0;
  let counter = 0;

  const c = lines.map(l => [...l]);
  const visited = lines.map(l => false);
  if (c[j][0] == 'nop') c[j][0] = 'jmp';
  else if (c[j][0] == 'jmp') c[j][0] = 'nop';


  while (!visited[counter] && counter < c.length) {
    const [i, n] = c[counter];
    visited[counter] = true;
    if (i == 'nop') {
      counter++;
    }
    if (i == 'acc') {
      accumulator += n;
      counter++;
    }
    if (i == 'jmp') {
      counter += n;
    }
  }

  if (counter >= c.length) console.log("res:", accumulator)
}

//console.log(accumulator)