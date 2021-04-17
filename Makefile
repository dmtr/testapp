PORT?=8080

.PHONY: build run


all: build run
build:
	lein uberjar
run: 
	java -jar target/uberjar/testapp-0.1.0-SNAPSHOT-standalone.jar $(PORT)
