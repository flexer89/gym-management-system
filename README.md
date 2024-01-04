## Folder Structure

The workspace contains two folders by default, where:

-  `src_client`: the folder to maintain sources of client

-  `src_server`: the folder to maintain sources of server

-  `lib`: the folder to maintain dependencies

-  `utils`: the folder to maintain utils libraries for borh client and server

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## DOCKER COMPOSE DATABASE ON WINDOWS
- it works kinda weird because dont always delete volume, i think it is because Docker Desktop 'rembers' container and its state
- to fully restart docker compose you need to run `docker compose down -v` and then `docker compose up` or `docker compose up -d` to run it in background

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

- Upload mysql dump - `big_backup.sql` for 5000 users, `backup.sql` for 600 users


## Restoring local db:
### Windows:
- Get-Content backup.sql | docker exec -i mysql mysql -u root -proot GMS
### Linux:
- docker exec -i mysql mysql -u root -proot GMS < backup.sql
## Dumping local db:
- docker exec mysql mysqldump -u root -proot GMS > backup.sql


## Dependency Management

  

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
