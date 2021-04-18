# testapp


## Usage

Build and run:

    $ make

Run tests:

    $ make test	

## Request examples

Create request:
```
curl -H "Content-Type: application/json" -X POST --data '{"title": "test", "desc": "new request", "reporter": "reporter", "assignee": "assignee", "date": "12-02-2021"}' 'http://localhost:8080/requests'
```

Get requests list:
```curl  'http://localhost:8080/requests?limit=20'```

## Possible enhancements

1. Add Swagger
1. Better validation
1. Better error message
1. Add filters
1. Change database schema: Reporter and Assignee must be independent entities
