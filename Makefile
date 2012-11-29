all: build

build:
	cd project8 && ant jars

clean:
	cd project8 && ant clean

turnin: clean
	turnin -v -c cs180=COMMON -p project8 project8
