[![Codacy Badge](https://app.codacy.com/project/badge/Grade/048de5a817024c8cbbbf7387ac95a9f2)](https://www.codacy.com/gh/Serheev/restaurant-voting/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Serheev/restaurant-voting&amp;utm_campaign=Badge_Grade)

# TopJava Internship Graduation Project

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

## The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  * If it is before 11:00 we assume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it (**better - link to Swagger**).

-----------------------------
P.S.: Make sure everything works with latest version that is on github :)  
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.
-----------------------------

## REST API documentation:
### Swagger
http://localhost:8080/swagger-ui.html <br>
http://localhost:8080/v3/api-docs

### CURL
#### get All Users (admin only)
`curl -s http://localhost:8080/api/admin/users --user admin@javaops.ru:admin`

#### get User Profile date
`curl -s http://localhost:8080/api/profile --user user@gmail.com:password`

#### register a New User (anonymous only)
`curl -s -X POST -d '{  
"email": "anonymous@email.com",
"firstName": "First Name",
"lastName": "Last Name",
"password": "password",
"date": "2021-12-17",
"roles": [
"USER"
]
}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/profile/register`

#### update User date
`curl -s -X PUT -d '{  
"email": "anonymous@email.ru",
"firstName": "First Name",
"lastName": "Last Name",
"roles": [
"USER"
]
}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/profile/  --user user@gmail.com:password`

#### get All Restaurants
`curl -s http://localhost:8080/api/user/restaurants --user user@gmail.com:password`

#### get Restaurants by id=1
`curl -s http://localhost:8080/api/user/restaurants/1 --user user@gmail.com:password`

#### get All Restaurants by name=meat
`curl -s http://localhost:8080/api/user/restaurants/by-name?name=meat --user user@gmail.com:password`

#### register a New Restaurant (admin only)
`curl -s -X POST -d '{
"name": "My New Restaurant",
"address": "London"
}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants --user admin@javaops.ru:admin`

#### update Restaurant info with id=1 (admin only)
`curl -s -X PUT -d '{
"name": "Kuvshin",
"address": "Paris"
}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/1 --user admin@javaops.ru:admin`

#### get Restaurant Menu with id=1
`curl -s http://localhost:8080/api/user/restaurants/1/today --user user@gmail.com:password`

#### add a New Dish to Menu Today for Restaurant with id=3 (admin only)
`curl -s -X POST -d '{
"dishes": [
{
"name": "Borsch",
"price": 100
},
{
"name": "Varenyk",
"price": 110
}
]
}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/3/today --user admin@javaops.ru:admin`

#### update Dishes list Today for Restaurant with id=1 if it exists only (admin only)
`curl -s -X PUT -d '{
"dishes": [
{
"name": "Borsch",
"price": 100
},
{
"name": "Varenyk",
"price": 110
}
]
}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/3/today --user admin@javaops.ru:admin`

#### delete Menu for the Restaurant with id=2 if it exists only (admin only)
`curl -s -X DELETE http://localhost:8080/api/admin/restaurants/2/today --user admin@javaops.ru:admin`

#### Vote for the Restaurant with id=1
`curl -s -X POST -d -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/user/restaurant/vote?restaurantId=1 --user user@gmail.com:password`

#### change the Vote for the Restaurant with id=2 instead of id=1 (it's available at the same day before 11:00 only, after 11:00 it's too late to change opinion)
`curl -s -X PUT -d -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/user/restaurant/vote?restaurantId=2 --user user@gmail.com:password`