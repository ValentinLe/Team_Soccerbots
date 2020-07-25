
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;

/**
 * regle du gardien
 */
public class DefendRule extends BehaviourRule {

  public DefendRule(SocSmall abstract_robot, ChainRule behaviour) {
    super(abstract_robot, behaviour);
  }

  /**
   * test si la regle est activable, elle est activable si l'agent est le plus proche
   * des buts par rapport a son equipe ou si il est le second et que le gardien est
   * en sortie de but
   * @return true si la regle est activable, false sinon
   */
  public boolean isActivable() {
    SensorTest sensorTest = new SensorTest(abstract_robot);
    Vec2 goal = abstract_robot.getOurGoal(curr_time);
    // l'agent est le plus proche de ses buts et au moins un comportement de la defense
    // est activable
    return (sensorTest.iAmNearestTo(goal) || sensorTest.mustDefend()) && super.isActivable();
  }
}
