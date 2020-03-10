package fr.univparis.metro;

public class LimitedConnectionSearch {

    /**
     * @param g the graph from which we suppress some stations.
     * @param line suppress all the sations that are not on this line.
     * @return a graph whose sations are all on the same line.
     * FIXME: the complexity of this method is O(n^3) (in the worst case), n being the number of stations in the graph g.
     */
    public static WGraph<Station> createGraphLine(WGraph<Station> g, String line){
        WGraph<Station> ret = new WGraph<Station>();
        ret.getWGraph().putAll(g.getWGraph());
        for(Station s : g.getVertices()){
            if(!s.getLine().equals(line)) ret.deleteVertex(s);
        }
        return ret;
    }

    static int[][] floyd(int[][] c) {
        int n = c.length;
        int[][] d = new int[n][n];
        for (int i=0; i<n; i++) {
          for (int j=0; j<n; j++) {
            d[i][j] = c[i][j];
            }
        }
        for (int k=0; k<n; k++) {
            for (int i=0; i<n; i++) {
                for (int j=0; j<n; j++) {
                    int dik = d[i][k], dkj = d[k][j];
                    if (dik == Integer.MAX_VALUE || dkj == Integer.MAX_VALUE) continue;
                    int u = dik + dkj;
                    if (u < d[i][j]) d[i][j] = u;
                }
            }
        }
        return d;
    }
}