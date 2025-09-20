import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {
    public static void main(String[] args) throws Exception {
      Main window = new Main();
      window.run();
    }

    class Canvas extends JPanel {
      Stage stage = new Stage();
      Player player;

      public Canvas() {
        setPreferredSize(new Dimension(1024, 720));
        player = (Player) stage.actors.stream().filter(a -> a instanceof Player).findFirst().orElse(null);

        System.out.println("Player initialized: " + (player != null)); // Debug output to ensure player is found

        addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            System.out.println("Key pressed: " + e.getKeyCode()); // Debug output to signify which key was pressed
            if (player != null) {
              switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                  player.move(Direction.UP, stage.grid, stage.actors, stage.items, stage);
                  break;
                case KeyEvent.VK_DOWN:
                  player.move(Direction.DOWN, stage.grid, stage.actors, stage.items, stage);
                  break;
                case KeyEvent.VK_LEFT:
                  player.move(Direction.LEFT, stage.grid, stage.actors, stage.items, stage);
                  break;
                case KeyEvent.VK_RIGHT:
                  player.move(Direction.RIGHT, stage.grid, stage.actors, stage.items, stage);
                  break;
              }
              repaint();
            }
          }
        });
        setFocusable(true);
        requestFocusInWindow(); // Ensure Canvas has focus so key events are captured
      }

      @Override
      public void paint(Graphics g) {
        stage.paint(g, getMousePosition());
      }
    }

    private Main() {
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Canvas canvas = new Canvas();
      this.setContentPane(canvas);
      this.pack();
      this.setVisible(true);
    }

    public void run() {
      while(true) {
        repaint();
      }
    }
}
