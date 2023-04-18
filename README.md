# Filmorate

## Модель базы данных представлена на ER-диаграмме

---
![Модель базы данных](src/main/resources/DBModel-ER-Diagra.png)

### Примеры запросов в базу данных

---

<details>
  <summary>Получить фильм с id=3</summary>

```sql
    SELECT *
    FROM films
    WHERE film_id = 3;
```

</details>  

<details>
  <summary>Получить пользователя с id=7</summary>

```sql
    SELECT *
    FROM users
    WHERE user_id = 7;
```

</details>  
