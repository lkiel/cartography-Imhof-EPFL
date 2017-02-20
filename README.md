# cartography-Imhof-EPFL
This is a school project I made with my colleague [Clément Nussbaumer](https://github.com/clementnuss) for an object-oriented programming course taught by Michel Schinz at EPFL Switzerland. The idea was to combine two types of data; some geographic data coming from the OpenStreetMaps project and digital terrain elevation data to produce a shaded map in the same style as the swiss national map.

Guidelines were given during the project as well as java classes behaviour description. Our task was to implement the required functionalities in a clean and efficient way. At the end of the semester, the possibility was given to enhance the software with additional features or different algorithms to treat the data.

Initially, the rendering of the map used gaussian blur when the resolution (of 1 arc-second) of the digital elevation model was unsufficient. Therefore, to enhance the project, we modified it so it now uses Gouraud shading, a technique of bilinear interpolation which gives nice results with better performance. You can find more on that subject in the wiki of the project. While at first the software necessitated the use of command line, we also added a graphical user interface which will hopefully make things a bit easier on the user side.

With the sample data provided you should be able to render some maps with the given parameters:

| OSM FILE           | HGT File      | Bottom-left longitude | Bottom-left latitude | Top-right longitude | Top-right latitude |
| :-----------------:|:-------------:|:---------------------:|:--------------------:|:-------------------:|:------------------:|
| lausanne.osm.gz    | N46E006.hgt   | 6.5594                |46.5032               |6.6508               |46.5459             |
| interlaken.osm.gz  | N46E007.hgt   | 7.8122                |46.6645               |7.9049               |46.7061             |
| berne.osm.gz       | N46E007.hgt   | 7.3912                |46.9322               |7.4841               |46.9742             |


![Illustration interface with result](https://raw.githubusercontent.com/lkieliger/cartography-Imhof-EPFL/master/illustrations/result.png "Illustration interface with result") 
![Illustration interface with raw map](https://raw.githubusercontent.com/lkieliger/cartography-Imhof-EPFL/master/illustrations/raw_osm.png "Illustration interface with raw map") 
![Illustration interface with raw shading](https://raw.githubusercontent.com/lkieliger/cartography-Imhof-EPFL/master/illustrations/raw_shading.png "Illustration interface with raw shading") 
