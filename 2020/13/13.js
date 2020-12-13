const fs = require('fs');

const lines = fs
  .readFileSync('input.txt')
  .toString()
  .split('\n')
  .filter(l => l.length > 0)

//const ts = parseInt(lines[0]);
//const busses = lines[1].split(',').filter(id => id != 'x').map(id => parseInt(id));
//
//let current = ts;
//
//while (!busses.some(b => current % b == 0)) current++;
//
//console.log(busses.find(b => current % b == 0) * (current - ts));

const busses = lines[1].split(',').map(id => (id == 'x') ? -1 : parseInt(id))

busses.forEach((b,i) => {if (b != -1) console.log(i,b)})

//Wolframalpha:
//x mod 23 = 0, (x+13) mod 41 = 0, (x+23) mod 829 = 0, (x+36) mod 13 = 0, (x+37) mod 17 = 0, (x+52) mod 29 = 0,(x+54) mod 677= 0,(x+60) mod 37= 0,(x+73) mod 19= 0
