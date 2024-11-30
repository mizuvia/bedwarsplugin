# Про плагін

BedWars — відома міні-гра в пісочниці Minecraft, головна задача гравця в якій залишитися єдиною живою командою. Команди відрізняються один від одного кольором, починаючи від стандартних жовтого/синього/червого/зеленого, закінчуючи чорним/коричневим/пурпуровим і т.д.

Серцем команди є ліжко, що знаходиться на острівці команди. Допоки ліжко не зламане, після смерті гравець даної команди перероджуватиметься. Для того щоб подолати команду, необхідно зламати ліжко та вбити всіх гравців цієї команди.

Ігрове поле складається з множини острівців, що висять в повітрі. Різні реалізації цієї міні-гри пропонують різні форми поля, проте головними складовими залишаються острівці команд (важливою складовою є наявність ліжка та характерні кольорові відмінності), що є однаковими один між одним, та центральний острів. Також нерідко на карті розташовані проміжні острівці. Руйнувати ігрове поле заборонено, можна тільки встановлювати нові блоки та видаляти встановлені гравцями блоки.

Для того, щоб будувати мости між острівцями, оборонятися, нападати, брати участь в боротьбі необхідно мати відповідні матеріали та інструменти. Для цього на кожному командному острівці розташовані NPC, який представляє ігровий магазин, в більшості випадків це житель. В ньому в окремих категоріях розташовуються будівельні матеріали, зброя, зілля, їжа, спеціальні предмети, пастки, інструменти, броня.

Валютою в грі є різного рівня матеріали. В більшості випадків це цегла, залізо, золото, в останніх версіях також є смарагд. Деякі реалізації дозволяють обмін матеріалів (100 цегли = 1 залізо до прикладу). Частина матеріалів з’являється з різною частотою на острові команди, матеріал найвищого рівня — на центральному острові. Нерідко деякі елементи можна купити тільки за найвищу валюту, яку не можна отримати ніяким іншим шляхом, окрім відвідування центру та боротьби з іншими охочими.

Деякі реалізації гри пропонують “поліпшення” острову команди. Для цього встановлений другий NPC, що відповідає за розвиток острову. Для того, щоб розвинути острів, необхідно знайти інший матеріал (часто це діамант), що з’являється на проміжних островах. Серед поліпшень часто можна зустріти швидкість роботи генератора на острові, тимчасовий повний захист ліжка, регенерацію здоров’я, гостроту леза, ефективність інструментів, захист броні та інше.

В грі є фази. Кожна фаза змінює певну поведінку поля, наприклад пришвидшує генерацію матеріалів в центрі або матеріалів для розвитку острова команди. Останньою фазою є руйнування всіх ліжок, що залишились у грі, що знімає “безсмертя” з гравців. Також іноді можна зустріч ще таку одну останню фазу, як виклик драконів. У драконів є можливість руйнувати карту, тобто після цієї фази не залишається територій, що призводить до смерті всіх гравців, якщо вони вирішили не вбивати один одного.

# Проєктування

Проєктування власної реалізації даної міні-гри складалося з основних фаз:

1. Аналіз конкурентів: для цього було проведено серію ігор на відомих пострадянських та європейських/американських серверах Minecraft, проаналізовано механіки та їхню реалізацію, виокремлено окремі правила гри, що присутні в конкретній реалізації
2. Аналіз коду: дуже рідко, але деякі сервери тримають реалізації в публічному доступі, також є розробницькі компанії, що продають окремі реалізації цієї міні-гри з можливістю модифікацій під час налаштувань на своєму сервері
3. Документування основних функцій та механік для гравців у власній грі. За основу брався найбільш відомий міжнародний сервер Hypixel: 
    1. Матеріали: цегла, залізо, золото, діамант на проміжному та смарагд на центральному островах. Неможливість обміну матеріалів один між одним. Повернення частини матеріалів гравця, якого вбив інший гравець.
    2. Поліпшення магазину: замість купівлі інструментів різних рівнів, необхідно купити інструмент першого рівня, розвинути його до другого, потім до третього і так далі. Автоматичне екіпірування броні.
    3. Розвиток острова: збільшення гостроти, захисту, ефективності, регенерація, швидкості генерації матеріалів, захист ліжка, пастки для інших гравців
    4. Захисники големи: можливість викликати залізного голема, що захищає острів від нападників.
    5. Фази: 6-хвилинні фази, спочатку збільшення швидкості генерації діамантів, потім смарагдів, потім знову діамантів, потім знову смарагдів, руйнування ліжок та кінець гри. Сумарно гра займає не більше 36 хвилин.
