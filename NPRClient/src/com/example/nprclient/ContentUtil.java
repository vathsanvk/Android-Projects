package com.example.nprclient;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentUtil {

	static public class ContentJSONParser {
		static ArrayList<Program> parsePrograms(String in) throws JSONException {
			ArrayList<Program> programsList = new ArrayList<Program>();

			JSONObject root = new JSONObject(in);
			JSONArray programsJSONArray = root.getJSONArray("item");

			for (int i = 0; i < programsJSONArray.length(); i++) {
				JSONObject programJSONObject = programsJSONArray
						.getJSONObject(i);
				Program program = new Program();
				program.setTitle(programJSONObject.getJSONObject("title")
						.getString("$text"));
				program.setId(programJSONObject.getInt("id"));

				programsList.add(program);

			}

			return programsList;

		}

		static ArrayList<Topic> parseTopics(String in) throws JSONException {
			ArrayList<Topic> topicsList = new ArrayList<Topic>();

			JSONObject root = new JSONObject(in);
			JSONArray topicsJSONArray = root.getJSONArray("item");

			for (int i = 0; i < topicsJSONArray.length(); i++) {
				JSONObject topicJSONObject = topicsJSONArray.getJSONObject(i);
				Topic topic = new Topic();
				topic.setTitle(topicJSONObject.getJSONObject("title")
						.getString("$text"));
				topic.setId(topicJSONObject.getInt("id"));

				topicsList.add(topic);

			}

			return topicsList;

		}

		static ArrayList<Story> parseStories(String in) throws JSONException {
			ArrayList<Story> storiesList = new ArrayList<Story>();

			JSONObject root1 = new JSONObject(in);
			JSONObject obj1 = root1.getJSONObject("list");

			JSONArray storiesJSONArray = obj1.getJSONArray("story");

			for (int i = 0; i < storiesJSONArray.length(); i++) {

				JSONObject storyJSONObject = storiesJSONArray.getJSONObject(i);
				Story story = new Story();

				if (!storyJSONObject.isNull("title")
						&& !storyJSONObject.getJSONObject("title").isNull(
								"$text")) {
					String temp = storyJSONObject.getJSONObject("title")
							.getString("$text");

					String temp1 =android.text.Html.fromHtml(temp).toString();
					story.setTitle(temp1);
				}
				if (!storyJSONObject.isNull("id")) {
					story.setId(storyJSONObject.getInt("id"));
				}
				if (!storyJSONObject.getJSONObject("miniTeaser")
						.isNull("$text")) {

					String temp = storyJSONObject.getJSONObject("miniTeaser")
							.getString("$text");

					String temp1 =android.text.Html.fromHtml(temp).toString();
					story.setMinTeaser(temp1);
				}
				if (!storyJSONObject.isNull("pubDate")
						&& !storyJSONObject.getJSONObject("pubDate").isNull(
								"$text")) {
					story.setPubDate(storyJSONObject.getJSONObject("pubDate")
							.getString("$text"));
				}

				if (!storyJSONObject.isNull("thumbnail")) {
					story.setThumbnail(storyJSONObject
							.getJSONObject("thumbnail").getJSONObject("medium")
							.getString("$text"));
				}
				if (!storyJSONObject.isNull("link")) {
					JSONArray linkArray = storyJSONObject.getJSONArray("link");
					story.setLink(linkArray.getJSONObject(0).getString("$text"));
				}

				if (!storyJSONObject.isNull("byline")) {
					JSONArray byLineArray = storyJSONObject
							.getJSONArray("byline");
					story.setRepName(byLineArray.getJSONObject(0)
							.getJSONObject("name").getString("$text"));
				}

				if (!storyJSONObject.isNull("teaser")) {
					String temp = storyJSONObject.getJSONObject("teaser")
							.getString("$text");

					String temp1 =android.text.Html.fromHtml(temp).toString();

					story.setTeaserText(temp1);

				}

				if (!storyJSONObject.isNull("audio")) {
					JSONArray audioArray = storyJSONObject
							.getJSONArray("audio");
					if (!audioArray.getJSONObject(0).getJSONObject("duration")
							.isNull("$text")) {
						story.setLenAudio(audioArray.getJSONObject(0)
								.getJSONObject("duration").getInt("$text"));
					}

					if (!audioArray.getJSONObject(0).getJSONObject("format")
							.isNull("mp3")) {
						JSONArray formatArray = audioArray.getJSONObject(0)
								.getJSONObject("format").getJSONArray("mp3");

						if (!formatArray.getJSONObject(0).isNull("$text")) {
							story.setStreamUrl(formatArray.getJSONObject(0)
									.getString("$text"));
						}
					}

				}

				storiesList.add(story);

			}

			return storiesList;

		}
	}
}
