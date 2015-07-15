# Pagination

Пока один пример и советы по использованию в Spring

1. Скачайте jar
2. Добавьте в библиотеку проекта
3. В конфигурации диспетчера добавьте бин

	``<bean id="paginationBuilder" class="com.artglorin.web.utils.PaginationBuilderImpl" factory-method="getInstance" scope="prototype"/>``

4. Добавьте зависимость в необходимый контроллер.

	``@Autowired``
	``private PaginationBuilder paginationBuilder;``

5. Инициализируйте параметры. Например в init методе @PostConstruct.

	> Совет. Если ваши сущности хранятся в базе данных, храните информацию об их количестве в переменной контроллера и проследите, чтобы в вашем коде при изменении количества сущностей, менялась переменная.

	``@PostConstruct
	public void init(){
	    ...
		paginationBuilder.setTotalItemsCount(itemsService.count());
		...
	}``

6. Используйте в методе обработчике запросов.

	``@RequestMapping("/listItems")
	public String getItemsListView (@CookieValue(required = false, defaultValue = "10", value = "itemsListLise") Integer listSize,
	                               @RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
		Pagination pagination = paginationBuilder.setCurrentPageNumber(page).setItemsListSize(listSize).build();
		model.addAttribute("Pagination", pagination);
		int start = (page - 1) * listSize;
		int end = start + listSize;
		model.addAttribute("Items", itemsService.findBetween(start, end));
		return "some-view-name";
	}``

7. Используйте Pagination в ViewResolver. Пример для Thymeleaf & Bootstrap

	``<nav th:if="${Pagination}" role="navigation">
	    <ul id="pager" class="pager"  th:with="url ='@{/listitems?page=}">
	        <th:block th:if="${Pagination.existPreviousSet}">
	            <li class="previous">
	                <a th:href="@{${url} + ${Pagination.firstPageInSet - 1}}"> <span>&larr;</span> Older </a>
                </li>
                <li class="disabled"><a href="#">&hellip;</a></li>
            </th:block>
            <li th:each="page : ${Pagination.setPages}" th:class="${page == Pagination.currentPage}? active">
                <a th:href="@{${url} + ${page}}" th:text="${page}"></a>
            </li>
            <th:block th:if="${Pagination.existNextSet}">
                <li class="disabled"><a href="#">&hellip;</a></li>
                <li class="previous">
                   <a th:href="@{${url} + ${Pagination.lastPageInSet + 1}}">Newer <span aria-hidden="true">&rarr;</span> </a>
               </li>
            </th:block>
	    </ul>
	</nav>``
