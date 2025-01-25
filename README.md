## Credit Module Challenge

- Inspired by Swagger API docs style & structure: http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------------------------

#### User

<details>
 <summary><code>POST</code> <code><b>/</b></code> <code>To create user</code></summary>

##### Example cURL

> ```javascript
>  curl --location 'localhost:8080/user/create' --header 'Content-Type: application/json' --header 'Accept-Language: tr' --data-raw '{"username": "user1_tc","password": "password","email": "user1@email.com"}'
> ```

</details>

<details>
 <summary><code>POST</code> <code><b>/</b></code> <code>To authentication</code></summary>

##### Example cURL

> ```javascript
> curl --location 'localhost:8080/user/auth' --header 'Content-Type: application/json' --header 'Accept-Language;' --data '{ "username": "user1_tc", "password": "password" }'
> > ```

</details>

<details>
 <summary><code>GET</code> <code><b>/</b></code> <code>To list users</code></summary>

##### Example cURL

> ```javascript
> curl --location 'localhost:8080/user/list' --header 'Content-Type: application/json' --header 'Accept-Language;' --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJ1c2VyMV90YyIsImlhdCI6MTczNzgyODg4NCwiZXhwIjoxNzY5MzY0ODg0fQ.d89CdxEy4b4Ux2XJTPSpiBC1hDAau2MbZK5tpU49crg'
> ```

</details>
