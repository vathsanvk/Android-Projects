package com.example.nprclient;

import java.io.Serializable;

public class Story implements Serializable{
	private String title, pubDate, minTeaser, thumbnail, link, repName, dateAired,
			 teaserText, streamUrl;
	
	private int lenAudio;
	private boolean isFav;
	
	public String toString(){
		return title + " " + isFav;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Story other = (Story) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public boolean isFav() {
		return isFav;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getDateAired() {
		return dateAired;
	}

	public void setDateAired(String dateAired) {
		this.dateAired = dateAired;
	}

	public int getLenAudio() {
		return lenAudio;
	}

	public void setLenAudio(int lenAudio) {
		this.lenAudio = lenAudio;
	}

	public String getTeaserText() {
		return teaserText;
	}

	public void setTeaserText(String teaserText) {
		this.teaserText = teaserText;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	int id;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getMinTeaser() {
		return minTeaser;
	}

	public void setMinTeaser(String minTeaser) {
		this.minTeaser = minTeaser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
