package game.entities;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // 위치 값을 직접 변경하는 메서드 (객체 재사용)
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 현재 위치에서 상대적으로 이동 (예: x+1, y+0)
    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public Position plus(int dx, int dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    // 거리 계산
    public double distanceTo(Position target, int offsetX, int offsetY) {
        // 가상의 내 위치
        int hypotheticalX = this.x + offsetX;
        int hypotheticalY = this.y + offsetY;

        return Math.sqrt(Math.pow(target.x - hypotheticalX, 2) + Math.pow(target.y - hypotheticalY, 2));
    }

    // 현재 위치에서 특정 거리와 각도만큼 이동한 '새로운 위치' 반환
    public Position move(double distance, double directionInRadians) {
        int nextX = this.x + (int) (Math.cos(directionInRadians) * distance);
        int nextY = this.y + (int) (Math.sin(directionInRadians) * distance);
        return new Position(nextX, nextY);
    }

    // 기준점(pivot)을 중심으로 대칭 이동한 위치 반환 (Inky 전략용)
    public Position reflectAround(Position pivot) {
        int vectorX = pivot.x - this.x;
        int vectorY = pivot.y - this.y;
        return new Position(pivot.x + vectorX, pivot.y + vectorY);
    }
}