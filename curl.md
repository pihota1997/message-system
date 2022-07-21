## user registration
curl -X POST http://localhost:9090/api/v1/registration -H "Content-Type: application/json" -d '{ "name": "user", "password": "password"}' -i

## login
curl -X POST "http://localhost:9090/api/v1/login" -H "Content-Type: application/json" -d '{"name": "user", "password": "password"}' -i

## save message
curl -X POST "http://localhost:9090/api/v1/message" -H "Content-Type: application/json" -H "Authorization: Bearer_token" -d '{"name" : "user", "message" : "message"}' -i

## get last 10 messages
curl -X POST "http://localhost:9090/api/v1/message" -H "Content-Type: application/json" -H "Authorization: Bearer_token" -d '{"name" : "user", "message" : "history 10" }' -i