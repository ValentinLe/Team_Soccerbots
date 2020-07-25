
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.config.*;

/**
 * Regle permettant de contrer l'attaquant adverse en se placant devant la balle
 */
public class CounterAttackRule extends AbstractRule {

  public CounterAttackRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, elle est activable si l'attaquant adverse est
   * plus proche de la balle que l'agent et que l'agent n'est pas proche de ses buts
   * @return true si la regle est activable, false sinon
   */
  public boolean isActivable() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    SensorTest sensorTest = new SensorTest(abstract_robot);
    Vec2 ball = abstract_robot.getBall(curr_time);
    Vec2 nearestOpponentFromBall = sensorGet.getOpponentNearestToBall();
    // position devant la balle
    Vec2 spotBlock = sensorGet.getSpotCounter();
    Vec2 goal = abstract_robot.getOurGoal(curr_time);

    return sensorGet.getDistance(nearestOpponentFromBall, ball) < ball.r &&
            goal.r > Parameters.DIST_GOAL_COUNTER_ATTACK;
  }

  /**
   * Effectue l'action de la regle, l'agent va se aller se placer devant la balle
   */
  public void action() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2 spotBlock = sensorGet.getSpotCounter();
    MoveRule moveRule = new MoveRule(abstract_robot, spotBlock);
    moveRule.action();
  }
}
