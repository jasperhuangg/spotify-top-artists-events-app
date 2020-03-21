package UpcomingEvent;

public class UpcomingEvent {
	private String name;
	private String status;
	private String city;
	private String date;
	private Double venueLat;
	private Double venueLong;
	private String venueName;
	private String songKickURL;
	private String time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getVenueLat() {
		return venueLat;
	}

	public void setVenueLat(Double venueLat) {
		this.venueLat = venueLat;
	}

	public Double getVenueLong() {
		return venueLong;
	}

	public void setVenueLong(Double venueLong) {
		this.venueLong = venueLong;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getSongKickURL() {
		return songKickURL;
	}

	public void setSongKickURL(String songKickURL) {
		this.songKickURL = songKickURL;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void Print() {
		System.out.println(name);
		System.out.println(status);
		System.out.println(city);
		System.out.println(venueName);
		System.out.println(venueLat);
		System.out.println(venueLong);
		System.out.println(songKickURL);
		System.out.println(date);
		System.out.println(time);

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
