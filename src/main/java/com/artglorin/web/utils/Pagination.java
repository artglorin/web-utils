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

import java.util.List;

/**
 * Интерфейс для работы с нумерацией на веб страницах.
 *
 * @author Verminsky V.V. e-mail: develop@artglorin.com
 * @version 0.0.1
 *          Created on 08.07.15.
 */
public interface Pagination {

	/**
	 * @return номер текущей страницы в наборе который представляет класс.
	 */
	int getCurrentPage ();

	/**
	 * @return номер первой страницы в наборе
	 */
	int getFirstPageInSet ();

	/**
	 * @return номер последней страницы в наборе
	 */
	int getLastPageInSet ();

	/**
	 * @return набор с номерами страниц в текущем наборе
	 */
	List<Integer> getSetPages ();

	/**
	 * @return true если существует следующий набор страниц
	 */
	boolean isExistNextSet ();

	/**
	 * @return true если существует предыдущий набор страниц
	 */
	boolean isExistPreviousSet ();
}
