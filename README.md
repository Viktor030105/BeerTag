# Spring boot Project "BeerTag"

## Overview

BeerTag enables your users to manage all the beers that they have drunk and want to drink.
Each beer has detailed information about it from the ABV (alcohol by volume) to the style and who created it. 
Data is community driven, and every beer lover can add new beers or update his old ones. 
Of course, there are different users with all the information about them - first and last name, username, email and password. 
You can see this user info and your own wishlist. 
There are also admins with some special abilities such as deleting / editing users and beers 


---
<br>

## Features

- Log in
- Browse all beers
- Filter beers by name, ABV and style, and sort them by name and ABV
- Create new beers
- Add beer to your wishlist
- Edit and delete own beers
- Administrators are able to edit/delete all beers and users


---
<br />

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

## Technologies Used

- Java
- Spring
- SQL
- Database design   


---
<br />

## Installation

Follow these steps to set up and run the application:

1. Clone the project on your IDE
2. Connect to a local server and insert the script and data into a database
2. Run the application
2. Open the browser and make different querries 


---
<br />

## Database Diagram

![database_diagram_img](https://github.com/user-attachments/assets/a6d7d268-ba8d-4ba9-b370-27a295d55c0e)


---
<br>

## Contributors
{{For further information, please feel free to contact me:}}

| Authors         | Emails               | GitHub                           |
| --------------  | -------------------  |--------------------------------  |
| Viktor Angelov  | vikotor05@gmail.com  | https://github.com/Viktor030105  |


---
<br />
