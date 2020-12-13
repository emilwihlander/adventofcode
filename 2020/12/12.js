const fs = require('fs');

const lines = fs
  .readFileSync('input.txt')
  .toString()
  .split('\n')
  .filter(l => l.length > 0)
  .map(l => [l[0], parseInt(l.substring(1))])
  //.map(l => l.split(''))
  //.map(l => parseInt(l))


//let dir = 0;
//let pos = [0, 0];
//
//const directions = [[0,1], [-1,0], [0,-1], [1,0]];
//
//lines.forEach(([o, v]) => {
//  switch (o) {
//    case 'N':
//      pos[0] += v;
//      break;
//    case 'S':
//      pos[0] -= v;
//      break;
//    case 'E':
//      pos[1] += v;
//      break;
//    case 'W':
//      pos[1] -= v;
//      break;
//    case 'L':
//      dir -= v / 90;
//      if (dir < 0) dir += 4;
//      break;
//    case 'R':
//      dir = (dir + v /90) % 4;
//      break;
//    case 'F':
//      pos = pos.map((p, i) => p + directions[dir][i] * v)
//      break;
//  }
//})

let wp = [1, 10];
let pos = [0,0];

lines.forEach(([o, v]) => {
  switch (o) {
    case 'N':
      wp[0] += v;
      break;
    case 'S':
      wp[0] -= v;
      break;
    case 'E':
      wp[1] += v;
      break;
    case 'W':
      wp[1] -= v;
      break;
    case 'F':
      for (let i = 0; i < v; i++) {
        pos[0] += wp[0]
        pos[1] += wp[1]
      }
      break
    case 'R':
      for (let i = 0; i < v /90; i++)
        wp = [-wp[1], wp[0]]
      break
    case 'L':
      for (let i = 0; i < v /90; i++)
        wp =[wp[1], -wp[0]]
      break
  }
  //console.log(wp, pos)
})

console.log(pos, pos[0] + pos[1])