package Top50;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import UpcomingEvent.UpcomingEvent;

public class Top50Artist implements Comparable<Top50Artist> {
	private String name;
	private List<Top50Track> top50Tracks = new ArrayList<Top50Track>();
	private Integer top50Score; // sum(track popularity)
	private String imageURL;
	private String spotifyURL;
	private String songKickAPIKey = "io09K9l3ebJxmxe2";
	private List<UpcomingEvent> upcomingEvents;

	public String getName() {
		return name;
	}

	public List<UpcomingEvent> getUpcomingEvents() {
		return upcomingEvents;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void incrementScore(Integer score) {
		top50Score += score;
	}

	public List<Top50Track> getTop50Tracks() {
		return top50Tracks;
	}

	public void addTop50Track(Top50Track t) {
		top50Tracks.add(t);
	}

	public void setTop50Tracks(List<Top50Track> top50Tracks) {
		this.top50Tracks = top50Tracks;
	}

	public Integer getTop50Score() {
		return top50Score;
	}

	public void setTop50Score(Integer top50Score) {
		this.top50Score = top50Score;
	}

	public void sortTop50Tracks() {
		Collections.sort(top50Tracks, Collections.reverseOrder());
	}

	private String GetMaxDate(Integer monthsAhead) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateobj = new Date();
		String currDateStr = df.format(dateobj);

		Integer month = (Integer.parseInt(currDateStr.substring(5, 7)) + monthsAhead);
		Integer year = Integer.parseInt(currDateStr.substring(0, 4));
		Integer day = 30;

		if (month > 12) {
			month -= 12;
			year += 1;
		}

		if (month == 2) {
			day = 28;
		}

		String monthStr = month < 10 ? ("0" + month.toString()) : (month.toString());

		String maxDate = year + "-" + monthStr + "-" + day;

		return maxDate;
	}

	public void CreateEvents() {
		System.out.println("Creating events for");
		System.out.println("ARTIST: " + name);
		upcomingEvents = new ArrayList<UpcomingEvent>();
		String url = "https://api.songkick.com/api/3.0/search/artists.json?apikey=" + songKickAPIKey + "&query="
				+ name.replace(" ", "+");
		System.out.println(url);
		String jsonResponse = getJSON(url);
		System.out.println(jsonResponse);
		JsonObject response = new JsonParser().parse(jsonResponse).getAsJsonObject();
		if (response.get("resultsPage").getAsJsonObject().get("totalEntries").getAsInt() > 0) {
			Integer artistID = Integer.parseInt(response.get("resultsPage").getAsJsonObject().get("results")
					.getAsJsonObject().get("artist").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString());
			url = "https://api.songkick.com/api/3.0/artists/" + artistID + "/calendar.json?apikey=" + songKickAPIKey
					+ "&max_date=" + GetMaxDate(6);
			if (name.equals("Arizona Zervas"))
				System.out.println(url);
			jsonResponse = getJSON(url);
			response = new JsonParser().parse(jsonResponse).getAsJsonObject();
			System.out.println(response);
			if (!response.get("resultsPage").getAsJsonObject().get("totalEntries").getAsString().equals("0")) {
				JsonArray events = response.get("resultsPage").getAsJsonObject().get("results").getAsJsonObject()
						.get("event").getAsJsonArray();
				for (int i = 0; i < events.size(); i++) {
//				System.out.println("NEW EVENT: ");
					// TODO: HANDLE NULL VALUES, add event image

					UpcomingEvent e = new UpcomingEvent();

					int openParenIndex = events.get(i).getAsJsonObject().get("displayName").getAsString().indexOf('(');
					int closedParenIndex = events.get(i).getAsJsonObject().get("displayName").getAsString()
							.indexOf(')');
					String nameParsed = events.get(i).getAsJsonObject().get("displayName").getAsString();
					if (openParenIndex != -1 && closedParenIndex != -1) {
						String parenSubstr = events.get(i).getAsJsonObject().get("displayName").getAsString()
								.substring(openParenIndex, closedParenIndex + 1);
						nameParsed = events.get(i).getAsJsonObject().get("displayName").getAsString()
								.replace(parenSubstr, "");
					}
					e.setName(nameParsed);
//				System.out.println(events.get(i).getAsJsonObject().get("displayName").getAsString());
					e.setSongKickURL(events.get(i).getAsJsonObject().get("uri").getAsString());
//				System.out.println(events.get(i).getAsJsonObject().get("uri").getAsString());
					e.setCity(events.get(i).getAsJsonObject().get("location").getAsJsonObject().get("city")
							.getAsString());
//				System.out.println(
//						events.get(i).getAsJsonObject().get("location").getAsJsonObject().get("city").getAsString());
					e.setStatus(events.get(i).getAsJsonObject().get("status").getAsString());
//				System.out.println(events.get(i).getAsJsonObject().get("status").getAsString());

					if (!events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("lat").toString()
							.equals("null")) {
						e.setVenueLat(events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("lat")
								.getAsDouble());
//					System.out.println(
//							events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("lat").getAsDouble());
					} else {
						e.setVenueLat(6969696969.0); // trash value to check later
//					System.out.println("no lat available");
					}
					if (!events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("lng").toString()
							.equals("null")) {
						e.setVenueLong(events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("lng")
								.getAsDouble());
//					System.out.println(
//							events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("lng").getAsDouble());
					} else {
//					System.out.println("no long available");
						e.setVenueLong(6969696969.0); // trash value to check later
					}

					if (!events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("displayName").toString()
							.equals("null")) {
//					System.out.println(events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("displayName")
//							.getAsString());
						e.setVenueName(events.get(i).getAsJsonObject().get("venue").getAsJsonObject().get("displayName")
								.getAsString());
					} else {
//					System.out.println("no venue available");
						e.setVenueName("No venue name available.");
					}

					e.setDate(events.get(i).getAsJsonObject().get("start").getAsJsonObject().get("date").getAsString());
//				System.out.println(
//						events.get(i).getAsJsonObject().get("start").getAsJsonObject().get("date").getAsString());

					if (!events.get(i).getAsJsonObject().get("start").getAsJsonObject().get("time").toString()
							.equals("null")) {
						e.setTime(events.get(i).getAsJsonObject().get("start").getAsJsonObject().get("time")
								.getAsString());
//					System.out.println("time: "
//							+ events.get(i).getAsJsonObject().get("start").getAsJsonObject().get("time").getAsString());

					} else {
						e.setTime("No time available.");
//					System.out.println("No time available.");
					}

					upcomingEvents.add(e);
//				System.out.println();
				}
			} /*
				 * else System.out.println("No events for artist");
				 */
		}
	}

	private String getJSON(String url) {
		String response = "";
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);
			if (responseCode != 200) {
				response = "error";
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine = "";
				while ((inputLine = in.readLine()) != null) {
					response += (inputLine);
				}
				in.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return response;
	}

	@Override
	public int compareTo(Top50Artist a) {
		if (top50Score == null || a.getTop50Score() == null) {
			return 0;
		}

		if (top50Score != a.getTop50Score())
			return top50Score.compareTo(a.getTop50Score());
		else if (top50Tracks.size() != a.getTop50Tracks().size())
			return top50Tracks.size() - a.getTop50Tracks().size();
		else
			return -name.compareTo(a.getName());
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getSpotifyURL() {
		return spotifyURL;
	}

	public void setSpotifyURL(String spotifyURL) {
		this.spotifyURL = spotifyURL;
	}

}
