<h1>CLEVERTEC (homework-10)</h1>

<p>CLEVERTEC homework-10 Servlets :</p>

<ol>

<li>
Написать CRUD запросы для работы со всеми таблицами.
</li>

<li>
Для метода findAll сделать пагинацию (по умолчанию 20 элементов на странице, 
если pagesize не передать).
</li>

<li>
Сделать GET метод, кот. генерирует чек (pdf формат должен возвращаться, 
если передавать не существующий товар для печати чека, то генерить ошибку).
</li>

<li>
Прикрутить возможность (через application.yml настраиваемый параметр), 
чтобы при подъеме приложения,  приложение создавало бы схему, таблицы и наполняло таблицы данными.
</li>

<li>
*Фильтры.
</li>

</ol>

<h2>Что сделано:</h2>
<ol>
<li>
Написаны классы в пакетах ru.clevertec.knyazev.dao, ru.clevertec.knyazev.service, 
ru.clevertec.knyazev.controller для обработки CRUD-запросов ко всем таблицам, используя сервлеты.
В сервлет-контроллерах добавлены ендпоинты: /addresses, /discards, /products, /sellers, /shops, 
/storages. В сервлет-контроллерах реализованы методы GET, POST, PUT, DELETE для сохранения, 
добавления, изменения, удаления данных из таблиц. Для данных CRUD-операций в реквест боди должен 
быть JSON: POST /products Body: {"id":null,"description":"Лимон СЛАДКИЙ для чая","isAuction":true}
</li>

<li>
Для метода getAll* реализована пагинация с возможностью определить количество элементов на странице.
В случае, если pagesize не передан, то по умолчанию выводится 20 элементов на странице.
Пример: GET /servlet/storages или GET /servlet/storages?page=1&pagesize=3 или GET /servlet/storages?page=1 или GET /servlet/storages?id=5
</li>

<li>
Реализован метод GET, который генерирует чек (pdf возвращается или генерируется ошибка).
Пример: /servlet/buy?purchase=3-1&purchase=1-1&purchase=5-1
</li>

<li>
Добавлена возможность (через application.yaml - src/main/resources  настраиваемые параметры), чтобы при подъеме приложеения создовалась 
схема, таблицы и таблицы наполнялись элементами, используя jakarta schema generation parameters и sql скрипты.
</li>

<li>
Добавлен фильтр в пакете ru.clevertec.knyazev.filter, проверяющий Mime тип, поддерживаемый клиентом. В случае если клиент, не поддерживает 
Mime тип, отправляемый сервером, клиенту выдается ошибка.
</li>
</ol>

<h3>Как запускать</h3>
<p>Поднимаем docker: docker-compose up -d</p>
<p>Проверяем ендпоинты в postman: servlet/sellers</p>