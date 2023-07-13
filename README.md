# Musify

Project to get information on Artists and their work!

# Technology Stack
`Java 17`

`Spring 3.1.1 -> Used Springboot for application development`

`Docker -> Used as a container to deploy docker images`

`Maven 3.9.2 -> Used for building application with different profiles`

`Redis -> Used for caching`

`Resilience4j -> CircuitBreaker for external API calls and Retry for retrying in case of certain exceptions`

`Actuator -> Used to expose operational and monitoring information about a live Spring boot application`

`Micormeter/Zipkin -> Used to add TraceId/SpanId in loggers`

`OpenAPI/Swagger -> Used for Rest API Documentation`

`Lombok -> Used for reducing bilerplate code`

`AWS Lambda -> Used to create lambda function and deployed for serverless execution` 

# Clone the project to your local directory

`git clone https://github.com/anupambabar/musify.git`

# Select Branch

`git checkout development`

# Run Tests :

`mvn clean test`

# Test cases included
`Standalone test cases -> MusifyApplicationTests.java, ArtistControllerTest.java, ArtistDetailsServiceTest.java, RestExceptionHandlerTest.java`

`Integration test case -> ArtistControllerIntegrationTest.java`

# Build your project :

`mvn clean install`

# Run your project in local machine (Windows) :

`  1. mvn spring-boot:run`

`  2. URL to hit -> http://localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e`

`  3. Swagger URL to test Rest API -> http://localhost:8081/swagger-ui/index.html#/artist-controller/getArtist`

`  4. Spring Actuator URLs are available on Swagger dashboard`

`  5. OpenAPI URL for Documentation -> http://localhost:8081/musify-openapi-docs`

# build docker container:

Assuming you have docker installed on your system, navigate to root directory of project and execute command  
`docker build -f Dockerfile -t musify:test .`

# Run the container:

`docker run -p 8081:8081 musify:test`

# Check if the API came up

`localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e`   

# Docker stop Container

List all running contaniers  
`docker ps`  
Stop the container that you want  
`docker stop musify`  

# Sample MBIDs to Test
`65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab`

`410c9baf-5469-44f6-9852-826524b80c61`

`f27ec8db-af05-4f36-916e-3d57f91ecf5e`

# Sample Test Request
'http://localhost:8081/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e'

