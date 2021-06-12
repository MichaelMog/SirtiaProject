# OCSF Mediator Example

## Structure
Pay attention to the three modules:
1. **client** - a simple client built using JavaFX and OCSF. We use EventBus (which implements the mediator pattern) in order to pass events between classes (in this case: between SimpleClient and PrimaryController.
2. **server** - a simple server built using OCSF.
3. **entities** - a shared module where all the entities of the project live.

## Running
1. Create a docker image.
2. Run Maven install **in the parent project**. (Create new goal "clean install" on the entire folder)
3. Run the server using the exec:java goal in the server module. (Create goal "exec:java" only on the server folder)
4. Run the client using the javafx:run goal in the client module. (Create goal "javafx:run" only on the client folder)
5. Press the button and see what happens!

## Creating a docker image
Lab 6 recording shows how it's done around minute 25. The gist of it:
1. Make sure you have Docker Desktop on your computer and "docker" recognized as a command prompt command.
2. Run in the command prompt: docker run --name demo-mysql -e MYSQL_ROOT_PASSWORD=ppppassword -p 3306:3306 -d mysql:8.0
3. Run in the command prompt: docker exec -it demo-mysql mysql -u root -p
4. After logging on, you can see databases by running "show databases;". Create a database called example: "create database MovieDb;".
5. Change the hibernate properties password to match the one chosen when downloading Docker Desktop.
6. If needed, change other properties to match the database name you chose.

You're now set (haha probably not. but the last sentence might help you, and for sure the nice people on stack overflow answered it for someone else 7 years ago).
Run "use database MovieDb;" and you can see tables by "show tables;".
To see a table's contents, run "select * from {table_name};".

Step 2 could be problematic. If it says the port it already used, make sure Docker Desktop doesn't have another image of a database open under a different name on the same port. You may have to go to the task manager and stop the process "mysqld.exe".
