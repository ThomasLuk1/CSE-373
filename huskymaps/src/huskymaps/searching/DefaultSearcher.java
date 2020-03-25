package huskymaps.searching;

import autocomplete.Autocomplete;
import autocomplete.DefaultTerm;
import autocomplete.Term;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @see Searcher
 */
public class DefaultSearcher extends Searcher {
    Autocomplete searcher;
    HashMap<String, Node> map;
    public DefaultSearcher(StreetMapGraph graph) {
        List<Node> nodes = graph.allNodes();
         map = new HashMap<String, Node>();
         int count = 0;
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println(nodes.get(i).name());
            if (nodes.get(i).name() != null) {
                count++;
            }
        }
        Term[] termsArray = new Term[count];
        int index = 0;
        for (int i = 0; i < nodes.size(); i++) {
            String nodeName = nodes.get(i).name();
            int nodeWeight = nodes.get(i).importance();
            if (nodeName != null) {
                Term newTerm = createTerm(nodeName, nodeWeight);
                map.put(nodeName, nodes.get(i));
                termsArray[index] = newTerm;
                index++;
            }
        }
        searcher = createAutocomplete(termsArray);
    }

    @Override
    protected Term createTerm(String name, int weight) {
        return new DefaultTerm(name, weight);
    }

    @Override
    protected Autocomplete createAutocomplete(Term[] termsArray) {
        return new Autocomplete(termsArray);
    }

    @Override
    public List<String> getLocationsByPrefix(String prefix) {
        Term[] terms = searcher.findMatchesForPrefix(prefix);
        List results = new ArrayList<String>();
        for (int i = 0; i < terms.length; i++) {
            if (!results.contains(terms[i].query())) {
                results.add(terms[i].query());
            }
        }
        return results;
    }

    @Override
    public List<Node> getLocations(String locationName) {
        Term[] terms = searcher.findMatchesForPrefix(locationName);
        List<Node> result = new ArrayList<Node>();
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].query().equals(locationName)) {
                result.add(map.get(terms[i].query()));
            }
        }
        return result;
    }
}
