BeerTag enables the users to manage all the beers that they have drunk and want to drink. Each beer has detailed information about it from the ABV (alcohol by volume) to the style and description. Data is community driven, and every beer lover can add new beers. Also, BeerTag allows you to rate a beer and see average ratings from different users. The public part is accessible without authentication i.e., for anonymous users. Anonymous users are able to browse all beers and their details. They are also able to filter by name, ABV, country and style, and sort by name, ABV and rating. Anonymous users have the ability to register and login. Registered users are able to create new beers, add a beer to their wish list and drunk list and rate beers. They are able to edit and delete their own beers. Registered users are able to modify their personal information as well. Administrators are the only ones able to edit/delete all beers and users. Administrators can modify breweries, and styles. The REST API provides the following capabilities:

1. Countries

· Read operations (must)

· Create, Update, Delete operations (should)

2. Breweries

· Read operations (must)

· Create, Update, Delete operations (should)

3. Styles

· Read operations (must)

· Create, Update, Delete operations (should)

4. Beers

· CRUD operations (must)

· Filter by name, ABV, country and style (must)

· Sort by name, ABV, rating (must)

· Rate beer (must)

5. Users

· CRUD operations (must)

· Add beer to wish list (must)

· Get wish list beers (must)

· Add beer to drunk list (must)

· Get drunk list beers (must)
