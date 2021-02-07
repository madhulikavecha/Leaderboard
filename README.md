# G-Loot code test for backend developers

## Things you will need
* Git
* Java 13
* A code editor

## General info
* Written using [spring boot](https://spring.io/projects/spring-boot)
* Uses [h2](http://www.h2database.com) as database
* Uses [lombok](https://projectlombok.org) which needs to be enabled/configured in your editor 
* Database migrations done with [flyway](https://flywaydb.org)
* Tests done in [JUnit 5](https://junit.org/junit5/)
* Maven wrapper is included for building/testing
  * On Unix systems use:
  `./mvnw clean verify`
  * On Windows:
  `./mvnw.cmd clean verify`

## Test
This is a sample spring boot which currently only offers a single GET endpoint at `/api/v1/leaderboard`
which will return a leaderboard consisting of entities representing players and their score.

We expect you to perform the following list of tasks:
* Extend functionality
    * Add support for multiple leaderboards
    * Add new API endpoints for:
        * For fetching the position of a user for a specific leaderboard
        * Setting score for a user in a leaderboard
    * Write tests for the code you add
* Refactor the method in LeaderboardService

## Sending in your submission
Alternatives:
* Clone the repository, make your changes and e-mail an archive with the result to [codetest@gloot.com](mailto:codetest@gloot.com?subject=Spring%20Boot%20Code%20Test)
* Fork the repository on github and just link us the fork with the changes

Including a commit history is a plus which would showcase your thought process

## solution
Here is the leaderboard application providing REST API to create users and update the scores.
Leaderboard will return the top user with the highest score from each country.

##Install :
mvn clean
mvn install

## Run the app:
curl -X GET http://localhost:8080/api/v1/leaderboard

## Run the test : 
mvn -q test

###Get leaderboard - List of top users from each country
curl -i  http://localhost:8080/api/v1/leaderboard
###Response
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 07 Feb 2021 19:15:42 GMT

[{"position":1,"nick":"glooter","username":"asd","score":9999,"country":"usa"},
{"position":2,"nick":"madhuEU","username":"deepal","score":111,"country":"eu"},
{"position":3,"nick":"myu8sw40253","username":"myusername1","score":0,"country":"sweden"}]

### Get leaderboard from all countries
curl -i  http://localhost:8080/api/v1/leaderboard/sweden
###Response
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 07 Feb 2021 19:15:42 GMT

[{"position":1,"nick":"myu8sw40253","username":"myusername1","score":0,"country":"sweden"},
{"position":2,"nick":"myu9sw32432","username":"myusername1","score":0,"country":"sweden"},
{"position":3,"nick":"myu10sw88068","username":"myusername1","score":0,"country":"sweden"}]

### Here is the response when there are no users in the specified  country 
curl -i  http://localhost:8080/api/v1/leaderboard/country/africa
##Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 51
Date: Sun, 07 Feb 2021 19:19:49 GMT

please select valid country among [eu, sweden, usa]

### Get users from all the countries
curl -i  http://localhost:8080/api/v1/leaderboard/allusers
### Response
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 07 Feb 2021 19:23:00 GMT

[{"position":1,"nick":"glooter","username":"asd","score":9999,"country":"usa"},{"position":2,"nick":"madhu3","username":"sofia","score":500,"country":"usa"},
{"position":3,"nick":"madhu2","username":"eric","score":111,"country":"usa"},{"position":4,"nick":"madhuEU","username":"deepal","score":111,"country":"eu"}
,{"position":5,"nick":"madhu","username":"johan","score":100,"country":"usa"},{"position":6,"nick":"myu8sw40253","username":"myusername1","score":0,"country":"sweden"},
{"position":7,"nick":"myu9sw32432","username":"myusername1","score":0,"country":"sweden"},{"position":8,"nick":"myu10sw88068","username":"myuser
name1","score":0,"country":"sweden"},{"position":9,"nick":"madhu5","username":"rohan","score":-1,"country":"usa"},{"position":10,"nick":"maEU4","username":"mania","score":-1,"country":"eu"}]


### Create a new user :
curl -i -d "{"""username""":"""myusername1""","""country""":"""sweden"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboard/createuser
### Response
HTTP/1.1 201
Content-Type: text/plain;charset=UTF-8
Content-Length: 66

New User is saved with userName, userId - myusername1, myu11sw4279

### Update user score :
curl -i -d "{"""username""":"""tomas""","""score""":"""1000"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboard/updatescore
### Response:
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 112

User Score is updated User , Score :  tomas ,1000

### Update user profiles with multiple accounts having the same username:
curl -i -d "{"""username""":"""myusername1""","""score""":"""1000"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboard/updatescore
### Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 112
Date: Sun, 07 Feb 2021 18:55:34 GMT

There are several accounts created with this username. Please provide a unique userId for username - myusername1 

### NEED TO PROVIDE NICK IN THIS CASE:
curl -i -d "{"""nick""":"""myu11sw4279""","""score""":"""1000"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboard/updatescore
### Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 112

User Score is updated User , Score :  myu11sw4279 ,1000

### Delete user 
curl -i -d "{"""username""":"""tomas"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboad/delete
### Response
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Content-Length: 32
Date: Sun, 07 Feb 2021 19:10:43 GMT

The user tomas  has been deleted


### Delete user profiles with multiple accounts having the same username
curl -i -d "{"""username""":"""myusername1"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboad/delete
### Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 112
Date: Sun, 07 Feb 2021 19:05:29 GMT

There are several accounts created with this username. Please provide a unique userId for username - myusername1
### NEED TO PROVIDE NICK IN THIS CASE
curl -i -d "{"""nick""":"""myu11sw4279"""}" -H "Content-Type:application/json"  http://localhost:8080/api/v1/leaderboard/updatescore
### Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 112

The user myu11sw4279  has been deleted

### Get position of user based on country
curl -i  http://localhost:8080/api/v1/leaderboard/position/eric/usa
### Response
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Content-Length: 55
Date: Sun, 07 Feb 2021 19:24:34 GMT

The position of user ( eric ) in usa leaderboard is   3

### Following is the case when the user is not available in specified country
curl -i  http://localhost:8080/api/v1/leaderboard/position/username1/usa
### Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 100
Date: Sun, 07 Feb 2021 19:26:02 GMT

Please provide the valid username. 'username1'  is not found in our records.

### Get position of the user profile with multiple accounts having the same username
curl -i http://localhost:8080/api/v1/leaderboard/position/johan/usa
### Response
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 106
Date: Sun, 07 Feb 2021 19:29:52 GMT

There are several accounts created with this username. Please provide a unique userId for username - johan
### NEED TO PROVIDE NICK IN THIS CASE
curl -i http://localhost:8080/api/v1/leaderboard/position/joh10us37531/usa
### Response
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Content-Length: 63
Date: Sun, 07 Feb 2021 19:31:25 GMT

The position of user ( joh10us37531 ) in usa leaderboard is 4