# 205832447
# kremizn

compile: bin
	find src -name "*.java" > sources.txt
	javac -cp biuoop-1.4.jar:. -d bin @sources.txt
run:
	java -cp biuoop-1.4.jar:bin:resources Ass7Game 
jar:
	jar cfm space-invaders.jar Manifest.mf -C bin . -C resources .
bin:
	mkdir bin