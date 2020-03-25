package autocomplete;

import java.util.Objects;

/**
 * This is currently the only implementation of the {@link Term} interface, which is why it's named
 * "default." (Having an interface with a single implementation is a little redundant, but we need
 * it to keep you from accidentally renaming things.)
 *
 * Make sure to check out the interface for method specifications.
 * @see Term
 */
public class DefaultTerm implements Term {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultTerm that = (DefaultTerm) o;
        return weight == that.weight &&
            Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, query);
    }

    @Override
    public String toString() {
        return "DefaultTerm{" +
            "weight=" + weight +
            ", query='" + query + '\'' +
            '}';
    }

    long weight;
    String query;
    /**
     * Initializes a term with the given query string and weight.
     * @throws IllegalArgumentException if query is null or weight is negative
     */
    public DefaultTerm(String query, long weight) {
        if (query == null || weight < 0) {
            throw new IllegalArgumentException("Invalid query or weight.");
        }
        this.weight = weight;
        this.query = query;
    }

    @Override
    public String query() {
        return query;
    }

    @Override
    public long weight() {
        return weight;
    }

    @Override
    public int queryOrder(Term that) {
        return query.compareTo(that.query());
    }

    @Override
    public int reverseWeightOrder(Term that) {
        return Long.compare(this.weight, that.weight()) * -1;
    }

    @Override
    public int matchesPrefix(String prefix) {
        if (query.length() > prefix.length()) {
            return query.substring(0, prefix.length()).compareTo(prefix);
        } else {
            return query.compareTo(prefix);
        }
    }
}
