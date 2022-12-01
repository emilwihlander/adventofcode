
var https = require('https');
var fs = require('fs');
var options = {
    host: 'adventofcode.com',
    path: '/2022/leaderboard/private/view/380632.json',
    method: 'GET',
    headers: {
        "Cookie": `session=${process.env.ADVENT_OF_CODE_TOKEN}`,
        "Set-Cookie": `session=${process.env.ADVENT_OF_CODE_TOKEN}`
    }
};

function downloadJson(urlToPrint){
    var file = fs.createWriteStream("leaderboard.json");
    var request = https.get(urlToPrint, function(response) {
      response.on("finish",function(){
        console.log( fs.readFileSync("leaderboard.json",{encoding:"utf8"}));
      }).pipe(file);
    });
}

downloadJson(options)

