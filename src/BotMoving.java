import java.awt.Graphics;
import java.util.List;

public class BotMoving implements GameState {
  @Override
  public void mouseClick(int x, int y, Stage s) {
    // no mouseClick activity for this GameState
  }

  @Override
  public void paint(Graphics g, Stage s) {
    // Update energy for all actors at start of turn
    for(Actor player: s.listOfPlayers) {
      player.updateEnergy(s.getWeatherTracker());
    }
    
    // move bots
    boolean anyBotMoved = false;
    for(Actor player: s.listOfPlayers) {
      if(player.isBot() && player.canMove()) {
        List<Cell> possibleLocs = s.getClearRadius(player.loc, player.moves);
        
        if (!possibleLocs.isEmpty()) {
          Cell chosenCell;
          
          // If low energy, seek rest spot. Otherwise move normally
          if (player.needsRest()) {
            // Use energy strategy to find best recovery spot
            chosenCell = player.getEnergyStrategy().chooseBestCell(possibleLocs, s.getWeatherTracker(), player.getEnergy());
          }
          else {
            // Normal movement (use existing strategy)
            chosenCell = player.mover.chooseNextLoc(possibleLocs, player, s.listOfPlayers);
          }
          
          if (chosenCell != null) {
            player.setLocation(chosenCell);
            anyBotMoved = true;
          }
        }
      }
      else if (player.isBot() && !player.canMove()) {
        // Bot skips turn due to low energy
        System.out.println(player.getClass().getSimpleName() + " is too tired to move!");
      }
    }
    
    // Record one move for weather tracker if any bot moved
    if (anyBotMoved) {
      s.getWeatherTracker().recordMove();
    }
    
    s.currentState = new ChoosingActor();
    for(Actor player: s.listOfPlayers) {
      player.turns = 1;
    }
  }  

  public String toString() {
    return getClass().getSimpleName();
  }
}