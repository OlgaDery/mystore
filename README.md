# mystore
Example of a small web application using Maven, Apache Derby database, JPA, Servlet, EJB and JMS APIs.

## Requirements:
- Eclipse Mars with Java SE 8;
- WildFly 9.0.2 or 10.0.0;
- Apache Derby 10.12.1.1.

## How to deploy:
- Clone the project on your machine.
- Extract it and import in Eclipse as an existing Maven project.
- Remove _standalonefull.xml_ from the project files and put it into the folder with the configuration files of the web server you intend to use.
- Configure this web server in Eclipse using the _standalonefull.xml_ you have saved.
- Start the Derby server on your mashine.
- Create a new database connection in your workspace for Derby. Create a new database called _MyStoreDB_. Connect to this database.
- Add the _mystore.ear_ file to the list of deployements of your web server and run the web server. Then, full publish the _mystore.ear_. The table in the database will be created automatically.
- In the browser, go to _http://localhost:8080/mystore-web/createCustomer.jsp_. Return to this page every time when you want to test the next piece of functionality.
 
