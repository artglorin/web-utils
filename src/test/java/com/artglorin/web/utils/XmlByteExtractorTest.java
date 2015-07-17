/**
 * Copyright (C) 2015 Verminsky V.V. (develop@artglorin.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artglorin.web.utils;

import com.artglorin.web.utils.exceptions.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Base64;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 16.07.15.
 */
public class XmlByteExtractorTest {

	private Document document;

	@BeforeClass
	public void init () {

	}

	@Test(expectedExceptions = DecodeException.class)
	public void testInvalidBase64Data () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "data:image/png;base64,iiVBORw0K");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		extractor.extractImage();
	}

	private Element newDocument () {
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail("Cannot create document");
		}
		Element root = document.createElement("div");
		document.appendChild(root);
		return root;
	}

	@Test(expectedExceptions = InvalidDecodeTypeException.class)
	public void testInvalidDecodeTypeInBase64 () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "data:image/png;base64iVBORw0K");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		extractor.extractImage();
	}

	@Test(expectedExceptions = InvalidDecodeTypeException.class)
	public void testInvalidEncodingInBase64 () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "data:image/png;base32,iVBORw0K");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		extractor.extractImage();
	}

	@Test(expectedExceptions = InvalidExtensionException.class)
	public void testInvalidExtensionInBase64 () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "data:image/png.base64,iVBORw0K");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		extractor.extractImage();
	}

	@Test(expectedExceptions = InvalidTypeException.class)
	public void testInvalidTypeInBase64 () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "data:image.png;base64,iVBORw0K");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		extractor.extractImage();
	}

	@Test(expectedExceptions = InvalidTypeException.class)
	public void testInvalidTypeInUrl () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "https://ya.ru");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		assertEquals(extractor.extractImage().size(), 1);
	}

	@Test(expectedExceptions = UnsupportedFormatException.class)
	public void testUnsupportedDataFormatInBase64 () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "bla:image/png;base64,iVBORw0K");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		extractor.extractImage();
	}

	@Test
	public void testValidBase64Data () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");

		byte[] data = Base64.getEncoder().encode("String".getBytes());
		String dataString = "data:image/png;base64," + new String(data);
		data = Base64.getDecoder().decode(new String(data));

		img.setAttribute("src", dataString);
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		assertEquals(extractor.extractImage().size(), 1);

		img = document.createElement("img");
		img.setAttribute("src", dataString);
		root.appendChild(img);

		List<XmlByteExtractor.Description> result = extractor.extractImage();
		assertEquals(result.size(), 2);

		// check Descriptions data

		XmlByteExtractor.Description description = result.get(1);
		assertEquals(description.getExtension(), "png");
		assertEquals(description.getType(), "image");
		assertEquals(description.getNode(), img);
		assertEquals(description.getData(), data);
	}

	@Test
	public void testValidDataInUrl () throws ExtractionException {
		Element root = newDocument();

		Element img = document.createElement("img");
		img.setAttribute("src", "https://placeholdit.imgix.net/~text?txtsize=45&txt=Image&w=300&h=250");
		root.appendChild(img);

		XmlByteExtractor extractor = new XmlByteExtractor(document);
		assertEquals(extractor.extractImage().size(), 1);

		img.setAttribute("src", "http://alvinalexander.com/sites/default/files/just-be-mindfulness-reminders-app.jpg");
		assertEquals(extractor.extractImage().size(), 1);

		img.setAttribute("src", "https://avatars.yandex.net/get-music-content/c0933341.a.215696-1/200x200");
		assertEquals(extractor.extractImage().size(), 1);
	}

}
