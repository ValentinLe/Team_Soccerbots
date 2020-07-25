
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.config.*;

/**
 * Regle de tir
 */
public class ShootRule extends AbstractRule {

  public ShootRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, la regle est activable si l'agent reunit les
   * conditions de tir
   * @return true si la regle est activable, false sinon
   */
  public boolean isActivable() {
    SensorTest sensorTest = new SensorTest(abstract_robot);
    return sensorTest.canShoot(Parameters.PROP_ANGLE_SHOOT);
  }

  /**
   * Effectue l'action de la regle, l'agent tir dans la balle
   */
  public void action() {
    this.abstract_robot.kick(curr_time);
  }
}
