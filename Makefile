target = gameoflife

all:
	g++ src/$(target).cpp -o $(target).exe

clean:
	rm -f $(target).exe
