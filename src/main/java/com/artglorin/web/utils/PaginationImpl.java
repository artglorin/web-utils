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

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса {@link Pagination}
 *
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 08.07.15.
 */
public class PaginationImpl implements Pagination {

	/**
	 * номер текущей страницы
	 */
	private int current;

	/**
	 * Метка следующего набора
	 */
	private boolean nextSet;

	/**
	 * Список страниц в наборе
	 */
	private List<Integer> pages;

	/**
	 * Метка следующего набора
	 */
	private boolean previousSet;


	/**
	 * Конструктор устанавливающий все необходимые параметры
	 *
	 * @param totalItemsCount общее число существующих элементов
	 * @param currentPage     номер текущей страницы
	 * @param itemsListSize   количество отображаемых элементов на страницу
	 * @param pagesInSet      количество страниц в наборе
	 */
	PaginationImpl (int totalItemsCount, int currentPage, int itemsListSize, int pagesInSet) {

		// Подсчитаем сколько всего будет страниц
		int totalPages = totalItemsCount % itemsListSize > 0 ? totalItemsCount / itemsListSize + 1 : totalItemsCount / itemsListSize;
		totalPages = totalPages == 0 ? 1 : totalPages;
		// Проверим и установим правильное значение текущей страницы
		if (currentPage < 1) {
			currentPage = 1;
		} else if (currentPage > totalPages) {
			currentPage = totalPages;
		}
		this.current = currentPage;
		// Инициализируем номера первой и последней страницы в наборе
		int firstPage = pagesInSet;
		while (firstPage < currentPage) {
			firstPage += pagesInSet;
		}
		firstPage -= pagesInSet;
		int lastPage = firstPage + pagesInSet;
		if (lastPage > totalPages) {
			lastPage = totalPages;
		}
		// Узнаем сколько в итоге страниц будет в наборе
		int pagesInCurrentView = lastPage - firstPage;
		// Инициализируем список и заполним его значениями
		this.pages = new ArrayList<>(pagesInCurrentView);
		for (int i = 1; i <= pagesInCurrentView; i++) {
			this.pages.add(i + firstPage);
		}
		// Установим метку того, что есть предыдущие  страницы
		this.previousSet = currentPage > pagesInSet;
		// Установим метку того, что есть следующие страницы
		this.nextSet = this.pages.get(pagesInCurrentView - 1) < totalPages;
	}

	@Override
	public int getCurrentPage () {

		return current;
	}

	@Override
	public int getFirstPageInSet () {
		return pages.isEmpty() ? current : pages.get(0);
	}

	@Override
	public int getLastPageInSet () {
		return pages.isEmpty() ? current : pages.get(pages.size() - 1);
	}

	@Override
	public List<Integer> getSetPages () {
		return pages;
	}

	@Override
	public boolean isExistNextSet () {
		return nextSet;
	}

	@Override
	public boolean isExistPreviousSet () {
		return previousSet;
	}
}
