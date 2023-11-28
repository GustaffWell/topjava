# Get meal
`curl -s http://localhost:8080/topjava/rest/meals/100003`

 # Delete meal
`curl -s -X DELETE http://localhost:8080/topjava/rest/meals/100003`

# Get all meals
`curl -s http://localhost:8080/topjava/rest/meals`

# Create new meal
`curl -s -X POST -d '{"dateTime":"2023-11-27T13:00","description":"Чипсы","calories":500}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava/rest/meals`

# Update meal
`curl -s -X PUT -d '{"dateTime":"2023-11-27T00:00", "description":"Торт (update)", "calories":1000}' -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/topjava/rest/meals/100003`

# Get meals filtered by date and time
`curl -s http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30&endDate=2020-01-31&startTime=00:00&endTime=14:00`
