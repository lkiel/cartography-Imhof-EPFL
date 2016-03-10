package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCapStyle;
import ch.epfl.imhof.painting.LineStyle.LineJoinStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * {@code public final class RoadPainterGenerator}
 * <p>
 * Générateur de peintres pour les routes, utilisable uniquement via sa méthode statique painterForRoads
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class RoadPainterGenerator {

    private RoadPainterGenerator() {
    }

    /**
     * {@code  public static Painter painterForRoads(RoadSpec rs1, RoadSpec... rs)}
     * <p>
     * Génère un peintre qui dessine les spécifications de route dans l'ordre
     * spécifié par la déclaration
     *
     * @param rs1 - La première spécification de route
     * @param rs  - Un nombre arbitraire de spécifications aditionnelles
     * @return Un peintre suivant les spécifications de routes fournies
     */
    public static Painter painterForRoads(RoadSpec rs1, RoadSpec... rs) {
        List<Painter> paintersList = new ArrayList<>();
        List<RoadSpec> specsList = new ArrayList<>();
        specsList.add(rs1);
        specsList.addAll(Arrays.asList(rs));

        specsList.forEach(v -> paintersList.add(v.bridgeIP));
        specsList.forEach(v -> paintersList.add(v.bridgeBP));
        specsList.forEach(v -> paintersList.add(v.roadIP));
        specsList.forEach(v -> paintersList.add(v.roadBP));
        specsList.forEach(v -> paintersList.add(v.tunnelP));

        return paintersList.stream().reduce(Painter::above).get();
    }

    /**
     * {@code public static class RoadSpec}
     * <p>
     * Classe décrivant la spécification d'une route à l'aide d'un prédicat ainsi que
     * de quatre paramètres de style. À l'interne la spécification est stockée sous forme
     * de cinq peintres distincts pour les types de tracés suivants: intérieur/extérieur des ponts,
     * intérieur/extérieur des routes ainsi que les ponts.
     */
    public static final class RoadSpec {

        private final static Predicate<Attributed<?>> isBridge = Filters.tagged("bridge");
        private final static Predicate<Attributed<?>> isTunnel = Filters.tagged("tunnel");
        private final static Predicate<Attributed<?>> isRoad = isBridge.or(isTunnel).negate();

        private final Painter bridgeIP;
        private final Painter bridgeBP;
        private final Painter roadIP;
        private final Painter roadBP;
        private final Painter tunnelP;

        /**
         * Constructeur de la classe RoadSpec
         *
         * @param predicate le prédicat à utiliser
         * @param wi        la largeur interne
         * @param ci        la couleur interne
         * @param wc        la largeur de bordure
         * @param cc        la couleur de bordure
         */
        public RoadSpec(Predicate<Attributed<?>> predicate, float wi, Color ci, float wc, Color cc) {
            Painter innerRoadPainter = Painter.line(new LineStyle(wi, ci, LineCapStyle.Round, LineJoinStyle.Round));
            LineStyle outerRoadStyle = new LineStyle(wi + 2 * wc, cc, LineCapStyle.Butt, LineJoinStyle.Round);
            float f = 2 * wi;

            bridgeIP = innerRoadPainter.when(predicate.and(isBridge));
            roadIP = innerRoadPainter.when(predicate.and(isRoad));
            bridgeBP = Painter.line(outerRoadStyle).when(predicate.and(isBridge));
            roadBP = Painter.line(outerRoadStyle.withCap(LineCapStyle.Round)).when(predicate.and(isRoad));
            tunnelP = Painter.line(outerRoadStyle.withWidth(wi / 2f).withPattern(f, f)).when(predicate.and(isTunnel));
        }

    }

}
