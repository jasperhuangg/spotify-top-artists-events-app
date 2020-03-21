let clientId = "81b1695da7764d3a86a1bab842ca436f";
let clientSecret = "d2f5b45becf949ad97ee2440a92ff80d";

/* TODO: RUN ./ngrok http 8080 to get new redirect_uri every time you run the ngrok server */
let redirect_uri = "http://82d5fe23.ngrok.io/homepage.html";

let authorization_uri =
  "https://accounts.spotify.com/authorize?client_id=" +
  clientId +
  "&scope=user-top-read&response_type=code&redirect_uri=" +
  redirect_uri;

window.open(authorization_uri);
window.close();