4. Документування механізмів, що не є механіками для гравців, проте доповнюють гру і роблять її загалом можливою:
    1. Зона очікування: частина гри, під час якої сама гра ще не активна, проте люди під’єднуються до серверу та очікують початку
    2. Бічне вікно стану гри: невеликий дисплей на правій стороні екрану, на якому відображається поточна фаза, статус ліжок і команд, час до наступної фази, до якої команди належить гравець, кількість вбивств, кількість зламаних ліжок і т.д.
    3. Генерація матеріалів: логіка, що дозволяє налаштовувати час генерації між різними видами матеріалів на островах
    4. Управління меню: усі меню, що є у грі (вибір команди, магазин, розвиток острова, купівля пасток, швидкий вибір товарів) налаштовуються окремо, тобто ціна товарів та їхня кількість, логіка “поліпшення” інструментів з одного рівня на інший
    5. Вбивство: через те, що після реального вбивства Minecraft показує смертельне вікно, що пропонує вийти з серверу, необхідно реалізувати симуляцію вбивства шляхом порівняння удару та наявного здоров’я
    6. Логування в чаті гри: відображення хто кого вбив, хто чиє ліжко зламав, яка команда повністю померла, відображення нових фаз та інше.
    7. Захисні механізми: блокування руйнування блоків, що не можна руйнувати, правила розлиття води, відсутність голоду, захист інвентарів-меню від багів та дюпів, захист від далекого знаходження від центру по горизонталі та вертикалі
    8. Літаючі сутності: зміна логіки роботи сутностей, що рухаються в грі, тобто стріл, вибухівок, вогняні ядра
    9. Косметичні механізми: літаючі голограми та блоки на особливих точках карти
    10. Робота з файлами: зчитування файлу конфігурації
    11. Робота зі світами: завантаження та розвантаження світів для оптимізації використання пам’яті та процесорного часу
    12. Робота з часом: оновлення фази та спостерігання за подіями.
    13. Статистика: збір інформації про гравця, яку він може прочитати в загальному меню сервера, наприклад кількість вбивств, зламаних ліжок, випитих зіллів і т.д.
    14. Комунікація з іншими ядрами серверів: перед тим, як увійти на ігровий сервер, гравець бачить в меню назву карти та поточну кількість гравців на ній (наприклад, 5/16), для цього, необхідно через Redis передати інформацію про те, що на сервер хтось приєднався
    15. Обробка інших подій, що є складовою гри
5. Визначення параметрів, що стануть конфігурацією гри, наприклад:
    1. Кількість гравців сумарна
    2. Кількість команд
    3. Координати центру мапи
    4. Координати точок генерації ресурсів
    5. Координати NPC
    6. Назва карти та її код
    7. Координати ліжка (верхньої та нижньої частини)
    8. Координати відродження гравців на острові

# Ігровий рушій

Головним класом плагіну є клас `Plugin` , в якому знаходяться загальні налаштування плагіну. В ньому створюються основні масиви граців і команд, запускається в роботу бічне вікно (оскільки воно працює що під час гри, що під час очікування), ініціалізується клас гри `Game` , створюється комунікація з іншими серверами через клас `Jedis` , налаштовується список учасників сервера, запускається підтримка подій, що стаються в світі, піднімається інстанс бази даних, підвантажуються дані з файлу конфігурації, перевіряється вже наявність гравців на сервері.

