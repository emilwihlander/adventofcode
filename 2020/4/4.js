const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n\n').map(l => l.replace('\n', ' '));

const res = lines.map(l => {
  const obj = {};
  const matches = l.match(/\w+:[^\s]+/g);
  matches.forEach(m => {
    const [key, value] = m.split(':');
    obj[key] = value
  })
  return obj;
})
  .filter(obj => {
    return /^(19[2-9]\d$|200[0-2])$/.test(obj.byr)
    && /^(201\d|2020)$/.test(obj.iyr)
    && /^(202\d|2030)$/.test(obj.eyr)
    && /^(1([5-8]\d|9[0-3])cm|(59|6\d|7[0-6])in)$/.test(obj.hgt)
    && /^#[a-f\d]{6}$/.test(obj.hcl)
    && /^(amb|blu|brn|gry|grn|hzl|oth)$/.test(obj.ecl)
    && /^\d{9}$/.test(obj.pid)
  })
console.log(res.length)