# Sample Test Response
'{
    "mbid": "f27ec8db-af05-4f36-916e-3d57f91ecf5e",
    "name": "Michael Jackson",
    "gender": "Male",
    "country": "US",
    "disambiguation": "“King of Pop”",
    "description": "<p><b>Michael Joseph Jackson</b> was an American singer, songwriter, dancer, and philanthropist. Known as the \"King of Pop\", he is regarded as one of the most significant cultural figures of the 20th century. During his four-decade career, his contributions to music, dance, and fashion, along with his publicized personal life, made him a global figure in popular culture. Jackson influenced artists across many music genres; through stage and video performances, he popularized complicated dance moves such as the moonwalk, to which he gave the name, as well as the robot.</p>",
    "albums": [
        {
            "id": "97e0014d-a267-33a0-a868-bb4e2552918a",
            "title": "Got to Be There",
            "imageUrl": "http://coverartarchive.org/release/7d65853b-d547-4885-86a6-51df4005768c/1619682960.jpg"
        },
        {
            "id": "51343255-0ad3-3635-9aa2-548ba939b23e",
            "title": "Ben",
            "imageUrl": "http://coverartarchive.org/release/cf81f5db-6b4d-493b-8f8f-c0f8c51442f9/11670488852.jpg"
        },
        {
            "id": "06b064b9-01e7-32d8-b585-86404584e795",
            "title": "Music & Me",
            "imageUrl": "http://coverartarchive.org/release/7c73f72d-8fa2-45a7-9125-a04696f64f3a/1620517729.jpg"
        },
        {
            "id": "50b9d7de-9124-33c0-83a3-76722bf346e5",
            "title": "Forever, Michael",
            "imageUrl": "http://coverartarchive.org/release/3fdd7c32-2da8-480c-8b70-1c628a7fd009/1619702784.jpg"
        },
        {
            "id": "ee749c63-5699-38e0-b565-7e84414648d9",
            "title": "Off the Wall",
            "imageUrl": "http://coverartarchive.org/release/6258e39d-bef4-4d5a-a654-440cf4c4c29a/5349015874.jpg"
        },
        {
            "id": "f32fab67-77dd-3937-addc-9062e28e4c37",
            "title": "Thriller",
            "imageUrl": "http://coverartarchive.org/release/e1b94ba6-c63c-4c2d-8928-9d1a525b7000/22018478497.jpg"
        },
        {
            "id": "a5711a77-42d1-3f4c-830c-e27a96f0800f",
            "title": "Bad",
            "imageUrl": "http://coverartarchive.org/release/60b529f1-f99b-499f-9b3d-e96f9971039e/17067123637.jpg"
        },
        {
            "id": "d6b52521-0dfa-390f-970f-790174c22752",
            "title": "Dangerous",
            "imageUrl": "http://coverartarchive.org/release/45417dcf-d97a-4f36-b729-441846bda882/8294209818.jpg"
        },
        {
            "id": "c24c5313-da47-3155-8277-a6a1a4237966",
            "title": "Invincible",
            "imageUrl": "http://coverartarchive.org/release/7bca6827-3f52-4b25-95cb-046ba5b84d7b/20630400651.jpg"
        },
        {
            "id": "90915175-cc35-3970-99f5-8f279ab59585",
            "title": "The Best of Michael Jackson",
            "imageUrl": "http://coverartarchive.org/release/56274aa3-457e-49b6-ad61-46df84cef737/18821116150.jpg"
        },
        {
            "id": null,
            "title": null,
            "imageUrl": null
        },
        {
            "id": "0621bd78-b867-39ab-8606-9636bfd94447",
            "title": "One Day in Your Life",
            "imageUrl": "http://coverartarchive.org/release/f038c12e-3ff2-4676-b51a-93f168edb50c/31149177185.jpg"
        },
        {
            "id": "54e774a0-7091-31f6-8765-9de5735fcbb1",
            "title": "18 Greatest Hits",
            "imageUrl": "http://coverartarchive.org/release/fd183228-0883-4805-82b6-4e5a64f8565c/33410663685.jpg"
        },
        {
            "id": "22fc8d45-6802-46f8-8a78-6ae823a9ed92",
            "title": "14 Greatest Hits With the Jackson 5",
            "imageUrl": "http://coverartarchive.org/release/f1c43fd5-066d-462a-93fa-33d7bb23564d/1613886666.jpg"
        },
        {
            "id": "ffc3f8b5-7b22-40ed-8867-0cad52b6b2ae",
            "title": "18 Greatest Hits",
            "imageUrl": "http://coverartarchive.org/release/75276adf-7504-4bba-8630-631ef020c31b/1871938266.jpg"
        },
        {
            "id": "6f33ff5d-025a-42d6-827e-6d5bb5a30b4a",
            "title": "18 Greatest Hits",
            "imageUrl": "No cover art found"
        },
        {
            "id": "e6696f23-a356-4cff-a096-fdf2a1e1a358",
            "title": "Great Songs and Performances That Inspired the Motown 25th Anniversary Television Special",
            "imageUrl": "http://coverartarchive.org/release/d77fe3d8-8a4b-4849-87d3-dabfb9f75e44/1619672416.jpg"
        },
        {
            "id": "500d9b05-68c3-3535-86e3-cf685869efc0",
            "title": "Farewell My Summer Love",
            "imageUrl": "http://coverartarchive.org/release/8172928a-a6c7-4d7c-83c8-5db2a4575094/13404444760.jpg"
        },
        {
            "id": "b513c135-b957-305b-9c5c-7f829d6195b3",
            "title": "Got to Be There / Ben",
            "imageUrl": "http://coverartarchive.org/release/f20ad013-43c9-4f91-9a23-741f5c6cff6a/15444575030.jpg"
        },
        {
            "id": "37906983-1005-36fb-b8e7-3a04687e6f4f",
            "title": "Anthology",
            "imageUrl": "http://coverartarchive.org/release/a7a74484-8c25-47e3-9afc-7de701ad3dde/1619836290.jpg"
        },
        {
            "id": "6a427340-2d07-45b5-b557-aaaab91357fd",
            "title": "The 12″ Tape",
            "imageUrl": "http://coverartarchive.org/release/9d647919-ce5e-46ea-92bd-5d87c5b82012/28872180093.jpg"
        },
        {
            "id": "5baedc41-91da-44c9-8289-6619a853e635",
            "title": "Love Songs",
            "imageUrl": "http://coverartarchive.org/release/f91ef9f3-f203-4951-af9a-4e1fa20a1f0d/1619530618.jpg"
        },
        {
            "id": "60005e6f-2299-3a22-a928-e8002b91e834",
            "title": "The Michael Jackson Mix",
            "imageUrl": "http://coverartarchive.org/release/f9ede3a4-95e3-44a2-8502-53b8992fdf70/1620171628.jpg"
        },
        {
            "id": "f674b393-2e2a-3008-aca3-2f5a115ebe31",
            "title": "Instrumental Version Collection",
            "imageUrl": "http://coverartarchive.org/release/044a3fdc-b591-4776-acef-bb3dc70b70cb/1619551610.jpg"
        },
        {
            "id": "30abac22-0c88-3340-b955-61dd3be73c55",
            "title": "The Original Soul of Michael Jackson",
            "imageUrl": "http://coverartarchive.org/release/7aad4ef7-06e9-4a6e-8ae2-205a568c7ca6/1513880900.jpg"
        }
    ]
}'
