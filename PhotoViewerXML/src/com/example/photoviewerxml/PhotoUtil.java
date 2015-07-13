package com.example.photoviewerxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.Xml;

public class PhotoUtil {

	static public class PhotoPullParser {

		static ArrayList<Photo> photoUrls;
		static Photo photo;

		static public ArrayList<Photo> parsePhoto(InputStream in)
				throws XmlPullParserException, IOException {
			Log.d("TAG", "Pull parser selected");
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(in, "UTF-8");
			photoUrls = new ArrayList<Photo>();
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("photo")) {
						photo = new Photo();
						photo.setUrl(parser.getAttributeValue(null, "url_m"));
					}

					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("photo")) {
						photoUrls.add(photo);
						photo = null;
					}
					break;

				default:
					break;
				}
				event = parser.next();
			}
			return photoUrls;

		}

	}

	static public class PhotoSAXParser extends DefaultHandler {

		ArrayList<Photo> photoUrls;
		StringBuilder xmlInnerText;
		Photo photo;

		public ArrayList<Photo> getPhotoUrls() {
			return photoUrls;
		}

		static public ArrayList<Photo> parsePhoto(InputStream in)
				throws IOException, SAXException {
			Log.d("TAG", "SAX parser selected");
			PhotoSAXParser parser = new PhotoSAXParser();
			Xml.parse(in, Xml.Encoding.UTF_8, parser);

			return parser.getPhotoUrls();
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
			photoUrls = new ArrayList<Photo>();

		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);
			photo = new Photo();
			if (localName.equals("photo")) {

				photo.setUrl(attributes.getValue("url_m"));
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
			if (localName.equals("photo")) {

				photoUrls.add(photo);
			}

		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			super.characters(ch, start, length);

		}

	}

}
