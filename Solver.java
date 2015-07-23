/**
 * Created by DmitryLabutin on 22.07.15.
 */
public class Solver {
    private SearchNode solve = null;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int movies;
        private int priority = -1;

        public SearchNode(Board b, int movies, SearchNode prev) {
            this.board = b;
            this.movies = movies;
            this.prev = prev;
        }

        public int priority() {
            if (priority == -1) {
                priority = board.manhattan() + movies;
            }
            return priority;
        }

        @Override
        public int compareTo(SearchNode that) {
            int p1 = this.priority();
            int p2 = that.priority();
            if (p1 < p2) return -1;
            if (p1 > p2) return +1;
            return 0;
        }
    }
    public Solver(Board initial) {
        if (null == initial) {
            throw new NullPointerException();
        }
        Board twin = initial.twin();
        MinPQ<SearchNode> mqpInitial = new MinPQ<SearchNode>();
        MinPQ<SearchNode> mqpTwin = new MinPQ<SearchNode>();
        SearchNode snInitial = new SearchNode(initial, 0, null);
        SearchNode snTwin = new SearchNode(twin, 0, null);
        mqpInitial.insert(snInitial);
        mqpTwin.insert(snTwin);
        while (true) {
            snInitial = mqpInitial.delMin();
            if (snInitial.board.isGoal()) {
                solve = snInitial;

                break;
            } else {
                for (Board n: snInitial.board.neighbors()) {
                    if (snInitial.prev == null || !n.equals(snInitial.prev.board)) {
                        mqpInitial.insert(new SearchNode(
                                n,
                                snInitial.movies + 1,
                                snInitial));
                    }
                }
            }
            snTwin = mqpTwin.delMin();
            if (snTwin.board.isGoal()) {
                solve = null;
                break;
            } else {
                for (Board n: snTwin.board.neighbors()) {
                    if (snTwin.prev == null || !n.equals(snTwin.prev.board)) {
                        mqpTwin.insert(new SearchNode(n, snTwin.movies + 1, snTwin));
                    }
                }
            }
        }
    }

    public boolean isSolvable() {
        return solve != null;
    }

    public int moves() {
        if (!this.isSolvable()) {
            return -1;
        }
        return solve.movies;
    }

    public Iterable<Board> solution() {
        if (!this.isSolvable()) {
            return null;
        }
        Stack<Board> res = new Stack<Board>();
        SearchNode s = solve;
        while (s != null) {
            res.push(s.board);
            s = s.prev;
        }
        return res;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
