const fs = require('fs');

const lines = fs
  .readFileSync('input.txt')
  .toString()
  .split('\n')
  .filter(l => l.length > 0)
  .map(l => l.split(''))
  //.map(l => parseInt(l))

console.log(lines)

const adjacent = (grid, R, C) => {
  let count = 0;
  for (let r = R-1; r <= R+1; r++) {
    for (let c = C-1; c <= C+1; c++) {
      if (r < 0 || c < 0 || r >= grid.length || c >=grid[0].length) continue;
      if (!(r == R && c == C) && grid[r][c] == '#') {
        count++;
      }
    }
  }
  return count;
}

const print = (grid) => {
  grid.forEach(l => console.log(l.join('')))
}

const rules = (grid) => {
  const next = grid.map(l => [...l]);
  for (let r = 0; r < grid.length; r++) {
    for (let c = 0; c < grid[0].length; c++) {
      let count = 0;
      [[1,0], [1,1], [0,1], [-1,1], [-1,0], [-1,-1], [0,-1],[1,-1]].forEach(([dr,dc]) => {
        let tmpR = r + dr;
        let tmpC = c + dc;

        let found = false;

        while (tmpC >= 0 && tmpR >= 0 && tmpR < grid.length && tmpC < grid[0].length) {
          if (grid[tmpR][tmpC] == '#') {
            found = true;
            break;
          } else if (grid[tmpR][tmpC] == 'L') {
            break;
          }
          tmpR += dr;
          tmpC += dc;
        }
        if (found) count++;
      })
      //console.log(count)
      if (grid[r][c] == 'L' && count == 0) {
        next[r][c] = '#';
      } else if (grid[r][c] == '#' && count >= 5) {
        next[r][c] = 'L';
      }
      //const count = adjacent(grid, r, c);
      //if (grid[r][c] == 'L' && count == 0) {
      //  next[r][c] = '#';
      //} else if (grid[r][c] == '#' && count >= 4) {
      //  next[r][c] = 'L';
      //}
    }
  }
  return next;
}

const same = (a, b) => {
  for (r = 0; r < a.length; r++) {
    for (c = 0; c < a[0].length; c++) {
      if (a[r][c] != b[r][c]) return false;
    }
  }
  return true;
}
let past = lines.map(l => [...l]);
let curr = rules(past);

while (!same(past, curr)) {
  //print(curr);
  past = curr;
  curr = rules(past);
}

let count = 0;
for (r = 0; r < curr.length; r++) {
  for (c = 0; c < curr[0].length; c++) {
    if (curr[r][c] == '#') count++;
  }
}


console.log(count)
