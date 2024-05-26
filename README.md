# -- CRUD API With Ratpack --
###### _Created by : Muhamad Ilfani M_
##### _________________________________________________________________
## Brief Explanation
This project use Java with Ratpack Library. Ratpack is a set of Java libraries that facilitate fast, efficient, evolvable and well tested HTTP applications. It is built on the highly performant and efficient Netty event-driven networking engine.

## Features

- API to Create Product
- API to Get List Product with pagination and Sorting
- API to Update Product
- API to Delelete Product

## Tech
This project using below technologies to implement:

- Java - Java Programming Language
- [Ratpack] - Java libraries for building scalable HTTP applications
- Guice - Dependency Injection
- [PostgreSQL] - The World's Most Advanced Open Source Relational Database
- [Redis] - The world’s fastest in-memory database

## Project Structure
Below is the structure code for this project

```sh
├── README.md
├── src/main/java/my/app
│ ├── db
│ ├── redis
│ ├── helpers
│ ├── services
│ │ ├── product
│ ├── models
│ │ ├── product
│ ├── handlers
│ │ ├── product
│ ├── MyModule.java
│ └── Main.java
├── .gitignore
├── .env
└── build.gradle
```

## DB Structure
Create table with name products with column as below:
```sh
CREATE TABLE IF NOT EXISTS products(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity NUMERIC(50, 3) NOT NULL,
    price NUMERIC(50, 3) NOT NULL,
    created_by VARCHAR(255) NOT NULL DEFAULT 'SYSTEM'::character varying,
    created_at timestamp NOT NULL DEFAULT now(),
    modify_by VARCHAR(255) NOT NULL DEFAULT 'SYSTEM'::character varying,
    modify_at timestamp NOT NULL DEFAULT now(),
    deleted_by VARCHAR(255),
    deleted_at timestamp,
);
```

## DB Setting
This project using PostgreSQL with configuration as below:
Setting on .env file based on DB your DB configuration setting
> DB_URL=jdbc:postgresql://{host}:{port}/postgres
> DB_USER_NAME=
> DB_PASSWORD=
> DB_DRIVER=org.postgresql.Driver
> DB_SCHEMA=

## Redis Setting
Setting your redis configuration. Here is the link [How to Set Redis](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/).
Setting .env value based on your redis configuration
> REDIS_HOST=
> REDIS_PORT=

## How to Run
1. Clone this repository
    > git clone https://github.com/muhamad-ilfani/crud-ratpack-java.git
2. Setting your DB connection and match it with .env file. Create table with name "products"
3. Setting your redis connection and match it with .env file
4. Build gradle
    > gradle build
5. Running program
    > gradle run
6. This program with running base on your setting BASE_PORT in .env file
7. Check status with hitting base URL
    > Response : Welcome to my project!
    > Status Code : 200

## API List and Specification
Below is the list and specification of API on this project.
| Function | API |  Notes | 
| ------ | ------ | ------ |
| Get All Products | GET /products?limit=10&page=1&sort=name:desc | This APi is used to get list of products in DB. It can be sorted by id, price, name, quantity and can set the pagination. Default limit is 10, default page is 1 |
| Get Product By ID | GET /product/:id | This APU is used to get product by selected ID. Parameter should be integer and data must be available on DB |
| Create Product | POST /product | This API is used to create product to DB.  Request payload will consist of "name", "quantity", "price"|
| Update Product By ID | PUT /product/update/:id | This API is used to update product data by ID.  Request payload will consist of "name", "quantity", "price"|
| Delete Product By ID | DELETE /product/delete/:id | This API is used to delete product by ID. |

- Get All Products
    There is caching mechanism with key "product_{limit}{page}{sort}". This caching will be invalidate by TTL (30 Minutes) and invalidate for every step when there is update data.

    Query Params
    >Limit: limit=10 (default limit 10)
    > Page: page=1 (default page 1)
    > Sort by id: sort=id:desc or sort=id:desc
    > Sort by name: sort=name:desc or sort=name:desc
    > Sort by quantity: sort=quantity:desc or sort=quantity:desc
    > Sort by price: sort=price:desc or sort=price:desc

    Response (200): 
    ```sh
    {
    "data": [
        {
            "id": 2,
            "name": "Soto Daging Update",
            "quantity": 2,
            "price": 11500
        }
    ],
    "total_entries": 6
    }
    ```
- Get Product By ID
    Enter your ID in params
    Response (200):
    ```sh
    {
    "data": {
        "id": 2,
        "name": "Soto Daging Update",
        "quantity": 2,
        "price": 11500
    }
    }
    ```
    Response (400 Invalid Param):
    ```sh
    {
    "message": "invalid parameter id"
    }
    ```
- Create Product
    Request:
    ```sh
    {
    "name": "Sop Buah 1",
    "quantity": 5,
    "price": 11000
    }
    ```
    Response (201):
    ```sh
    {
    "id": 10,
    "message": "Product inserted successfully"
    }
    ```
- Update Product By ID
    Request:
    ```sh
    {
    "name": "Sop Buah 1",
    "quantity": 5,
    "price": 11000
    }
    ```
    
    Response (200): 
    ```sh
    {
    "message": "Product updated successfully"
    }
    ```
    Response (404 Not Found)
     ```sh
    {
    "message": "Product not found"
    }
    ```
    Response (400 Invalid Param):
    ```sh
    {
    "message": "invalid parameter id"
    }
    ```
- Delete Product By ID
    Response (:
    ```sh
    {
    "message": "Product deleted successfully"
    }
    ```
    Response (404 Not Found):
    ```sh
    {
    "message": "Product not found"
    }
    ```
    Response (400 Invalid Param):
    ```sh
    {
    "message": "invalid parameter id"
    }
    ```

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [How to Set Redis]: <https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/>
   [Redis]: <https://redis.io/>
   [PostgreSQL]: <https://www.postgresql.org/>
   [Ratpack]: <https://ratpack.io/>
