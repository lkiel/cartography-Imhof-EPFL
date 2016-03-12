# cartography-Imhof-EPFL
This is a school project I made with my colleague [Clément Nussbaumer](https://github.com/clementnuss)
for an object-oriented programming course taught by Prof. M. Schinz at EPFL Switzerland.
The idea was to combine two types of data; some geographic data coming from the [OpenStreetMaps](http://www.openstreetmap.org)
project and digital terrain elevation data to produce a shaded map in the same style as the swiss national map.

Guidelines were given during the project as well as classes behaviour description. Our task was to implement the required functionalities in a clean and efficient way. At the end of the semester the possibility was given to enhance the software with additional features or different algorithms to treat the data.

Initially, the rendering of the map used gaussian blur when the resolution (of 1 arc-second) of the digital elevation model was 
unsufficient. We modified it so it now uses Gouraud shading, a technique of bilinear interpolation which gives nice results with
better performance. You can find more on that subject in the wiki of the project. While at first the software necessited the use of command line, we added a graphical user interface which will hopefully make things a bit easier on the user side. Work is still on progress on that feature.

With the sample data provided you should be able to render some maps with the given parameters:

| OSM FILE           | HGT File      | Bottom-left longitude | Bottom-left latitude | Top-right longitude | Top-right latitude |
| :-----------------:|:-------------:|:---------------------:|:--------------------:|:-------------------:|:------------------:|
| lausanne.osm.gz    | N46E006.hgt   | 6.5594                |46.5032               |6.6508               |46.5459             |
| interlaken.osm.gz  | N46E007.hgt   | 7.8122                |46.6645               |7.9049               |46.7061             |
| berne.osm.gz       | N46E007.hgt   | 7.3912                |46.9322               |7.4841               |46.9742             |


------![alt text](https://raw.githubusercontent.com/lkieliger/cartography-Imhof-EPFL/master/illustrations/interface.png "Illustration interface") 
