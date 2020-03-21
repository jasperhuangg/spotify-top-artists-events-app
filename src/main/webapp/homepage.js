let currentURL = window.location.href;

let codeStartIndex = currentURL.search("code") + 5;

let authCode = currentURL.substring(codeStartIndex, currentURL.length);

console.log(authCode);

let arr;
setTimeout(function() {
  // $.get("Top50Servlet").done(function(responseJson)
  $.get("Top50Servlet", {
    oauthcode: authCode
  }).done(function(responseJson) {
    $(".lds-ring").remove();
    // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
    $.each(responseJson, function(index, item) {
      // Iterate over the JSON array.
      for (let i = 0; i < item.length; i++) {
        var artistItem = $(
          '<div class="col-lg-2 col-md-3 col-sm-4 m-2 mt-4 mb-4 artist-item"><div>'
        );
        var artistName = $(
          '<div class="text-center artist-name font-italic">' +
            item[i].name +
            "</div>"
        );
        var artistImage = $(
          '<img class="artist-img" src="' + item[i].imageURL + '">'
        );

        artistItem.append(artistName);
        artistItem.append(artistImage);
        $("#artists-container").append(artistItem);
      }
      // handle overlay clicks
      $(".artist-item").on("click", function() {
        $(".artist-overlay").remove();
        // console.log($(this).children());
        let artistName = $(this).children()[1].innerHTML;
        let artistImgURL = $(this).children()[2].src;
        let artistTracklist = [];

        for (let i = 0; i < item.length; i++) {
          if (item[i].name === artistName) {
            artistTracklist = item[i].top50Tracks;
            break;
          }
        }
        // create overlay element
        var artistOverlay = $('<div class="artist-overlay"></div>');
        var overlayHeader = $(
          '<div class="row align-items-center overlay-header"></div>'
        );
        var overlayName = $(
          '<div class="col-4 h1 font-italic overlay-name text-center text-white">' +
            artistName +
            "</div>"
        );
        var overlayEventsRedirect = $(
          '<button type="button" class="events-redirect col-2 btn btn-danger">' +
            "Artist Page" +
            "</button>"
        );
        overlayEventsRedirect.css("z-index", 500);
        var overlayImg = $(
          '<div class="col-8 overlay-img"><img  src="' +
            artistImgURL +
            '"><div class="x-out"><i class="far fa-times-circle"></i></div></div>'
        );
        overlayHeader.append(overlayName);
        overlayHeader.append(overlayImg);
        overlayHeader.append(overlayEventsRedirect);

        var overlayTracklist = $('<ul class="list-group"></ul>');
        for (let i = 0; i < artistTracklist.length; i++) {
          var track = $(
            '<li class="list-group-item"><i>' +
              artistTracklist[i].name +
              "</i> - " +
              artistTracklist[i].mainArtistName +
              "</li>"
          );
          overlayTracklist.append(track);
        }
        artistOverlay.append(overlayHeader);
        artistOverlay.append(overlayTracklist);
        $(".container-fluid").append(artistOverlay);
      });
    });
  });
}, 1000);

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
  if (event.key === "Escape") $(".artist-overlay").remove();
});

$(document).on("click", ".events-redirect", function(e) {
  console.log("click");

  let artistName = $(this)
    .parent()
    .children()[0].innerHTML;
  localStorage["requestedArtist"] = artistName;

  window.location.replace("/artist.html");
  // e.preventDefault();
  // $.ajax({
  //   url: "/ArtistEventServlet",
  //   data: {
  //     requestedartist: artistName
  //   },
  //   type: "GET",
  //   success: function(data) {
  //     window.location.replace("/artist.html");
  //   }
  // });
});

$(document).on("click", ".x-out", function() {
  $(".artist-overlay").remove();
});
