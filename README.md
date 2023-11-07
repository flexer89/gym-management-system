## Folder Structure

The workspace contains two folders by default, where:

  

-  `src_client`: the folder to maintain sources of client

-  `src_server`: the folder to maintain sources of server

-  `lib`: the folder to maintain dependencies

  

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

  

## STARTING LOCAL DB AND CBEAVER

-  `cd database`

-  `docker compose up` or `docker compose up -d` to run it in background

## URLs
- DBeaver: `localhost:8888`

 ## Starting Server
 After starting `docker compose`you can run server app.
 
## Starting Client
Client will only work if server is started

## RESTARTING LOCAL DB

For now data is not persisted, so if you want have clear db schema just restart docker compose

-  `cd database`

-  `docker compose down -v`

-  `docker compose up` or `docker compose up -d` to run it in background

## Dependency Management

  

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
