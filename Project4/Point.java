package Project4;

public final record Point(int x, int y) {
    @Override
    public boolean equals(Object other) {
        if (other instanceof Point p) {
            return p.x() == x() && p.y() == y();
        }
        return false;
    }
}
