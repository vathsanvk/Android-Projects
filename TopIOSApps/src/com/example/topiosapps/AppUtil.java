package com.example.topiosapps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class AppUtil {

	static public class AppPullParser {

		static String nameSpace;

		static public ArrayList<App> parseApp(InputStream in)
				throws XmlPullParserException, IOException {

			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			parser.setInput(in, "UTF-8");
			App app = null;
			boolean inEntry = false;
			String textValue = "";

			ArrayList<App> appData = new ArrayList<App>();
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:

					if (parser.getName().equals("entry")) {
						inEntry = true;
						app = new App();

					}

					if (inEntry) {
						if (parser.getName().equals("id")) {

							app.setId(parser.getAttributeValue(null, "id"));

						}
						if (parser.getName().equals("title")) {

							app.setTitle(parser.nextText());

						}
						if (parser.getName().equals("artist")) {

							app.setDevName(parser.nextText());

						}
						if (parser.getName().equals("link")) {

							app.setUrl(parser.getAttributeValue(null, "href"));

						}

						if (parser.getName().equals("image")) {

							String ht = parser
									.getAttributeValue(null, "height");
							if (ht.equals("53")) {
								app.setSmallPhotoUrl(parser.nextText().trim());
							} else if (ht.equals("100")) {
								app.setLargePhotoUrl(parser.nextText().trim());
							}

						}
						if (parser.getName().equals("price")) {
							app.setPrice(parser.getAttributeValue(null,
									"amount"));
						}

						if (parser.getName().equals("releaseDate")) {
							app.setRlsDate(parser
									.getAttributeValue(null, "label"));
						}

					}

					break;

				case XmlPullParser.END_TAG:

					if (parser.getName().equals("entry")) {

						appData.add(app);
						app = null;
						inEntry = false;
					}

					break;

				default:
					break;
				}
				event = parser.next();
			}
			return appData;

		}

	}

}
