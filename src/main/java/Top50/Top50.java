package Top50;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;

public class Top50 {

	/* Spotify API stuff */
	private static final String clientId = "81b1695da7764d3a86a1bab842ca436f";
	private static final String clientSecret = "d2f5b45becf949ad97ee2440a92ff80d";
	private static final SpotifyApi spotifyApi = new SpotifyApi.Builder().setClientId(clientId)
			.setClientSecret(clientSecret).build();
	private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

	private List<Top50Artist> top50Artists;

	public Top50() {

		top50Artists = CreateTop50Artists();
	}

	private List<Top50Artist> CreateTop50Artists() {

		List<Top50Artist> t = new ArrayList<Top50Artist>();

		ClientCredentials clientCredentials = null;
		try {
			clientCredentials = clientCredentialsRequest.execute();
		} catch (SpotifyWebApiException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		spotifyApi.setAccessToken(clientCredentials.getAccessToken());
		// For all requests an access token is needed
		GetPlaylistRequest gpr = spotifyApi.getPlaylist("37i9dQZEVXbLRQDuF5jeBp").market(CountryCode.US).build();
		Playlist top50 = null;
		try {
			// Execute the request synchronous
			top50 = gpr.execute();
		} catch (Exception e) {
			System.out.println("Something went wrong!\n" + e.getMessage());
		}
		// for each track in the response
		for (int i = 0; i < top50.getTracks().getItems().length; i++) {

			Top50Track track = new Top50Track();
			track.setName(top50.getTracks().getItems()[i].getTrack().getName());
			track.setPreviewURL(top50.getTracks().getItems()[i].getTrack().getPreviewUrl());
			track.setImageURL(top50.getTracks().getItems()[i].getTrack().getAlbum().getImages()[0].getUrl());
			track.setSpotifyURL(top50.getTracks().getItems()[i].getTrack().getExternalUrls().get("spotify"));
			track.setPopularity(top50.getTracks().getItems()[i].getTrack().getPopularity());
			Integer score = top50.getTracks().getItems()[i].getTrack().getPopularity();
			ArtistSimplified[] artistNameList = top50.getTracks().getItems()[i].getTrack().getArtists();
			track.setMainArtistName(artistNameList[0].getName());
//			System.out.println(artistNameList[0].getName());

			// for each artist in track's artistNameList
			for (int j = 0; j < artistNameList.length; j++) {
				String artistName = artistNameList[j].getName();
				if (Top50ListContainsArtist(t, artistName)) { // if artist in list
					Top50ListAddTrack(t, track, artistName);
					Top50ListIncrementScore(t, score, artistName);
				} else {
					String artistID = artistNameList[j].getId();
					GetArtistRequest gar = spotifyApi.getArtist(artistID).build();
					Artist artist = null;
					try {
						artist = gar.execute();
					} catch (SpotifyWebApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String artistImageURL = artist.getImages()[0].getUrl();

					// create a new Top50Artist with track, add to list
					Top50Artist top50Artist = new Top50Artist();

					top50Artist.setTop50Score(score);
					top50Artist.setName(artistName);
					top50Artist.addTop50Track(track);
					top50Artist.setImageURL(artistImageURL);

					t.add(top50Artist);
				}
			}

		}

		// we only want to create events for a queried artist (but this shit works tho)
//		for (int i = 0; i < t.size(); i++) {
//			if (t.get(i).getTop50Tracks().size() > 1)
//				t.get(i).sortTop50Tracks();
//			t.get(i).CreateEvents();
//		}

		Collections.sort(t, Collections.reverseOrder());
		return t;

	}

	// Create events for a particular artist upon accessing that artist's event page
	// because creating events for all artists at the start takes way too long.
	public void CreateEventsForArtist(String name) {
		for (int i = 0; i < top50Artists.size(); i++) {
			if (top50Artists.get(i).getName().equals(name)) {
				top50Artists.get(i).CreateEvents();
				return;
			}
		}
	}

	private boolean Top50ListContainsArtist(List<Top50Artist> t, String artistName) {
		if (t.size() == 0)
			return false;
		for (int i = 0; i < t.size(); i++) {
			if (t.get(i).getName().equals(artistName))
				return true;
		}
		return false;
	}

	private void Top50ListAddTrack(List<Top50Artist> t, Top50Track track, String artistName) {
		for (int i = 0; i < t.size(); i++) {
			Top50Artist a = t.get(i);
			if (a.getName().equals(artistName)) {
				a.addTop50Track(track);
				return;
			}
		}
	}

	private void Top50ListIncrementScore(List<Top50Artist> t, Integer score, String artistName) {
		for (int i = 0; i < t.size(); i++) {
			Top50Artist a = t.get(i);
			if (a.getName().equals(artistName)) {
				a.incrementScore(score);
				return;
			}
		}
	}

	private void PrintTop50Artists() {
		for (int i = 0; i < top50Artists.size(); i++) {
			System.out.println("Artist: " + top50Artists.get(i).getName());
			System.out.println("----Popularity Score: " + top50Artists.get(i).getTop50Score());
			System.out.println("----Tracks: ");
			for (int j = 0; j < top50Artists.get(i).getTop50Tracks().size(); j++) {
				System.out.println("--------" + top50Artists.get(i).getTop50Tracks().get(j).getName()
						+ " - popularity: " + top50Artists.get(i).getTop50Tracks().get(j).getPopularity());
			}
		}
	}

	public List<Top50Artist> getTop50Artists() {
		return top50Artists;
	}

}
