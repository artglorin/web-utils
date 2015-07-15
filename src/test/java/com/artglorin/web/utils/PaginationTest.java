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

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author Verminsky V.V. e-mail: mail@artglorin.com
 * @version 0.0.1
 *          Created on 03.07.15.
 */
public class PaginationTest {

	private PaginationBuilder builder;

	@Test (groups = "init")
	public void testPaginationBuilderGetInstance(){
		builder = PaginationBuilderImpl.getInstance();
		assertNotNull(builder);
	}

	@Test(dependsOnMethods = "testPaginationBuilderGetInstance", groups = "init")
	public void testPaginationBuilderBuild(){
		assertNotNull(builder.build());
	}

	@Test(dependsOnGroups = "init")
	public void testInvalidCurrentPage(){
		Pagination pagination = builder.setCurrentPageNumber(-1).setItemsListSize(10).setTotalItemsCount(5).setPagesCountInSet(2).build();
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 1);
		assertEquals(pagination.getSetPages().size(), 1);
		assertFalse(pagination.isExistPreviousSet());
		assertFalse(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testInvalidListSize(){
		Pagination pagination = builder.setCurrentPageNumber(1).setItemsListSize(-1).setTotalItemsCount(5).setPagesCountInSet(10).build();
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 5);
		assertEquals(pagination.getSetPages().size(), 5);
		assertFalse(pagination.isExistPreviousSet());
		assertFalse(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testInvalidTotalItemsCount(){
		Pagination pagination = builder.setCurrentPageNumber(1).setItemsListSize(1).setTotalItemsCount(-1).setPagesCountInSet(2).build();
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 1);
		assertEquals(pagination.getSetPages().size(), 1);
		assertFalse(pagination.isExistPreviousSet());
		assertFalse(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testInvalidPagesInSet(){
		Pagination pagination = builder.setCurrentPageNumber(1).setItemsListSize(1).setTotalItemsCount(5).setPagesCountInSet(-1).build();
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 1);
		assertEquals(pagination.getSetPages().size(), 1);
		assertFalse(pagination.isExistPreviousSet());
		assertTrue(pagination.isExistNextSet());
	}


	@Test(dependsOnGroups = "init")
	public void testItemsCountLessThenListSize_FirstPageInSet(){
		// Текущая страница - 1
		// Размер списка - 3
		// Размер набора - 2
		// Количество элементов - 2
		Pagination pagination = builder
				.setCurrentPageNumber(1)
				.setItemsListSize(3)
				.setPagesCountInSet(2)
				.setTotalItemsCount(2).build();

		// Ожидаемый результат
		// Текущая страницы - 1
		// Первая страницы в наборе - 1
		// Последняя страницы в наборе - 1
		// Страниц в наборе - 1
		// Предыдущий набор - нет
		// Следующий набор - нет
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 1);
		assertEquals(pagination.getSetPages().size(), 1);
		assertFalse(pagination.isExistPreviousSet());
		assertFalse(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testItemsCountEqualsListSize_FirstPageInSet(){
		// Текущая страница - 1
		// Размер списка - 2
		// Размер набора - 2
		// Количество элементов - (2*2) = 4
		Pagination pagination = builder
				.setCurrentPageNumber(1)
				.setItemsListSize(2)
				.setPagesCountInSet(2)
				.setTotalItemsCount(4).build();

		// Ожидаемый результат
		// Текущая страницы - 1
		// Первая страницы в наборе - 1
		// Последняя страницы в наборе - 2
		// Страниц в наборе - 2
		// Предыдущий набор - нет
		// Следующий набор - нет
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 2);
		assertEquals(pagination.getSetPages().size(), 2);
		assertFalse(pagination.isExistPreviousSet());
		assertFalse(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testItemsCountMoreThenListSize_FirstPageInSet(){
		// Текущая страница - 1
		// Размер списка - 2
		// Размер набора - 2
		// Количество элементов - (2*2) + 1 = 5
		Pagination pagination = builder
				.setCurrentPageNumber(1)
				.setItemsListSize(2)
				.setPagesCountInSet(2)
				.setTotalItemsCount(5).build();

		// Ожидаемый результат
		// Текущая страницы - 1
		// Первая страницы в наборе - 1
		// Последняя страницы в наборе - 2
		// Страниц в наборе - 2
		// Предыдущий набор - нет
		// Следующий набор - есть
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 2);
		assertEquals(pagination.getSetPages().size(), 2);
		assertFalse(pagination.isExistPreviousSet());
		assertTrue(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testItemsCountMoreThenPagesSetSize_FirstPageInSet(){
		// Текущая страница - 1
		// Размер списка - 2
		// Размер набора - 2
		// Количество элементов - 2*2 +1 = 5
		Pagination pagination = builder
				.setCurrentPageNumber(1)
				.setItemsListSize(2)
				.setPagesCountInSet(2)
				.setTotalItemsCount(5).build();

		// Ожидаемый результат
		// Текущая страницы - 1
		// Первая страницы в наборе - 1
		// Последняя страницы в наборе - 2
		// Страниц в наборе - 2
		// Предыдущего набора - нет
		// Следующий набор - есть
		assertEquals(pagination.getCurrentPage(), 1);
		assertEquals(pagination.getFirstPageInSet(), 1);
		assertEquals(pagination.getLastPageInSet(), 2);
		assertEquals(pagination.getSetPages().size(), 2);
		assertFalse(pagination.isExistPreviousSet());
		assertTrue(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testItemsCountMoreThenPagesSetSize_MiddlePageInSet(){
		// Текущая страница - 3
		// Размер списка - 2
		// Размер набора - 2
		// Количество элементов - (2*2)*3 = 12
		Pagination pagination = builder
				.setCurrentPageNumber(3)
				.setItemsListSize(2)
				.setPagesCountInSet(2)
				.setTotalItemsCount(12).build();

		// Ожидаемый результат
		// Текущая страницы - 3
		// Первая страницы в наборе - 3
		// Последняя страницы в наборе - 4
		// Страниц в наборе - 2
		// Предыдущего набора - есть
		// Следующий набор - есть
		assertEquals(pagination.getCurrentPage(), 3);
		assertEquals(pagination.getFirstPageInSet(), 3);
		assertEquals(pagination.getLastPageInSet(), 4);
		assertEquals(pagination.getSetPages().size(), 2);
		assertTrue(pagination.isExistPreviousSet());
		assertTrue(pagination.isExistNextSet());
	}

	@Test(dependsOnGroups = "init")
	public void testItemsCountMoreThenPagesSetSize_LastPageInSet(){
		// Текущая страница - 3
		// Размер списка - 2
		// Размер набора - 2
		// Количество элементов - (2*2)*3 = 12
		Pagination pagination = builder.setCurrentPageNumber(3).setItemsListSize(2).setTotalItemsCount(12).setPagesCountInSet(2).build();

		// Ожидаемый результат
		// Текущая страницы - 3
		// Первая страницы в наборе - 3
		// Последняя страницы в наборе - 4
		// Страниц в наборе - 2
		// Предыдущего набора - есть
		// Следующий набор - есть
		assertEquals(pagination.getCurrentPage(), 3);
		assertEquals(pagination.getFirstPageInSet(), 3);
		assertEquals(pagination.getLastPageInSet(), 4);
		assertEquals(pagination.getSetPages().size(), 2);
		assertTrue(pagination.isExistPreviousSet());
		assertTrue(pagination.isExistNextSet());
	}
}
