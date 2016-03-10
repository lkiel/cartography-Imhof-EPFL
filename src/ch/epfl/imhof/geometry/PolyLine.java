package ch.epfl.imhof.geometry;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code public abstract class PolyLine}
 * <p>
 * Classe abstraite {@link PolyLine}, mettant en place la structure des
 * polylignes ouvertes ou fermées à partir d'une liste de sommets fournie.
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public abstract class PolyLine implements Serializable {

    private static final long serialVersionUID = 2015052700L;
    transient private final List<Point> vertexList;

    /**
     * {@code public static final class Builder}
     * <p>
     * Bâtisseur imbriqué de la classe {@link PolyLine}
     *
     * @author Clément Nussbaumer (250261)
     * @author Leandro Kieliger (246263)
     */
    public static final class Builder {

        private final List<Point> builderVertexList;

        /**
         * {@code public Builder()}
         * <p>
         * Constructeur du builder
         */
        public Builder() {
            builderVertexList = new ArrayList<>();
        }

        /**
         * {@code public void addPoint(Point newPoint)}
         * <p>
         * Méthode ajoutant un {@link Point} à la polyligne
         *
         * @param newPoint - le point à ajouter
         */
        public void addPoint(Point newPoint) {
            builderVertexList.add(newPoint);
        }

        /**
         * {@code public OpenPolyLine buildOpen()}
         * <p>
         * Méthode construisant une nouvelle OpenPolyLine
         *
         * @return - Objet de type OpenPolyLine
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(builderVertexList);
        }

        /**
         * {@code public ClosedPolyLine buildClosed()}
         * <p>
         * Méthode construisant une nouvelle ClosedPolyLine
         *
         * @return - Objet de type ClosedPolyLine
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(builderVertexList);
        }
    }

    /**
     * Constructeur de la classe PolyLine
     *
     * @param points - Une liste des sommets constituant la polyligne
     * @throws IllegalArgumentException si la liste de sommets est vide
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.isEmpty())
            throw new IllegalArgumentException("La liste des sommets est vide");

        vertexList = Collections.unmodifiableList(new ArrayList<>(points));
    }

    /**
     * {@code public abstract boolean isClosed()}
     * <p>
     * Méthode abstraite retournant vrai ssi la polyligne est fermée
     *
     * @return - vrai si et seulement si la polyligne est fermée
     */
    public abstract boolean isClosed();

    /**
     * {@code public Point firstPoint()}
     * <p>
     * Méthode retournant le premier sommet de la polyligne
     *
     * @return - le premier sommet de la polyligne
     */
    public Point firstPoint() {
        return vertexList.get(0);
    }

    /**
     * {@code public List<Point> points()}
     * <p>
     * Méthode retournant la liste des sommets de la polyligne
     *
     * @return - la liste des sommets de la polyligne
     */
    public List<Point> points() {
        return vertexList;
    }


    /**
     * Méthode qui écrit la liste des points dans le flot d'objet sortant.
     * @param oos un flot d'objet sortant
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeInt(vertexList.size());
        for (Point p : vertexList) {
            oos.writeDouble(p.x());
            oos.writeDouble(p.y());
        }
    }

    /**
     * Méthode qui lit la liste des points et qui modifie le champ final vertexList pour le remplacer par la liste importée
     * @param ois le flot d'entrée d'objets
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream ois) throws IllegalAccessException, NoSuchFieldException, IOException {
        int length = ois.readInt();
        List<Point> tmpVertexList = new ArrayList<>(length);

        for (int i = 0; i < length; i++)
            tmpVertexList.add(new Point(ois.readDouble(), ois.readDouble()));

        Field f = PolyLine.class.getDeclaredField("vertexList");
        f.setAccessible(true);
        f.set(this, Collections.unmodifiableList(tmpVertexList));
    }

}