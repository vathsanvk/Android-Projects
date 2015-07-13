package com.example.flickrbrowser;

public class Photo {
	private String title;
	private String author;
	private String authorId;
	private String link;
	private String image;
	private String tags;

	public Photo(String title, String author, String authorId, String link,
			String image, String tags) {
		super();
		this.title = title;
		this.author = author;
		this.authorId = authorId;
		this.link = link;
		this.image = image;
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getLink() {
		return link;
	}

	public String getImage() {
		return image;
	}

	public String getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return "Photo [Title=" + title + ", author=" + author + ", authorId="
				+ authorId + ", link=" + link + ", image=" + image + ", tags="
				+ tags + "]";
	}

}
