let currentURL = window.location.href;

let codeStartIndex = currentURL.search("code") + 5;

let authCode = currentURL.substring(codeStartIndex, currentURL.length);

// console.log(authCode);

let arr;
// $.get("Top50Servlet").done(function(responseJson)
$.get("Top50Servlet", {
  oauthcode: authCode
}).done(function(responseJson) {
  localStorage["top50artists"] = JSON.stringify(responseJson);
  // console.log("done");
  // console.log(responseJson);
  $(".lds-ring").remove();
  // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
  $.each(responseJson, function(index, item) {
    // Iterate over the JSON array.
    // for (let i = 0; i < item.length; i++) {
    var artistItem = $(
      '<div class="col-lg-2 col-md-3 col-sm-4 m-2 mt-4 mb-4 artist-item"><div>'
    );
    var artistName = $(
      '<div class="text-center artist-name font-italic">' + item.name + "</div>"
    );

    var artistImage = $('<img class="artist-img" src="' + item.imageURL + '">');

    var artistSpotifyURL = $(
      "<div style='display:none;'>" + item.spotifyURL + "</div>"
    );

    var artistRanking = $(
      "<div style='display:none;'>" + (index + 1) + "</div>"
    );

    artistItem.append(artistName);
    artistItem.append(artistImage);
    artistItem.append(artistSpotifyURL);
    artistItem.append(artistRanking);
    $("#artists-container").append(artistItem);
  });
});

// handle overlay clicks
$(document).on("click", ".artist-item", function() {
  $(".artist-overlay").css("display", "none"); // hide the open overlay
  let artistName = $(this).children()[1].innerHTML;
  let artistNameNoSpace = $(this)
    .children()[1]
    .innerHTML.replace(/ /g, "-");
  let artistImgURL = $(this).children()[2].src;
  let artistSpotifyUrl = $(this).children()[3].innerHTML;
  let artistRanking = $(this).children()[4].innerHTML;

  let overlays = $(".artist-overlay");
  // console.log(overlays.length + " overlays exist");
  var overlayEvents;
  let overlayThatAlreadyExists = null;

  // check if an overlay already exists with the artist's name
  // if one already exists, just set it's display to inline-block to display it
  // if one doesn't exist, create it

  for (let i = 0; i < overlays.length; i++) {
    // console.log("Checking overlays");
    // console.log(overlays[i].children[0].children[0].innerHTML);
    if (
      overlays[i].children[0].children[0].children[0].innerHTML === artistName
    ) {
      // if we are clicking on an overlay that already exists
      overlayThatAlreadyExists = overlays[i];
      // console.log("overlay you just clicked already exists.");
      overlayThatAlreadyExists.style.display = "inline-block";
    }
  }

  if (overlayThatAlreadyExists === null) {
    // create overlay element
    var artistOverlay = $(
      '<div class="artist-overlay" id="artist-overlay-' +
        artistNameNoSpace +
        '"></div>'
    );
    var overlayHeader = $(
      '<div class="row align-items-center overlay-header"></div>'
    );
    var overlayName = $(
      '<div class="col-5 h1 overlay-name text-center text-white">' +
        artistRanking +
        ". " +
        "<i>" +
        artistName +
        "</i>" +
        "</div>"
    );

    overlayEvents = $(
      '<div id="overlay-events-' +
        artistNameNoSpace +
        '"class="overlay-events container-fluid text-center"></div>'
    );
    var overlayLoader = $(
      '<div class="events-loader lds-ring"><div></div><div></div><div></div><div></div></div>'
    );
    overlayEvents.append(overlayLoader);
    var overlaySpotifyRedirect = $(
      '<button type="button" onclick="spotifyRedirect(\'' +
        artistSpotifyUrl +
        '\');" style="left:13% !important;"class="events-redirect col-2 btn btn-danger">' +
        "See on Spotify" +
        "</button>"
    );

    overlaySpotifyRedirect.css("z-index", 500);
    var overlayImg = $(
      '<div class="col-7 overlay-img"><img  src="' +
        artistImgURL +
        '"><div class="x-out"><i class="far fa-times-circle"></i></div></div>'
    );

    overlayHeader.append(overlayName);
    overlayHeader.append(overlayImg);
    overlayHeader.append(overlaySpotifyRedirect);
    artistOverlay.append(overlayHeader);
    artistOverlay.append(overlayEvents);

    $("#page-container").append(artistOverlay);

    // console.log("doing get for: " + artistName);
    // make request to ArtistEventServlet, get artist's events
    $.get("ArtistEventServlet", { requestedartist: artistName }).done(function(
      responseJson
    ) {
      // console.log(
      //   "returned " + responseJson.length + " events for: " + artistName
      // );

      // $(document).ready(function() {
      if (responseJson.length === 0) {
        // $("#overlay-events-" + artistNameNoSpace).html(
        //   "This artist has no upcoming events."
        // );
        // $("#overlay-events-" + artistNameNoSpace).css("color", "white");
        overlayEvents.html("This artist has no upcoming events.");
        overlayEvents.css("color", "white");
      } else {
        // console.log("showing events for " + artistName);
        $(document)
          .find("#overlay-events-" + artistNameNoSpace)
          .html(
            "This area will show " +
              artistName +
              "'s " +
              responseJson.length +
              " events."
          )
          .end();
        overlayEvents.css("color", "white");
        // console.log("id is: " + overlayEvents.attr("id"));
        // console.log(
        //   "overlayEvents innerHTML says: " +
        //     $(document)
        //       .find("#overlay-events-" + artistNameNoSpace)
        //       .html()
        // );
      }
      // });
    });
  }
});

$("#search-bar").on("keyup", function() {
  artists = document.getElementsByClassName("artist-item");
  filter = $(this)
    .val()
    .toUpperCase();
  // console.log(filter);
  for (let i = 0; i < artists.length; i++) {
    if (
      artists[i]
        .getElementsByClassName("artist-name")[0]
        .innerHTML.toUpperCase()
        .indexOf(filter) > -1
    ) {
      artists[i].style.display = "";
    } else {
      artists[i].style.display = "none";
    }
  }
});

$(".search").on("click", function() {
  $("#search-bar").val("");
  artists = document.getElementsByClassName("artist-item");
  for (let i = 0; i < artists.length; i++) {
    artists[i].style.display = "";
  }
});

$(document).on("keydown", function() {
  if (event.key === "Escape") $(".artist-overlay").css("display", "none");
});

function spotifyRedirect(url) {
  console.log(url);
  window.open(url);
}

// $(document).on("click", ".events-redirect", function(e) {
//   let artistName = $(this)
//     .parent()
//     .children()[0].innerHTML;
//   // localStorage["requestedArtist"] = artistName;

//   // window.location.replace("/artist.html");
//   e.preventDefault();
//   // $.ajax({
//   //   url: "/ArtistEventServlet",
//   //   data: {
//   //     requestedartist: artistName
//   //   },
//   //   type: "GET",
//   //   success: function(data) {
//   //     window.location.replace("/artist.html");
//   //   }
//   // });
// });

$(document).on("click", ".x-out", function() {
  $(".artist-overlay").css("display", "none");
});
