import java.awt.Graphics;
import java.util.Optional;
public class ChoosingActor implements GameState {
  @Override
  public void mouseClick(int x, int y, Stage s) {
    s.playerInAction = Optional.empty();
    for(Actor player: s.listOfPlayers) {
      //Only allow selection if player has energy to move
      if(player.loc.contains(x, y) && !player.isBot() && player.canMove()) {
        s.cellOverlay = s.grid.getRadius(player.loc, player.moves);
        s.playerInAction = Optional.of(player);
        s.currentState = new SelectingNewLocation();
      }
      else if(player.loc.contains(x, y) && !player.isBot() && !player.canMove()) {
        System.out.println(player.getClass().getSimpleName() + " needs rest! Energy too low to move.");
      }
    }
  }
  @Override
  public void paint(Graphics g, Stage s) {
    // no paint activity for this GameState
  }
  public String toString() {
    return getClass().getSimpleName();
  }
}