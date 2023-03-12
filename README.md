# HomeFinder-Server
Bienvenue dans le README de HomeFinder-Server, l'application de serveur qui fournit une API pour permettre à des utilisateurs de chercher des maisons disponibles pour la location ou l'achat. Cette application est conçue pour être utilisée en tandem avec HomeFinder, une application mobile.

Endpoints
Les endpoints sont les URLs que vous pouvez appeler pour interagir avec l'API. Voici les endpoints disponibles dans HomeFinder-Server:

## Register [POST]
```shell
https://homefinder-ktor-server.herokuapp.com/auth/register
```
Cet endpoint permet à un utilisateur de créer son compte en fournissant des informations telles que le sername,firstname, lastname, password et email.
```json
{
    "username" : "john_doe",
    "password" : "JohnDoe",
    "email" : "johndoe@gmail.com",
    "firstName": "John",
    "lastName" : "Doe"
}
```

## Login [POST]
```shell 
https://homefinder-ktor-server.herokuapp.com/auth/login
```
Cet endpoint permet à un utilisateur de se connecter en fournissant leur adresse email et leur mot de passe. Si les informations d'identification sont correctes, un jeton d'accès est renvoyé en tant qu'objet JSON avec le champ token.
### Exemple Requête :
```json
{
    "username" : "john_doe",
    "password" : "JohnDoe"
}
```
### Exemple Resultat :
```json 
{
    "token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJIb21lRmluZGVyQXVkaWVuY2UiLCJyb2xlIjoiVVNFUiIsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwODAiLCJleHAiOjE2Nzg2MTIxNjEsInVzZXJJZCI6IjY0MDQ2OThjNWVlNzc5MDI1Njg3NjMwZCJ9.y7iUfkLgldDAl3sYHwpSqmUn2RNsNQIVFmtSx-bguhk"
}
```

## Houses [GET]
```shell 
https://homefinder-ktor-server.herokuapp.com/api/v1/houses
```
Cet endpoint renvoie toutes les maisons disponibles dans la base de données. Les maisons sont renvoyées en tant qu'objet JSON avec les champs suivants: id, title, address, description, price, type, bedrooms, bathrooms, owner et images.
`Bearer` associé dans le Header de la requête avec le token reçu lors de l'authentification, on peut à présent voir l'ensemble des maisons.

Exemple de résultat:
```json
{
	"success": true,
	"message": "OK",
	"prevPage": 1,
	"nextPage": 3,
	"data": [
		{
			"id": "640d727e3ab37e185f9bc984",
			"description": "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
			"images": [],
			"price": 98500,
			"address": {
				"id": "6409597e47767d7fef95d232",
				"number": "2501",
				"street": "Boulevard des Forges",
				"city": "Trois-Rivières",
				"province": "Québec",
				"postalCode": "G8Z 3L9",
				"country": "Canada"
			},
			"bedrooms": 3,
			"bathrooms": 1,
			"area": 1500,
			"type": "CONDO",
			"yearBuilt": 2000,
			"pool": false,
			"owner": {
				"id": "640d727d3ab37e185f9bc983",
				"username": "Jonathan Sillon",
				"firstName": "Jonathan",
				"lastName": "Sillon",
				"email": "jonathansillon@gmail.com",
				"phone": "819 111 1111"
			}
		}
	],
	"lastUpdated": 1678610362309
}
```

Vous avez la possibilté de determiner la page et la limite sur la requête comme ceci : 
```shell 
https://homefinder-ktor-server.herokuapp.com/api/v1/houses?page=2
```

```shell 
https://homefinder-ktor-server.herokuapp.com/api/v1/houses?page=2&limit=5
```
