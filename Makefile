HOST=localhost
PORT=8365
MATCHTYPE=FCFS
SLEEPTIME=1000

all: build

build:
	cd project8 && ant jars

clean:
	cd project8 && ant clean

turnin: clean
	turnin -v -c cs180=COMMON -p project8 project8

run-server: build
	java -jar project8/build/jar/Server.jar $(PORT) $(MATCHTYPE) $(SLEEPTIME)

run-request: build
	java -jar project8/build/jar/Request.jar $(HOST) $(PORT)

run-response: build
	java -jar project8/build/jar/Response.jar $(HOST) $(PORT)
