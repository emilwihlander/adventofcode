const t0 = console.time('test');

const start = [0,8,15,2,12,1,4];
//const start = [0,3,6];

const m = new Map();

for (let i = 0; i < start.length; i++) {
  m.set(start[i], [i]);
}

let count = start.length;
let last = start[start.length-1];


while (count < 30000000) {
  if (m.get(last).length == 1) {
    m.set(0,[count, m.get(0)[0]])
    last = 0;
  } else {
    const tmp = m.get(last)
    last = tmp[0] - tmp[1];
    if (m.has(last))
      m.set(last, [count, m.get(last)[0]]);
    else
      m.set(last, [count]);
  }
  count++;
}

const t1 = console.timeEnd('test');

console.log(last)