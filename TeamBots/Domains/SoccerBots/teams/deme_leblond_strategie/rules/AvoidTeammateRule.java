
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;

/**
 * Regle d'evitement de joueurs
 */
public class AvoidTeammateRule extends AbstractRule {

  public AvoidTeammateRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, elle l'est si il y a un alies proche ou
   * un adversaire proche
   * @return true si la regle peut etre activee, false sinon
   */
  public boolean isActivable() {
    SensorTest sensorTest = new SensorTest(abstract_robot);
    return sensorTest.haveCloseTeamate();
  }

  /**
   * Effectue l'action de la regle, l'agent se deplace dans la direction opposee
   * des joueurs proches de lui
   */
  public void action() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2 closestTeammate = sensorGet.getClosestPlayer(abstract_robot.getTeammates(curr_time));
    Vec2 avoidTeammate = sensorGet.awayFrom(closestTeammate);

    // ensuite on deplace l'agent dans la direction opposee du ou des joueurs trop proches
    MoveRule moveRule = new MoveRule(abstract_robot, avoidTeammate);
    moveRule.action();
  }
}
