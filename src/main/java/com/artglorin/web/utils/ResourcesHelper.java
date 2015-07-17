/**
 * Copyright (C) 2015 Verminsky V.V. (develop@artglorin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artglorin.web.utils;

import org.apache.commons.io.FileUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Класс для работы со статическими ресурсами, которые хранятся на сервере.
 *
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 15.07.15.
 */
public class ResourcesHelper {

	private String pathToResources;

	private String realPath;

	/**
	 * Единственный  конструктор аргументы которого необходимы для работы класса
	 *
	 * @param pathToResources путь от корня приложения до места хранения ресурсов, не может быть null
	 * @param servletContext  контекст текущего сервлета, чтобы узнать реальный путь до места сохранения ресурсов
	 */
	public ResourcesHelper (String pathToResources, ServletContext servletContext) {
		if (pathToResources == null) {
			throw new IllegalArgumentException("Required argument pathToResources is null");
		}
		if (servletContext == null) {
			throw new IllegalArgumentException("Required argument servletContext is null");
		}
		this.pathToResources = pathToResources;
		realPath = servletContext.getRealPath(pathToResources);
	}

	/**
	 * Если в качестве параметра передать путь до папки, то будет удаленна папка и все под папки и файлы.
	 *
	 * @param path путь до файла или папки
	 * @throws IOException
	 */
	public void deleteResourcesDirectory (String... path) throws IOException {
		FileUtils.deleteDirectory(this.createRealPath(path).toFile());
	}

	/**
	 * Создает реальный путь в файловой системе
	 *
	 * @param path путь который нужно создать
	 * @return итоговый путь
	 */
	private Path createRealPath (String... path) {
		return Paths.get(realPath, path);
	}

	/**
	 * Возвращает путь до ресурсов от корня приложения.
	 * Ту же строку, которая была передана в качестве аргумента конструктора
	 *
	 * @return строку путь до ресурсов от корня приложения.
	 */
	public String getPathToResources () {
		return pathToResources;
	}

	/**
	 * Записывает данные из массива байтов в файл указанный во втором аргументе.
	 * Данные будут перезаписывать данные
	 *
	 * @param data массив данных, которые нужно записать.
	 * @param path путь до файла в который нужно записать данные. Путь может как существовать, так и нет. Если в конце
	 *             пути добавить расширение, то будет создан файл с этим расширением. Например если передать массив строк
	 *             ["data","file",".txt"], то данные будет записанный в файл по пути "/pathToResources/data/file.txt".
	 *             Если передавать массив ["data","file.txt"] или одну строку "/data/file.txt", данные также будут
	 *             записанный в файл по пути "/pathToResources/data/file.txt"
	 * @throws IOException
	 */
	public void saveData (byte[] data, String... path) throws IOException {
		if (path.length == 0) {
			throw new IOException("Path cannot be null");
		}
		Path file = createRealPath(path);
		String[] filePath = null;
		if (file.toFile().exists()) {
			FileUtils.writeByteArrayToFile(file.toFile(), data);
		} else {
			if (path.length > 1) {
				if (path[path.length - 1].startsWith(".")) {
					filePath = Arrays.copyOf(path, path.length - 2);
				}
			}
		}
		if (filePath == null) {
			filePath = Arrays.copyOf(path, path.length - 1);
		}
		createResourcesDirectory(filePath);
		FileUtils.writeByteArrayToFile(file.toFile(), data);
	}

	/**
	 * Создаст полный путь указанный в аргументе
	 *
	 * @param path массив строк пути, который нужно создать
	 * @throws IOException
	 */
	public void createResourcesDirectory (String... path) throws IOException {
		Files.createDirectories(this.createRealPath(path));
	}
}