Далі керування світом передається на 2 основні потоки: клас `Waiting` , що спостерігає за кількістю гравців щосекунди в очікуванні запустити гру, та події, що описані в коді програми. Більшість подій під час очікування заблокована. Коли час очікування спливає, керування переходить до класу `Game` , що містить в собі основні компоненти гри: гравці, встановлені блоки, заборонені локації для блоків, таймер фаз, масив NPC, масив командних сховищ, масив меню, поточний час гри, кількість мертвих команд та інше. Коли починається гра, генеруються всі NPC на карті, запускається ігрове бічне вікно, створюються літаючі блоки над генераторами, встановлюються заборонені території для встановлення блоків, гравці телепортуються зі світу очікування в ігровий світ, оновлюється список гравців, перевіряються пусті команди і знищуються їхні ліжка.

Після того як почалася гра, працюють тільки періодичні класи (ті, які самі генерують виклик себе протягом певного проміжку часу), наприклад клас `Time` , що оновлює фази, клас `ArmorStandsManager` , що оновлює положення літаючих блоків, клас `SpawnResources` , що генерує матеріали на підлозі, та хендлери подій, що виникають протягом гри та змінюють поведінку інших класів.

Після цього очікується виклик методу `.finishGame()` в класі `Time`, після чого перезапускається світ та перестворюються класи.

# Контент

Види матеріалів:

![image](https://github.com/user-attachments/assets/666d7e46-9041-4c77-9bd7-d430cf42fbd6)

Вигляд бічного вікна бару:

![image](https://github.com/user-attachments/assets/ad62f9b6-7a36-4ef8-85b4-c51631cb9121)

Вигляд літаючих блоків для генерації діамантів:

![image](https://github.com/user-attachments/assets/9afb15ff-66d9-4afd-aa2c-7d4bfd9c2e35)

Вигляд генерації:
![image](https://github.com/user-attachments/assets/d1c6f89d-d764-4768-9303-2f19ed2e5542)

Вигляд меню магазину:

![image](https://github.com/user-attachments/assets/e7468c15-a488-42c6-be92-c8c2bb029c1c)
![image](https://github.com/user-attachments/assets/0383d7df-bbc7-4ff1-9d3a-98a9a0ec10ed)

Вигляд зміни фази:

![image](https://github.com/user-attachments/assets/8332a4a7-408e-4223-8687-3e8941edadf0)

Вигляд подій з гравцем:

![image](https://github.com/user-attachments/assets/e5a366bb-5bfe-44b8-b328-61a231ca0e4d)

Покращення команди:

![image](https://github.com/user-attachments/assets/a63c416e-e375-4dab-95c1-416e55218ed2)

NPC і генерація:

![image](https://github.com/user-attachments/assets/507b4ce5-b774-427f-b537-68ddb655d877)

Загалом до формування ігрового контенту (ігрові карти, текстури окремих предметів, набір товарів) долучалося багато інших людей, яким я дуже вдячний за роботу!

# Ігрові механіки

1. Взаємодія між світом та гравцем: встановлення і руйнування блоків, руйнування ліжка команди ворогів, підбирання матеріалів, виклик голема для оборони острова, смерть від падіння, 
2. Взаємодія між гравцем та гравцем: атака різною зброєю, симуляція вбивства і отримання матеріалів з інвентаря, ефект зіллів, використання спільного сховища команди, спілкування в чаті команди та загальному чаті
3. Взаємодія між гравцем та NPC: використання меню, витрачання матеріалів, отримання товарів та розвитку острова, встановлення меню швидкої покупки, вибір команди
4. Взаємодія між гравцем та іншими сутностями: підрив динаміту, виклик і прицілювання вогняного ядра, використання лука та стріл, боротьба з големом, телепортування за допомогою перла Енду