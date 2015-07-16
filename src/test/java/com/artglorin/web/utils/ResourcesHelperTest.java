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

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 15.07.15.
 */
public class ResourcesHelperTest {

	private String pathToResources = "ResourcesHelperTestFolder";

	private ResourcesHelper resourcesHelper;

	private Path testFolder = null;

	@AfterClass(enabled = true)
	public void deleteTestFolder () throws IOException {
		FileUtils.deleteDirectory(testFolder.toFile());
	}

	@Test(dependsOnGroups = "init")
	public void testCreateResourcesDirectory () throws IOException {
		resourcesHelper.createResourcesDirectory("testCreateDirectory");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), "/testCreateDirectory")));
		resourcesHelper.createResourcesDirectory("test", "create", "directory");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), "test", "create", "directory")));
	}

	@Test(dependsOnMethods = "testCreateResourcesDirectory")
	public void testDeleteResourcesDirectory () throws IOException {
		resourcesHelper.createResourcesDirectory("testCreateDirectory");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), "/testCreateDirectory")));
		resourcesHelper.deleteResourcesDirectory("testCreateDirectory");
		assertFalse(Files.exists(Paths.get(testFolder.toString(), "/testCreateDirectory")));
		resourcesHelper.deleteResourcesDirectory("testCreateDirectory");
		resourcesHelper.createResourcesDirectory("test", "create", "directory");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), "test", "create", "directory")));
		resourcesHelper.deleteResourcesDirectory("test", "create", "directory");
		assertFalse(Files.exists(Paths.get(testFolder.toString(), "test", "create", "directory")));
		resourcesHelper.deleteResourcesDirectory("test", "create", "directory");
	}

	@BeforeClass
	public void testGetInstance () {
		try {
			testFolder = Files.createTempDirectory(pathToResources);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Cannot create Test directory");
		}
		ServletContext servletContext = mock(ServletContext.class);
		when(servletContext.getRealPath(pathToResources)).thenAnswer(answer -> testFolder.toAbsolutePath().toString());
		resourcesHelper = new ResourcesHelper(pathToResources, servletContext);
	}

	@Test(dependsOnGroups = "init")
	public void testPathToResources () {
		assertEquals(resourcesHelper.getPathToResources(), pathToResources);
	}

	@Test(dependsOnMethods = "testDeleteResourcesDirectory")
	public void testSaveDataToServer () throws IOException {
		resourcesHelper.saveData("SOME DATA".getBytes(), "data", "folder", "file.txt");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), "data", "folder", "file.txt")));
		resourcesHelper.saveData("SOME DATA".getBytes(), ".data");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), ".data")));
		resourcesHelper.saveData("SOME DATA".getBytes(), "/datsa");
		assertTrue(Files.exists(Paths.get(testFolder.toString(), "datsa")));
	}

}
