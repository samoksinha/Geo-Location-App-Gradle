Geo-Location-App-Gradle

Geo-Location-App-Gradle for adding shops and find nearest shops

Introduction: Geo Location App is typically provides below functionality: •	User first needs an API key to access the below functionality. API key must be obtained by hitting “getApiKey“ API passing email id as header parameter. •	User can add/modify shop address using this app with latitude and longitude details retrieves from the Google Geo Code API. •	Many concurrent users can simultaneously add/modify shop details using this app. But if two concurrent user try to modify shop details with then only one user at a time can able to modify the shop details. •	User can modify the shop details which some other user previously added then the previous version is updated with the new version and previous version has been returned to the user as response. •	User can search nearest shops by passing his latitude and longitude.

The above functionalities have been achieved using 3 different API. Below are the details of the 3 API’s:

            •	Get API Key: This API creates an authentication token and returned this authentication token as response. The validity of this authentication token is 120 min. 
               	Request Parameter: It requires emailid as header parameter. Using this emaild, authentication token has been generated. And it is used for monitoring purpose for all the API’s.
               	Response Parameter: Status, emailid and apikey has been returned as response parameter.
               	Request Type: user has to generate HTTP GET request for this API.
               	API URL: http://127.0.0.1:7070/gelolocationapp/map/getapikey/v1 (Port can be configured through properties file)
               
           •	Add Shop: This API first validated the authentication key passed as request header and if it is valid then it will take parameters from request body and creates/updates shop details.

               	Request Parameter: It requires authentication parameter from request header and shop details from request body.
               	Response Parameter: Status, and shop details has been returned as response parameter.
               	Request Type: user has to generate HTTP POST request for this API.
               	API URL: http://127.0.0.1:7070/gelolocationapp/map/addshop/v1 (Port can be configured through properties file)
               
           •	Nearest Shop: This API first validated the authentication key passed as request header and if it is valid then it will take parameters from request body to find nearest shop details.

               	Request Parameter: It requires authentication parameter from request header and latitude and longitude details from request body.
               	Response Parameter: Status, and nearest shop details has been returned as response parameter.
               	Request Type: user has to generate HTTP POST request for this API.
               	API URL: http://127.0.0.1:7070/gelolocationapp/map/findnearestshops/v1 (Port can be configured through properties file)
               
Note: All the API’s are Rest bases API and data exchange format is JSON. PORT can be configured through application.properties file bundled with this APP. By default the PORT number is 7070.

Unit Test Cases: 3 Unit test cases have been written to test the above three API’s.

Database: Below are the two tables are created:

          •	transanction_log: This table has been used monitoring purpose. All the request come to the application are logged with necessary details. 
                Note: To identify a request uniquely one unique_log_id parameter has been generated from application and stored in this table. This is useful in multithread environment to identify a request uniquely.

          •	shop_details: This table has been used for storing the Shop Details provided by the user along with latitude and longitude. 
Logging: Log4j has been used for logging framework. User can pass log4j.xml path through command line while executing the Jar file. If not provided through command line then it will pick the bundled log4j.xml from inside the application class path.

          •	Swagger Implementation: Swagger has been integrated for end points documentation and sending request and getting response. Please find below the URL:
                      •	Swagger URL: http://127.0.0.1:7070/gelolocationapp/swagger-ui.html (Port can be configured through properties file)
          
          •	Command: java -Dlog4j.debug -Dlog4j.configuration=file:<Log4j.xml File Path> <Jar File>
