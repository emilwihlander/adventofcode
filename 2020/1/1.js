const fs = require('fs');

const file = fs.readFileSync('./input.txt').toString().split("\n");

console.log("Part 1");
for (i = 0; i < file.length - 1; i++) {
  for (j = i + 1; j < file.length; j++) {
    const x = parseInt(file[i]);
    const y = parseInt(file[j]);
    if (x + y == 2020) {
      console.log(x * y);
    }
  }
}

for (i = 0; i < file.length - 2; i++) {
  for (j = i + 1; j < file.length - 1; j++) {
    for (k = j + 1; k < file.length; k++) {
      const x = parseInt(file[i]);
      const y = parseInt(file[j]);
      const z = parseInt(file[k]);
      if (x + y + z == 2020) {
        console.log(x * y * z);
      }
    }
  }
}