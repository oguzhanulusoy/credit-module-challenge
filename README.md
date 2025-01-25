## Credit Module Challenge

- Inspired by Swagger API docs style & structure: http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------------------------

#### User

<details>
 <summary><code>POST</code> <code><b>/</b></code> <code>To create user</code></summary>

##### Parameters

> | name       |  type     | data type | description                                                           |
> |------------|-----------|-----------|-----------------------------------------------------------------------|
> | username   |  required | string    | N/A  |
> | ---------- |-----------|-----------|-----------------------------------------------------------------------|
> | password   |  required | string    | N/A  |
> | ---------- |-----------|-----------|-----------------------------------------------------------------------|
> | email      |  required | string    | N/A  |


##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `201`         | `text/plain;charset=UTF-8`        | `Configuration created successfully`                                |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |
> | `405`         | `text/html;charset=utf-8`         | None                                                                |

##### Example cURL

> ```javascript
>  curl -X POST -H "Content-Type: application/json" --data @post.json http://localhost:8889/
> ```

</details>

