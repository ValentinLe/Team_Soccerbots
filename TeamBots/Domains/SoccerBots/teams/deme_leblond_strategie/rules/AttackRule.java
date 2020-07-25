
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;

/**
 * Regle de l'attaquant
 */
public class AttackRule extends BehaviourRule {

  public AttackRule(SocSmall abstract_robot, ChainRule behaviour) {
    super(abstract_robot, behaviour);
  }

  /**
   * La regle d'attaque est activable si l'agent est le plus proche de la balle
   * par rapport a ses alies
   * @return true si la regle peut etre activee, false sinon
   */
  public boolean isActivable() {
    SensorTest sensorTest = new SensorTest(abstract_robot);
    Vec2 ball = abstract_robot.getBall(curr_time);
    // l'agent est le plus proche de la balle et au moins un comportement de l'attaque est
    // activable
    return sensorTest.iAmNearestTo(ball) && super.isActivable();
  }
}
