# cartography-Imhof-EPFL
This is a school project I made with my colleague [Clément Nussbaumer](https://github.com/clementnuss)
for an object-oriented programming course taught by Prof. M. Schinz at EPFL Switzerland.
The idea was to combine two types of data; some geographic data coming from the [OpenStreetMaps](http://www.openstreetmap.org)
project and digital terrain elevation data to produce a shaded map in the same style as the swiss national map.

Initially, the rendering of the map used gaussian blur when the resolution (of 1 arc-second) of the digital elevation model was 
unsufficient. We modified it so it now uses Gouraud shading, a technique of bilinear interpolation which gives nice results with
better performance. While at first the software necessited the use of command line, we added a graphical user interface which 
hopefully make things a bit easier on the user side.
