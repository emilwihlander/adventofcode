const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n');

const bags = new Map();

const canHaveGolden = (color) => {
  const colors = bags.get(color);
  if (!colors || colors.length == 0)
    return false;
  if (colors.includes('shiny gold'))
    return true;
  return colors.some(c => canHaveGolden(c));
}

const countGolden = (bag) => {
  console.log('bag', bag)
  const colors = bags.get(bag[1]);
  if (!colors || colors.length == 0)
    return bag[0]
  return bag[0] + bag[0] * colors.map(c => {
      const x = countGolden(c);
      console.log('count', c, x);
      return x}
    ).reduce((a,b) => a + b, 0)
}

const res = lines.map(l => {
  const r = l.split(' ');
  const color = r[0] + ' ' + r[1];
  const bag1 = r[5] + ' ' + r[6];
  const bag2 = (r.length > 8) ? r[9] + ' ' + r[10] : '';

  const b = [];

  r.slice(4).forEach((w, i) => {
    if (i % 4 == 1 && w != 'other') {
      b.push([parseInt(r[3+i]), w + ' ' + r[5+i]]);
    }
  })

  bags.set(color, b);

  return color;
})//.filter(c => canHaveGolden(c));

console.log(bags)
console.log(countGolden([1, 'shiny gold']))