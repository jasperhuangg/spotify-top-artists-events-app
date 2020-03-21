package Top50;

public class Top50Track implements Comparable<Top50Track> {
	private String name;
	private String previewURL;
	private String spotifyURL;
	private String imageURL;
	private Integer popularity;
	private String mainArtistName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpotifyURL() {
		return spotifyURL;
	}

	public void setSpotifyURL(String spotifyURL) {
		this.spotifyURL = spotifyURL;
	}

	public String getPreviewURL() {
		return previewURL;
	}

	public void setPreviewURL(String previewURL) {
		this.previewURL = previewURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	@Override
	public int compareTo(Top50Track a) {
		return popularity - a.getPopularity();
	}

	public String getMainArtistName() {
		return mainArtistName;
	}

	public void setMainArtistName(String mainArtistName) {
		this.mainArtistName = mainArtistName;
	}

}
