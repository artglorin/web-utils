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
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Класс для того, чтобы извлекать данные в виде массива байт из xml докумнента.
 *
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 17.07.15.
 */
public class XmlByteExtractor {


	private final Document document;

	/**
	 * Единственный конструктор, которому в качестве аргумента передается документ из которого нужно извлекать данные.
	 * Связь с документом остается. Данные не дублируются.
	 *
	 * @param document Документ из которого нужно извлечь данные
	 */
	public XmlByteExtractor (Document document) {
		this.document = document;
	}

	/**
	 * <p>Поочереди извлекает все изображения, аудио и видео из документа.
	 * данные должны находится в тегах &lt;img&gt; &lt;audio&gt; &lt;video&gt;
	 * , а источник данных в аттрибуте "src".</p>
	 * <p>На данный момент поддерживается источники в виде</p>
	 * <ul>
	 * <li>зашифрованный HTML Base64, т.е. "data:audio/wav;base64,iVBORw0K"</li>
	 * <li>URL по протоколу http и https</li>
	 * </ul>
	 *
	 * @return список с описателями извлеченных типов данных. Пустой список, если не одного изображения не было найденно
	 * @throws ExtractionException более подробные данные об ошибках можно посмотреть в {@link com.artglorin.web.utils.exceptions}
	 */
	public List<Description> extractAll () throws ExtractionException {
		List<Description> result = new ArrayList<>();
		result.addAll(extractAudio());
		result.addAll(extractImage());
		result.addAll(extractVideo());
		return result;
	}

	/**
	 * <p>Извлекает аудио из документа.
	 * данные должны находится в теге &lt;audio&gt;
	 * , а источник данных в аттрибуте "src".</p>
	 * <p>На данный момент поддерживается источники в виде</p>
	 * <ul>
	 * <li>зашифрованный HTML Base64, т.е. "data:audio/wav;base64,iVBORw0K"</li>
	 * <li>URL по протоколу http и https</li>
	 * </ul>
	 *
	 * @return список с описателями извлеченных типов данных. Пустой список, если не одного изображения не было найденно
	 * @throws ExtractionException более подробные данные об ошибках можно посмотреть в {@link com.artglorin.web.utils.exceptions}
	 */
	public List<Description> extractAudio () throws ExtractionException {
		return this.extract("audio", "src");
	}

	/**
	 * <p>Извлекает изображения из документа.
	 * данные должны находится в теге &lt;img&gt;
	 * , а источник данных в аттрибуте "src".</p>
	 * <p>На данный момент поддерживается источники в виде</p>
	 * <ul>
	 * <li>зашифрованный HTML Base64, т.е. "data:image/png;base64,iVBORw0K"</li>
	 * <li>URL по протоколу http и https</li>
	 * </ul>
	 *
	 * @return список с описателями извлеченных типов данных. Пустой список, если не одного изображения не было найденно
	 * @throws ExtractionException более подробные данные об ошибках можно посмотреть в {@link com.artglorin.web.utils.exceptions}
	 */
	public List<Description> extractImage () throws ExtractionException {
		return this.extract("img", "src");
	}

	/**
	 * <p>Извлекает видео из документа.
	 * данные должны находится в теге &lt;video&gt;
	 * , а источник данных в аттрибуте "src".</p>
	 * <p>На данный момент поддерживается источники в виде</p>
	 * <ul>
	 * <li>зашифрованный HTML Base64, т.е. "data:video/mpeg4;base64,iVBORw0K"</li>
	 * <li>URL по протоколу http и https</li>
	 * </ul>
	 *
	 * @return список с описателями извлеченных типов данных. Пустой список, если не одного изображения не было найденно
	 * @throws ExtractionException более подробные данные об ошибках можно посмотреть в {@link com.artglorin.web.utils.exceptions}
	 */
	public List<Description> extractVideo () throws ExtractionException {
		return this.extract("video", "src");
	}

	private List<Description> extract (String tagName, String valueAttr) throws ExtractionException {
		NodeList tags = document.getElementsByTagName(tagName);
		List<Description> result = new ArrayList<>(tags.getLength());
		byte[] data;
		String type, extension, value, decodeType;
		Node node;
		for (int i = 0; i < tags.getLength(); i++) {
			node = tags.item(i);
			value = node.getAttributes().getNamedItem(valueAttr).getNodeValue();
			if (value.startsWith("data:")) {
				try {
					type = value.substring(value.indexOf(":") + 1, value.indexOf("/"));
				} catch (Exception e) {
					throw new InvalidTypeException(value);
				}
				try {
					extension = value.substring(value.indexOf("/") + 1, value.indexOf(";"));
				} catch (Exception e) {
					throw new InvalidExtensionException(value);
				}
				try {
					decodeType = value.substring(value.indexOf(";") + 1, value.indexOf(","));
				} catch (Exception e) {
					throw new InvalidDecodeTypeException(value);
				}
				if (decodeType.equals("base64")) {
					try {
						data = Base64.getDecoder().decode(value.substring(value.indexOf(",") + 1));
					} catch (Exception e) {
						throw new DecodeException(e.toString());
					}
				} else {
					throw new InvalidDecodeTypeException(decodeType);
				}
				result.add(new Description()
						.setData(data)
						.setExtension(extension)
						.setNode(node)
						.setType(type));
			} else if (value.startsWith("http:") || value.startsWith("https:")) {
				URL url;
				try {
					url = new URL(value);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new ExtractionException("Cannot create URL. Exception: " + e.toString());
				}
				URLConnection connection;
				try {
					connection = url.openConnection();
				} catch (IOException e) {
					throw new ExtractionException("Cannot open connection");
				}
				String contentType = connection.getContentType();

				if (contentType.toLowerCase().startsWith("image/") || contentType.toLowerCase().startsWith("audio/") || contentType.toLowerCase().startsWith("video/")) {
					type = contentType.substring(0, contentType.indexOf("/"));
					extension = contentType.substring(contentType.indexOf("/") + 1);
					try {
						data = IOUtils.toByteArray(connection.getInputStream());
					} catch (IOException e) {
						throw new ExtractionException("Cannot get bytes from source: " + value);
					}
					result.add(new Description().setData(data).setExtension(extension).setType(type).setNode(node));
				} else {
					throw new InvalidTypeException(contentType);
				}
			} else {
				throw new UnsupportedFormatException(value);
			}
		}
		return result;
	}

	/**
	 * Класс для описания типа извлеченных данных
	 */
	public class Description {

		private byte[] data;

		private String extension;

		private Node node;

		private String type;

		/**
		 * @return байтовый массив полученных данных
		 */
		public byte[] getData () {
			return data;
		}

		private Description setData (byte[] data) {
			this.data = data;
			return this;
		}

		/**
		 * @return строку расширение файла
		 */
		public String getExtension () {
			return extension;
		}

		private Description setExtension (String extension) {
			this.extension = extension;
			return this;
		}

		/**
		 * @return Node из которой извлекались данные
		 */
		public Node getNode () {
			return node;
		}

		private Description setNode (Node node) {
			this.node = node;
			return this;
		}

		/**
		 * @return тип данных image, audio, video
		 */
		public String getType () {
			return type;
		}

		private Description setType (String type) {
			this.type = type;
			return this;
		}
	}
}
