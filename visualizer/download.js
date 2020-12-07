
var https = require('https');
var fs = require('fs');
var options = {
    host: 'adventofcode.com',
    path: '/2020/leaderboard/private/view/380632.json',
    method: 'GET',
    headers: {
        "Cookie": "session=53616c7465645f5f0aa26143183875c778d4c25921bb3b3b74dcf80ccf0361558099be833f649fd2b145acab2e5961b9", // POINTER its unfortunately useless
        "Set-Cookie": "session=53616c7465645f5f0aa26143183875c778d4c25921bb3b3b74dcf80ccf0361558099be833f649fd2b145acab2e5961b9" // POINTER its unfortunately useless
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

