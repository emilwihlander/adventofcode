const fs = require('fs');

const lines = fs.readFileSync('input.txt').toString().split('\n\n').map(l => l.split('\n'));

//const res = lines.map(group => group.reduce((a,b) => a + b, '').split('').filter((v, i, a) => a.indexOf(v) === i).length).reduce((a,b) => a + b)
const res = lines.map(g => g.filter(l => l.length != 0)).map(group => 'abcdefghijklmnopqrstuvwxyz'.split('').filter(c => group.every(s => s.includes(c))).length).reduce((a,b) => a + b)


console.log(res)