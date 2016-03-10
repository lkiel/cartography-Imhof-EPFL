package ch.epfl.imhof.geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code public final class Polygon}
 * <p>
 * Classe implémentant les polygones, constitués d'une <code>ClosedPolyLine</code>
 * formant leur enveloppe extérieure (shell) et d'une liste de <code>ClosedPolyLine</code>
 * répertoriant les éventuels trous du <code>Polygon</code>.
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class Polygon implements Serializable {

    private static final long serialVersionUID = 2015052700L;
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holesList;

    /**
     * {@code public Polygon(ClosedPolyLine shell)}
     * <p>
     * Construit un polygone avec l'enveloppe donnée en argument sans trous
     *
     * @param shell - l'enveloppe du polygone
     */
    public Polygon(ClosedPolyLine shell) {
        this.shell = shell;
        holesList = Collections.emptyList();
    }

    /**
     * {@code public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes)}
     * <p>
     * Construit un polygone à partir d'une enveloppe extérieure et d'une liste de
     * polylignes fermées qui constitueront les trous du polygone. Le
     * constructeur ne vérifie pas que tous les trous sont effectivement
     * contenus dans l'enveloppe.
     *
     * @param shell - l'enveloppe du polygone
     * @param holes - la liste des polylignes qui représentent les trous
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        holesList = Collections.unmodifiableList(new ArrayList<>(holes));
    }

    /**
     * {@code public ClosedPolyLine shell()}
     * <p>
     * Méthode retournant l'enveloppe extérieure du polygone
     *
     * @return - l'enveloppe du polygone
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * {@code public List<ClosedPolyLine> holes()}
     * <p>
     * Méthode retournant la liste des trous contenus dans le polygone
     *
     * @return - la liste des trous contenus dans le polygone
     */
    public List<ClosedPolyLine> holes() {
        return holesList;
    }

}