/**
 * Created by DmitryLabutin on 22.07.15.
 */
public class Board {
    private int[][] blocks;
    private int dimension;

    public Board(int[][] blocks) {
        this.blocks = new int[blocks.length][blocks.length];
        this.dimension = blocks.length;
        for (int i = 0; i < this.dimension; ++i) {
            for (int j = 0; j < this.dimension; ++j) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        return this.dimension;
    }

    public int hamming() {
        int result = 0;
        int position = 1;
        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                if (0 != blocks[i][j] && blocks[i][j] != position) {
                    ++result;
                }
                ++position;
            }
        }
        return result;
    }

    private int distance(int i, int j, int number) {
        int row = (number - 1) / this.dimension();
        int col = (number - 1) % this.dimension();

        return Math.abs(i - row) + Math.abs(j - col);
    }

    public int manhattan() {
        int result = 0;
        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                if (0 != blocks[i][j]) {
                    result += this.distance(i, j, blocks[i][j]);
                }
            }
        }
        return result;
    }

    public boolean isGoal() {
        if (blocks[this.dimension() - 1][this.dimension() - 1] != 0) {
            return false;
        }
        int position = 1;
        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                if (blocks[i][i] != 0 && blocks[i][j] != position) {
                    return false;
                }
                ++position;
            }
        }
        return true;
    }

    public Board twin() {
        int row = 0;
        if (blocks[0][0] == 0 || blocks[0][1] == 0) {
            ++row;
        }
        int tmp = blocks[row][0];
        blocks[row][0] = blocks[row][1];
        blocks[row][1] = tmp;

        Board res = new Board(this.blocks);

        tmp = blocks[row][0];
        blocks[row][0] = blocks[row][1];
        blocks[row][1] = tmp;

        return res;
    }

    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board c = (Board) y;
        if (this.dimension() != c.dimension()) return false;
        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                if (this.blocks[i][j] != c.blocks[i][j]) return false;
            }
        }
        return true;
    }

    private int[] findZero() {
        int[] res = new int[2];
        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                if (blocks[i][j] == 0) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return null;
    }

    private void swap(int i1, int j1, int i2, int j2) {
        int tmp = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = tmp;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> it = new Queue<Board>();
        int[] coord = this.findZero();
        if (coord == null) return it;
        int row = coord[0];
        int col = coord[1];

        if (row > 0) {
            swap(row, col, row - 1, col);
            it.enqueue(new Board(blocks));
            swap(row, col, row - 1, col);
        }
        if (row < this.dimension() - 1) {
            swap(row, col, row + 1, col);
            it.enqueue(new Board(blocks));
            swap(row, col, row + 1, col);
        }
        if (col > 0) {
            swap(row, col, row, col - 1);
            it.enqueue(new Board(blocks));
            swap(row, col, row, col - 1);
        }
        if (col < this.dimension() - 1) {
            swap(row, col, row, col + 1);
            it.enqueue(new Board(blocks));
            swap(row, col, row, col + 1);
        }
        return it;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dimension());
        sb.append("\n");
        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                sb.append(" ");
                sb.append(blocks[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
