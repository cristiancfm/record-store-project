# record-store-project
This application can manage artists and their respective titles to be used in a record store.
Developed as a project for the Programming III subject in the University of West Timisoara (UVT).
Created with JavaFX and JPA (Hibernate).

![imagen](https://user-images.githubusercontent.com/72354794/148654375-8861be42-2992-416b-8edc-d0b479ff2647.png)

# Running the Application
To run this app, you need a previously created database in MySQL. Create a schema with the
name "recordstore" using MySQLWorkbench and run the provided script (record-store.sql).

You can change the username and password used to access the database in the persistence.xml file
(in the folder src/main/resources/META-INF).

You can also add the command argument "--add-examples" when running the application to add a few
examples to the database.