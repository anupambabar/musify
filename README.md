# Musify

Project to get information on Artists and their work!

# Clone the project to your local directory

`git clone https://github.com/anupambabar/musify.git

# Select Branch as per below functionality

`For Rest Template Implementation -> git checkout feature/resttemplate`
`For WebClient Implementation -> git checkout feature/webclient`
`For Rest Template with AWS Labmda (Serverless) Implementation -> git checkout feature/awslambda`

# Build your project :

`mvn clean install`

# Run your project in local machine (Windows) :

`For feature/resttemplate and feature/webclient branches`
`  1. mvn spring-boot:run`
`  2. URL to hit -> http://localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e`
`  3. Swagger URL to test Rest API -> http://localhost:8081/swagger-ui/index.html#/artist-controller/getArtist`
`  4. OpenAPI URL for Documentation -> http://localhost:8081/musify-openapi-docs`

`For feature/awslambda branch`
`  1. To run the local profile -> mvn spring-boot:run -Dboot`
`  2. URL to hit -> http://localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e`
`  3. Swagger URL to test Rest API -> http://localhost:8081/swagger-ui/index.html#/artist-controller/getArtist`
`  4. OpenAPI URL for Documentation -> http://localhost:8081/musify-openapi-docs`

# build docker container:

Assuming you have docker installed on your system  
`docker build -t <containerName>:<tag>.`

# Run the container:

`docker run -p 8080:8080 <containerName>:<tag>`

# Check if the API came up

`192.168.99.100:8080/musify/music-artist/details`   
This IP is provided by docker when it runs. Most cases its this same one.

# Docker stop Container

list all running contaniers  
`docker ps`  
stop the container that you want  
`docker stop <containerName>:<tag>`  
