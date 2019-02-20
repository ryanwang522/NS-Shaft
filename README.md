# Overview
* This is a classical game that player should keep going down stair for staying alive.
* This project is for practicing `Network` and `Polymorphism` in Java.
* Here is a simple demo for single player mode
* ![](https://github.com/ryanwang522/NS-Shaft/blob/master/demo.gif)

### Network
* Server-client architecture using socket.
* Syncronize the objects (e.g. player, platform) and game information in two computers for dual mode.

### Polymorphism
* Each type of platform extends from the base platform class. 
* Dynamic binding is applied when the player intersect any platform.

# Environment 
* Recommend OS is on windows 10, the application on macOS will be much slower due to the timer issues.
* For sending the serialized object in dual mode correctly, make sure the two computers' java environment is the same. 
* `$ java -version`
```
java version "10.0.1" 2018-04-17
Java(TM) SE Runtime Environment 18.3 (build 10.0.1+10)
Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10.0.1+10, mixed mode)
```

# Reference
* The project is based from [githubalexliu/NS-Shaftt](https://github.com/githubalexliu/NS-Shaftt)
