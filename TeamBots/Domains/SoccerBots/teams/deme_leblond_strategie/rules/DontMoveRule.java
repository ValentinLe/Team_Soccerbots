
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;

/**
 * Regle de position stationnaire en fixant une direction
 */
public class DontMoveRule extends AbstractRule {

  private Vec2 direction;

  /**
   * Construit la regle avec la direction dans laquelle l'agent doit regarder
   * @param abstract_robot l'agent
   * @param direction la direction dans laquelle l'agent doit regarder
   */
  public DontMoveRule(SocSmall abstract_robot, Vec2 direction) {
    super(abstract_robot);
    this.direction = direction;
  }

  /**
   * test si la regle est activable, la regle n'a pas de condition de non-activation
   * @return true
   */
  public boolean isActivable() {
    return true;
  }

  /**
   * Effectue l'action de la regle, fixe la vitesse de l'agent a 0 et place la
   * direction de l'agent a direction de la classe
   */
  public void action() {
    this.abstract_robot.setSteerHeading(curr_time, this.direction.t);
		this.abstract_robot.setSpeed(curr_time, 0.0);
  }
}
