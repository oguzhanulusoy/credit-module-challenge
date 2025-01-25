## Credit Module Challenge

- Inspired by Swagger API docs style & structure: http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------------------------

#### User

<details>
 <summary><code>POST</code> <code><b>/</b></code> <code>To create user</code></summary>

##### Example cURL

> ```javascript
>  curl --location 'localhost:8080/user/create' \
--header 'Content-Type: application/json' \
--header 'Accept-Language: tr' \
--data-raw '{
"username": "user1_tc",
"password": "password",
"email": "user1@email.com"
}'
> ```

</details>

