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
 * Реализация интерфейса {@link PaginationBuilder}
 *
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 08.07.15.
 * @see PaginationBuilder
 */
public class PaginationBuilderImpl implements PaginationBuilder {

	private int listSize = DEFAULT_LIST_SIZE;

	private int pagesCount = DEFAULT_SHOWED_PAGES_COUNT;

	private int itemsCount;

	private int currentPage;

	private PaginationBuilderImpl () {
	}

	/**
	 * @return новый экземпляр класса
	 */
	public static PaginationBuilder getInstance () {
		return new PaginationBuilderImpl();
	}

	@Override
	public PaginationBuilder setPagesCountInSet (int pagesCountInSet) {
		pagesCount = pagesCountInSet > 0 ? pagesCountInSet : 1 ;
		return this;
	}

	@Override
	public PaginationBuilder setItemsListSize (int listSize) {
		this.listSize = listSize > 0 ? listSize : 1;
		return this;
	}

	@Override
	public PaginationBuilder setTotalItemsCount (int totalItemsCount) {
		this.itemsCount = totalItemsCount >= 0 ? totalItemsCount : 0;
		return this;
	}

	@Override
	public PaginationBuilder setCurrentPageNumber (int page) {
		this.currentPage = page > 0 ? page : 1;
		return this;
	}

	@Override
	public Pagination build () {
		return new PaginationImpl(itemsCount, currentPage, listSize, pagesCount);
	}

}

