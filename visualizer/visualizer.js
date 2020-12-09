const fs = require('fs');
const file = fs.readFileSync('leaderboard.json');
const leaderboard = JSON.parse(file);

const prepend = `
<html style="width: 100%;">
  <head>
    <title>Leaderboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/css?family=Source+Code+Pro:300&amp;subset=latin,latin-ext" rel="stylesheet" type="text/css">
    <style>
body {
  background: #0f0f23;
  color: #cccccc;
  font-family: "Source Code Pro", monospace;
  font-size: 12pt;
}

a {
  color: #ccc;
  text-decoration: none;
}

a div {
  height: 56px;
  margin-top: 32px;
}

header {
  margin-bottom: 2em;
}

h1 {
  display: inline-block;
  margin: 0;
  padding-right: 1em;
  font-size: 1em;
}

.glowing {
  color: #00cc00;
  text-shadow: 0 0 2px #00cc00, 0 0 5px #00cc00;
}

.green {
  color: #00cc00;
}

.timelines {
  width: 2074px;
}

.lower {
  height: 300px;
  margin-top: -8px;
}

.upper {
  height: 8px;
  margin-top: 256px;
}

.lower div {
  position: absolute;
  width: 300px;
  text-align: right;
  transform-origin: center right;
  transform: rotate(-90deg);
}

.upper div {
  position: absolute;
  width: 300px;
  transform-origin: 0 center;
  transform: rotate(-90deg);
}

.silver {
  color: #9999cc;
}

.gold {
  color: #ffff66;
}
    </style>
  </head>
  <body>
    <header>
      <div><h1 class="glowing">Advent of Code</h1>Emil Wihlander's leaderboard</div>
      <div><h1 class="green">&nbsp;&nbsp;&nbsp;year = <span class="glowing">2020</span></h1><span class="green">380632</span></div>
    </header>
    <div class="timelines">
`

const append = `
    </div>
  </body>
</html>
`

const timeline = '-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|-----.-----|'

const generateTimeline = (day) => {
  const pad = (num) => ((num > 9) ? '' : '0') + num;
  const entry = (name, first, time) => ({first: first, name: name, ts: new Date(parseInt(time.get_star_ts) * 1000)});
  const htmlElement = (upper, lower) => `<a href="https://adventofcode.com/2020/day/${day}"><h1>[Day <span class="green">${day}</span>]</h1></a><div class="upper">${upper.join('')}</div><div>${timeline}</div><div class="lower">${lower.join('')}</div>`;

  const upper = [];
  const lower = [];

  Object.values(leaderboard.members)
    .filter(m => m.completion_day_level[day])
    .flatMap(({name, completion_day_level}) => {
      const first = completion_day_level[day][1];
      const second = completion_day_level[day][2];

      const entries = [ entry(name, true, first) ];
      if (second) entries.push(entry(name, false, second));
      return entries;
    })
    .sort((a, b) => a.ts - b.ts)
    .forEach((entry, i) => {
      const color = (entry.first) ? 'silver' : 'gold';
      const offset = Math.floor((entry.ts - (new Date('2020-12-' + pad(day) + 'T05:00:00Z')))/(18*60*60*1000)*2074)
      if (offset > 2285) return
      const dispTime = pad(entry.ts.getUTCHours()+1) + ':' + pad(entry.ts.getMinutes()) + ':' + pad(entry.ts.getSeconds());
      if (i % 2 == 0) upper.push(`<div class="${color}" style="left: ${offset+5}">-${dispTime} ${entry.name}</div>`);
      else lower.push(`<div class="${color}" style="left: ${offset-300+5}">${entry.name} ${dispTime}-</div>`);
    })

  return htmlElement(upper, lower);
}

const getLatestDay = () => {
  return Object.entries(leaderboard.members)
    .flatMap(([_, m]) => Object.keys(m.completion_day_level))
    .reduce((a,b) => Math.max(a,b), 0)
}

const htmlTimelines = [...Array(getLatestDay()).keys()].reverse().map(n => generateTimeline(n+1)).join('')

fs.writeFileSync("index.html", prepend + htmlTimelines + append)