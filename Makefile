PORT?=8080

.PHONY: build run test


all: build run
build:
	lein uberjar
run: 
	java -jar target/uberjar/testapp-0.1.0-SNAPSHOT-standalone.jar $(PORT)
test:
	lein test
