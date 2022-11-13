const baseUrl = window.location.href
const matchesUrl = "http://localhost:8080/soccer/api/match"

const token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMiIsImlzcyI6InRlc3Qtand0LWlzc3VlciIsImF1ZCI6Imp3dC1hdWRpZW5jZSIsImdyb3VwcyI6WyJVU0VSIl19.ifOEvPGdRvoaU0cdgGYmZCWvWTwOC-em5qZNCwtd13V2rv4b8kI8x22LZdWCf6HB6BuhkWTkUrWhDi0v20jQvUf7c53omWIaV2Xu2dWBvjF_W8I4dauWS4tC9UD5nviP2NzS0D_VH53D4eEtdk_f3WreT0Esy-FuHgpBQ3WVJRURNC30CoaAfdOpQKTivwGqpGYil_lp9-6MaNCqged3BHAOo1kaTS04X2mPNJIuR-YG4YXdB4xcA4gIP3DGYgw4l_vPWMHSg0tfPD-eR7EguTug5JKlPOCDVHxYM_zpFnWA1AvePThDoBf7q2MElYN81pXWdOfLTCFkXDOXhTyjQA"

function appendMatches(data) {

    data.forEach(match => {
    console.log(match)
        let matchUrl = baseUrl + "match.html?id=" + match["id"]
        let html = '<a href = "' + matchUrl  + '" class="match"> ' + match["teamHomeName"] + " vs. " + match["teamAwayName"] + '</a><br>'
        html += ''
        document.getElementById("match_list").innerHTML += html;
    });

}

fetch(matchesUrl, {
     headers: new Headers({
            'Authorization': 'Bearer ' + token,
        }),
})
    .then(response => response.json())
    .then(data => {
        console.log(data)
        appendMatches(data)
    })
    .catch(error => {
        console.log(error)
    });

