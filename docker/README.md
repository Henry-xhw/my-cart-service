This project is to docker image for integration test.

1. How to build image: cd docker && ./build.sh
2. How to run image: ./run.sh
3. How to attache to the container: docker exec -it cart_service bash

Current implementation is to use the exported docker/db_init_script.sql to setup the initial schema. Will update the initial mechanism to run all scripts in KitManager folder after DBE dump all schemas from prod.

Dev local test can run your local sql in the running container and connect by port 1434 with username/pwd: sa/123!P01@W