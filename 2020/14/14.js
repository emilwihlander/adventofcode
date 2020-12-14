const fs = require('fs');

const toBin = (v) => {
  if (v < 2) return ["" + v];
  return [...toBin(Math.floor(v/2)), "" + v % 2];
}

const toDec = (v) => {
  let res = 0;
  v.forEach(b => {res *= 2; res += parseInt(b)});
  return res;
}

const to36 = (a) => {
  return [...Array(36-a.length).fill('0'), ...a]
}

//const applyMask = (v, m) => {
//  return v.map((b, i) => {
//    if (m[i] == 'X') return b;
//    else return m[i];
//  })
//}
//
//const lines = fs
//  .readFileSync('input.txt')
//  .toString()
//  .split('\n')
//  .filter(l => l.length > 0)
//  .map(l => l.split(' = '))
//  .map(([cmd, val]) => {
//    if (cmd == 'mask') return ['mask', val.split('')]
//    else return ['mem', parseInt(cmd.match(/mem\[(\d+)\]/)[1]), to36(toBin(parseInt(val)))]
//  })
//
//let mask = []
//
//const memory = new Map();
//
//
//lines.forEach(cmd => {
//  if (cmd[0] == 'mask') {
//    mask = cmd[1]
//  } else {
//    memory.set(cmd[1], applyMask(cmd[2], mask))
//  }
//})
//
//let sum = 0;
//
//memory.forEach(v => sum += toDec(v))
//
//console.log(sum)

const lines = fs
  .readFileSync('input.txt')
  .toString()
  .split('\n')
  .filter(l => l.length > 0)
  .map(l => l.split(' = '))
  .map(([cmd, val]) => {
    if (cmd == 'mask') return ['mask', val.split('')]
    else return ['mem', to36(toBin(parseInt(cmd.match(/mem\[(\d+)\]/)[1]))), parseInt(val)]
  })

const applyMask = (v, m) => {
  let res = [[]];
  v.forEach((b, i) => {
    if (m[i] == '0')
      res.forEach(r => r.push(b))
    else if (m[i] == '1')
      res.forEach(r => r.push(1))
    else {
      res = [...res.map(r => [...r, 1]), ...res.map(r => [...r, 0])]
    }
  })
  return res.map(r => toDec(r));
}

let mask = []
const memory = new Map();

lines.forEach(cmd => {
  if (cmd[0] == 'mask')
    mask = cmd[1];
  else {
    const addresses = applyMask(cmd[1], mask);
    addresses.forEach(a => memory.set(a, cmd[2]))
  }
})

let sum = 0;

memory.forEach(v => sum += v)

console.log(sum)
