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

/**
 * Интерфейс для удобной настройки и создания реализации интерфейса {@link Pagination}
 *
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 08.07.15.
 * @see Pagination
 */
public interface PaginationBuilder {

	/**
	 * Количество показываемых элементов на каждой странице
	 */
	int DEFAULT_LIST_SIZE = 10;

	/**
	 * Количество страниу в наборе
	 */
	int DEFAULT_SHOWED_PAGES_COUNT = 10;

	/**
	 * Создать новый {@link Pagination} с текущими настройками
	 *
	 * @return новый экземпляр реализации интерфейса {@link Pagination}
	 * @see Pagination
	 */
	Pagination build ();

	/**
	 * Устанавливает номер текущей страницы
	 *
	 * @param page число-номер текущей страницы
	 * @return ссылку на себя для удобной настройки по цепочке
	 */
	PaginationBuilder setCurrentPageNumber (int page);

	/**
	 * Устанавливает общее количество существующих элементов
	 *
	 * @param totalItemsCount общее количество элементов
	 * @return ссылку на себя для удобной настройки по цепочке
	 */
	PaginationBuilder setTotalItemsCount (int totalItemsCount);

	/**
	 * Устанавливает количество отображаемых элементов на каждой странице
	 *
	 * @param listSize количество элементов на странице
	 * @return ссылку на себя для удобной настройки по цепочке
	 */
	PaginationBuilder setItemsListSize (int listSize);

	/**
	 * Устанавливает количество страниц в наборе
	 *
	 * @param pagesCountInSet количество страниц в наборе
	 * @return ссылку на себя для удобной настройки по цепочке
	 */
	PaginationBuilder setPagesCountInSet (int pagesCountInSet);
}
