const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n');

const res = lines.map(l => {
  let r_lower = 0;
  let r_upper = 128;
  let s_lower = 0;
  let s_upper = 8;

  l.split('').forEach(c => {
    switch (c) {
      case 'F': r_upper = (r_lower + r_upper) / 2; break;
      case 'B': r_lower = (r_lower + r_upper) / 2; break;
      case 'L': s_upper = (s_lower + s_upper) / 2; break;
      case 'R': s_lower = (s_lower + s_upper) / 2; break;
    }
  })
  return r_lower * 8 + s_lower;
}).sort()

//.reduce((a,b) => (a > b) ? a : b); PART 1
//console.log(res);

for (i = 0; i < res.length - 1; i++) {
  if (res[i] + 2 == res[i+1]) console.log(res[i] + 1)
}
