//package TetrisGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class TetrisGame extends JFrame {
    public TetrisGame() {
        add(new Board());
        setTitle("Swing Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var game = new TetrisGame();
            game.setVisible(true);
        });
    }
}

class Board extends JPanel {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 30;
    private Timer timer;
    private boolean isFallingFinished = false;
    private int curX = 0, curY = 0;
    private int[][] board;
    private Shape curPiece;

    public Board() {
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        curPiece = new Shape();
        
        // タイマー設定 (400msごとに落下)
        timer = new Timer(400, e -> update());
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (curPiece.getShape() == 0) return;
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) tryMove(curPiece, curX - 1, curY);
                if (key == KeyEvent.VK_RIGHT) tryMove(curPiece, curX + 1, curY);
                if (key == KeyEvent.VK_DOWN) tryMove(curPiece, curX, curY + 1);
                if (key == KeyEvent.VK_UP) tryMove(curPiece.rotate(), curX, curY);
                if (key == KeyEvent.VK_SPACE) dropDown();
            }
        });
        newPiece();
    }

    private void update() {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
        repaint();
    }

    private void newPiece() {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 - 1;
        curY = 0;
        if (!tryMove(curPiece, curX, curY)) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over");
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY + newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) return false;
            if (board[y][x] != 0) return false;
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY + 1)) pieceDropped();
    }

    private void dropDown() {
        while (tryMove(curPiece, curX, curY + 1));
        pieceDropped();
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY + curPiece.y(i);
            board[y][x] = curPiece.getShape();
        }
        removeFullLines();
        isFallingFinished = true;
    }

    private void removeFullLines() {
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) { full = false; break; }
            }
            if (full) {
                for (int k = i; k > 0; k--) board[k] = board[k - 1].clone();
                board[0] = new int[BOARD_WIDTH];
                i++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 設置済みブロックの描画
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != 0) drawSquare(g, j * TILE_SIZE, i * TILE_SIZE, board[i][j]);
            }
        }
        // 操作中ミノの描画
        for (int i = 0; i < 4; i++) {
            drawSquare(g, (curX + curPiece.x(i)) * TILE_SIZE, (curY + curPiece.y(i)) * TILE_SIZE, curPiece.getShape());
        }
    }

    private void drawSquare(Graphics g, int x, int y, int shape) {
        Color[] colors = {Color.BLACK, Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED};
        g.setColor(colors[shape]);
        g.fillRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2);
    }
}

class Shape {
    private int[][] coords = new int[4][2];
    private int shapeType;
    private static final int[][][] SHAPES_TABLE = {
        {{0,0},{0,0},{0,0},{0,0}}, // NoShape
        {{0,-1},{0,0},{0,1},{0,2}}, // I
        {{-1,0},{0,0},{1,0},{0,1}}, // T
        {{0,0},{1,0},{0,1},{1,1}}, // O
        {{-1,-1},{0,-1},{0,0},{1,0}}, // Z
        {{1,-1},{0,-1},{0,0},{-1,0}}, // S
        {{-1,-1},{-1,0},{0,0},{1,0}}, // J
        {{1,-1},{1,0},{0,0},{-1,0}}  // L
    };

    public void setRandomShape() {
        shapeType = new Random().nextInt(7) + 1;
        for (int i = 0; i < 4; i++) System.arraycopy(SHAPES_TABLE[shapeType][i], 0, coords[i], 0, 2);
    }

    public Shape rotate() {
        if (shapeType == 3) return this; // O型は回転しない
        Shape result = new Shape();
        result.shapeType = shapeType;
        for (int i = 0; i < 4; i++) {
            result.coords[i][0] = -coords[i][1];
            result.coords[i][1] = coords[i][0];
        }
        return result;
    }

    public int x(int i) { return coords[i][0]; }
    public int y(int i) { return coords[i][1]; }
    public int getShape() { return shapeType; }
}