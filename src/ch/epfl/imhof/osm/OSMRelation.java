package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code public final class OSMRelation extends OSMEntity}
 * <p>
 * Classe immuable héritant d'{@code OSMEntity} représentant une relation OSM
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class OSMRelation extends OSMEntity {

    private final List<Member> membersList;

    /**
     * {@code public static final class Builder extends OSMEntity.Builder}
     * <p>
     * Bâtisseur de la classe {@code OSMRelation} héritant du bâtisseur d'
     * {@code OSMEntity}
     */
    public static final class Builder extends OSMEntity.Builder {

        private final List<Member> builderMembersList;

        /**
         * {@code public Builder(long id)}
         * <p>
         * Construit le bâtisseur avec l'identifiant unique de la relation
         *
         * @param id - l'identifiant unique de la relation
         */
        public Builder(long id) {
            super(id);
            builderMembersList = new ArrayList<>();
        }

        /**
         * {@code public void addMember(Member.Type type, String role, OSMEntity newMember)}
         * <p>
         * Ajoute un nouveau membre au bâtisseur de la relation
         *
         * @param type      - le {@link Type} du membre, pouvant etre NODE, WAY ou RELATION
         * @param role      - {@link String} indiquant le rôle du membre
         * @param newMember - {@link OSMEntity} représentant l'entité associée au membre
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            builderMembersList.add(new Member(type, role, newMember));
        }

        /**
         * {@code public OSMRelation build() throws IllegalStateException}
         * <p>
         * Construit un objet de type OSMRelation.
         *
         * @return Une {@link OSMRelation}
         * @throws IllegalStateException si l'entité a été définie comme étant incomplète. c'est
         *                               le cas si la méthode {@code setIncomplete()} a été
         *                               appelée précédemment sur l'objet
         */
        public OSMRelation build() throws IllegalStateException {
            if (isIncomplete())
                throw new IllegalStateException("La relation en cours de construction est incomplète");

            return new OSMRelation(idBuilder, builderMembersList, attributesBuilder.build());
        }
    }

    /**
     * {@code public static final class Member}
     * <p>
     * Classe immuable imbriquée statiquement dans {@code OSMRelation}
     * représentant un membre d'une relation OpenStreetMaps
     */
    public static final class Member {

        private final Type memberType;
        private final String memberRole;
        private final OSMEntity memberEntity;

        /**
         * {@code public enum Type}
         * <p>
         * Enumère les 3 types de membres qu'une relation peut comporter. les
         * options possibles sont {@code NODE}, {@code WAY} et {@code RELATION}
         */
        public enum Type {
            NODE, WAY, RELATION
        }

        /**
         * {@code public Member(Type type, String role, OSMEntity member)}
         * <p>
         * Constructeur de la classe Member
         *
         * @param type   - le {@link Type} du membre
         * @param role   - {@link String} indiquant le rôle du membre
         * @param member - l'entité de type {@link OSMEntity} du membre
         */
        public Member(Type type, String role, OSMEntity member) {
            memberType = type;
            memberRole = role;
            memberEntity = member;
        }

        /**
         * {@code public Type type()}
         * <p>
         *
         * @return le {@link Type} du membre. les valeurs possibles sont {@code NODE},
         * {@code WAY} et {@code RELATION}
         */
        public Type type() {
            return memberType;
        }

        /**
         * {@code public String role()}
         * <p>
         *
         * @return le rôle du membre au sein de la relation
         */
        public String role() {
            return memberRole;
        }

        /**
         * {@code public OSMEntity member()}
         * <p>
         *
         * @return l'entité OSM du membre lui-même
         */
        public OSMEntity member() {
            return memberEntity;
        }
    }

    /**
     * {@code public OSMRelation(long id, List<Member> members, Attributes attributes)}
     * <p>
     * Constructeur de la classe {@code OSMRelation}, créant une relation étant
     * donnés son identifiant unique, ses membres et ses attributs
     *
     * @param id         - l'identifiant unique
     * @param members    - la liste des {@link Member} qui composent la relation
     * @param attributes - les {@link Attributes} de la relation
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        membersList = Collections.unmodifiableList(new ArrayList<>(members));
    }

    /**
     * {@code public List<Member> members()}
     * <p>
     *
     * @return la liste des {@link Member} présents au sein de la relation
     */
    public List<Member> members() {
        return membersList;
    }
}
