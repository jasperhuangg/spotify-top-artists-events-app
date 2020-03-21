package PersonalizedTop;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import Top50.Top50Artist;

public class UserTopArtists {
	private static final String clientId = "81b1695da7764d3a86a1bab842ca436f";
	private static final String clientSecret = "d2f5b45becf949ad97ee2440a92ff80d";
	private SpotifyApi spotifyApi;

	private String OAuthCode;
	private String accessToken;

	/*
	 * TODO: RUN ./ngrok http 8080 to get new redirect_uri every time you run the
	 * ngrok server, don't forget to change on Spotify Dashboard.
	 */
	private String redirectURI = "https://11f22450.ngrok.io";

	private List<Top50Artist> top50Artists;

	public UserTopArtists() {
//		top50Artists = CreateUsersTopArtists();
	}

	public void SetAccessToken() {
		System.out.println("auth code: " + OAuthCode);
		OAuthCode = OAuthCode.replace("#_=_", "");
//		String encoded = Base64.encodeBase64URLSafeString((clientId + ":" + clientSecret).getBytes());
//		String encoded = /* Base64.encodeBase64URLSafeString( */(clientId + ":" + clientSecret);
//		String command = "curl -H \"Authorization: Basic " + encoded + "\" -d grant_type=authorization_code -d code="
//				+ OAuthCode
//				+ " -d redirect_uri=http://3f6f90a4.ngrok.io/homepage.html https://accounts.spotify.com/api/token";

//		String scopes = "user-top-read";

//		String command = "curl -d client_id=" + clientId + " -d client_secret=" + clientSecret
//				+ " -d grant_type=authorization_code -d code=" + OAuthCode
//				+ " -d redirect_uri=http://3f6f90a4.ngrok.io/homepage.html -d scope=" + scopes
//				+ " https://accounts.spotify.com/api/token";

		String command = "curl -d client_id=" + clientId + " -d client_secret=" + clientSecret
				+ " -d grant_type=authorization_code -d code=" + OAuthCode + " -d redirect_uri=" + redirectURI
				+ "/homepage.html https://accounts.spotify.com/api/token";

		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		InputStream inputStream = process.getInputStream();

		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonResponse = writer.toString();

		System.out.println(jsonResponse);

		JsonObject response = new JsonParser().parse(jsonResponse).getAsJsonObject();

		accessToken = response.get("access_token").getAsString();

		System.out.println("scopes: " + response.get("scope").getAsString());

		spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();

		process.destroy();
	}

	public List<Top50Artist> GetUsersTopArtists() {
		final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists().limit(50).build();

		List<Top50Artist> t = new ArrayList<Top50Artist>();

		Paging<Artist> artistPaging = null;
		try {
			artistPaging = getUsersTopArtistsRequest.execute();
			System.out.println("after top artists request");

			System.out.println("Total: " + artistPaging.getTotal());
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.toString());
		}

		System.out.println("YOUR TOP 50 MOST PLAYED SPOTIFY ARTISTS: ");
		for (int i = 0; i < artistPaging.getTotal(); i++) {
			System.out.println("ARTIST: " + artistPaging.getItems()[i].getName());
			Artist a = artistPaging.getItems()[i];
			Top50Artist artist = new Top50Artist();
			artist.setName(a.getName());
			artist.setImageURL(a.getImages()[0].getUrl());
			artist.setSpotifyURL(a.getHref());
			t.add(artist);
		}
		return t;

	}

	public List<Top50Artist> getTop50Artists() {
		return top50Artists;
	}

	public void setTop50Artists(List<Top50Artist> top50Artists) {
		this.top50Artists = top50Artists;
	}

	public String getOAuthCode() {
		return OAuthCode;
	}

	public void setOAuthCode(String oAuthCode) {
		OAuthCode = oAuthCode;
	}
}
