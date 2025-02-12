# Spring boot Project "BeerTag"

## Overview

BeerTag is a platform for beer enthusiasts to track the beers they have tried and want to explore. Each beer entry includes details such as name, ABV (alcohol by volume) and style. Users can filter and sort beers by name, ABV, and style in ascending or descending order. The database is community-driven, allowing users to add, update, and delete their own beer entries. Each user has a profile with their first name, last name, username, email, and password. Registration and login are required to access the system, and once logged in, users can view other profiles (excluding passwords). Only the creator of a beer entry can edit or delete it, ensuring data integrity.


---
<br>

## Features

- Register
- Log in
- Log out
- Browse all beers
- Filter beers by name, ABV and style
- Sort beers by name, ABV and style in ascending or descending order
- Create new beers
- Edit and delete own beers
- See all users information
- Update or delete your personal information


---
<br>

## Technologies Used

- Java
- Spring Boot / MVC
- SQL 
- HTML
- CSS
- Thymeleaf
- RestAPI


---
<br />

## Installation

Follow these steps to set up and run the application:

1. Clone the project on your IDE
2. Connect to a local server and insert the script and data into a database
2. Run the application
2. Open the browser and search http://localhost:3309/


---
<br>

## Rest API

Users:
1. Get all users         -> /api/users
2. Get user by id        ->  /api/users/{id}
3. View wishlist         -> /api/users/{id}/wish-list
4. Add to wishlist       -> /api/users/{id}/wish-list/{beerId}
5. Remove from wishlist  -> /api/users/{id}/wish-list/{beerId}

Beers:
1. Get all beers   -> /api/beers
2. Get beer by id  -> /api/beers/{id}
3. Create beer     -> /api/beers
4. Update beer     -> /api/beers/{id}
5. Delete beer     -> /api/beers{id}


---
<br />

## Pictures representing the different pages of the project

![user-page](https://github.com/user-attachments/assets/410a4453-0d0f-4402-8293-e72b0942efc3)

![update-user-page](https://github.com/user-attachments/assets/2bff7b8f-d9a4-46ea-88c9-fdd7c164a8fa)

![update-beer-page](https://github.com/user-attachments/assets/d9d7eca6-9ea0-4e0b-8875-60028d167b75)

![page-not-found](https://github.com/user-attachments/assets/2859c8ae-4d1e-4a0f-89c1-dc9727290c44)

![login-page](https://github.com/user-attachments/assets/c7283fe9-6c02-4af7-a884-970ea43dae7a)

![home-page](https://github.com/user-attachments/assets/245d3c3b-4339-45be-a6d7-508d1e914338)

![all-users-page](https://github.com/user-attachments/assets/1254a499-5f94-4149-af5a-7cbe1fc24be6)

![all-beers-page](https://github.com/user-attachments/assets/4a215191-9b25-41ba-8e6d-f04e74533647)

![access-denied-page](https://github.com/user-attachments/assets/0ce0ea34-2376-4944-9255-5fd45b4597af)

![about-page](https://github.com/user-attachments/assets/fd942bd8-a71c-497b-8cce-40abae5728ea)

![register-page](https://github.com/user-attachments/assets/d548c69d-70e1-4548-9653-40ab68b1458f)

![beer-page](https://github.com/user-attachments/assets/6f7676cd-bc8d-4c36-9d80-16af154fa90f)


---
<br />


## Database Diagram

![database_diagram_img](https://github.com/user-attachments/assets/f0b32368-755c-4cb0-9acf-4f985840481f)



<br>

## Contributors
{{For further information, please feel free to contact me:}}

| Authors         | Emails               | GitHub                           |
| --------------  | -------------------  |--------------------------------  |
| Viktor Angelov  | vikotor05@gmail.com  | https://github.com/Viktor030105  |


---
<br />
