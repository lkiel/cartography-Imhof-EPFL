package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@code public final class Graph<N>}
 * <p>
 * Classe immuable représentant un graphe non orienté de noeuds de type
 * générique
 *
 * @param <N> - le type des noeuds composant le graphe
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class Graph<N> {

    private final Map<N, Set<N>> neighborsMap;

    /**
     * {@code public final static class Builder<N>}
     * <p>
     * Bâtisseur générique de la classe Graph prenant un paramètre de type pour
     * les noeuds qui le composent
     *
     * @param <N> - le type des noeuds composant le graphe
     */
    public final static class Builder<N> {
        private final Map<N, Set<N>> builderNeighborsMap;

        /**
         * {@code public Builder()}
         * <p>
         * Constructeur par défaut initialisant la table associative noeud -
         * ensemble de voisins
         */
        public Builder() {
            builderNeighborsMap = new HashMap<>();
        }

        /**
         * {@code public void addNode(N n)}
         * <p>
         * Ajoute un noeud au graphe si il ne s'y trouve pas déjà
         *
         * @param n - le noeud à ajouter de type générique N
         */
        public void addNode(N n) {
            builderNeighborsMap.putIfAbsent(n, new HashSet<>());
        }

        /**
         * {@code public void addEdge(N n1, N n2) throws IllegalArgumentException}
         * <p>
         * Ajoute une arrête entre les noeuds spécifiés en paramètre ou lance une
         * IllegalArgumentException si un des deux noeuds n'est pas contenu dans
         * le graphe.
         *
         * @param n1 - Le premier noeud constituant le premier sommet de
         *           l'arrête.
         * @param n2 - Le deuxieme noeud constituant le deuxième sommet de
         *           l'arrête.
         * @throws IllegalArgumentException si un des deux noeuds ne se trouve pas dans le graphe
         *                                  lors de l'appel de cette méthode.
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!builderNeighborsMap.containsKey(n1) || !builderNeighborsMap.containsKey(n2))
                throw new IllegalArgumentException("Au moins un des noeuds n'appartient pas au graphe en cours de construction");

            builderNeighborsMap.get(n1).add(n2);
            builderNeighborsMap.get(n2).add(n1);
        }

        /**
         * {@code public Graph<N> build()}
         * <p>
         * Construit un objet de type Graph avec les données du bâtisseur
         *
         * @return un objet de type Graph avec le paramètre de type identique à
         * celui du bâtisseur
         */
        public Graph<N> build() {
            return new Graph<>(builderNeighborsMap);
        }
    }

    /**
     * {@code public Graph(Map<N, Set<N>> neighbors)}
     * <p>
     * Constructeur de la classe Graph prenant en paramètre une table qui
     * associe à chaque noeud son ensemble de voisins direct
     *
     * @param neighbors - la table associative de type {@code Map<N, Set<N>>} où N
     *                  est le type générique du graphe
     */
    public Graph(Map<N, Set<N>> neighbors) {
        //Réalisation d'une copie profonde des Sets et de la HashMap
        Map<N, Set<N>> tmp = new HashMap<>();
        for (Map.Entry<N, Set<N>> mapEntry : neighbors.entrySet())
            tmp.put(mapEntry.getKey(), Collections.unmodifiableSet(new HashSet<>(mapEntry.getValue())));

        neighborsMap = Collections.unmodifiableMap(tmp);
    }

    /**
     * {@code public Set<N> nodes()}
     *
     * @return les noeuds du graphe sous forme d'ensemble
     */
    public Set<N> nodes() {
        return neighborsMap.keySet();
    }

    /**
     * {@code public Set<N> neighborsOf(N node) throws IllegalArgumentException}
     * <p>
     *
     * @param node - Le noeud de référence pour trouver les voisins
     * @return retourne l'ensemble des noeuds voisins du noeud passé en
     * paramètre
     * @throws IllegalArgumentException si le noeud donné ne fait pas partie du graphe.
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (neighborsMap.containsKey(node)) return neighborsMap.get(node);
        throw new IllegalArgumentException("Le noeud passé en paramètre ne fait pas partie du graphe!");
    }
}
