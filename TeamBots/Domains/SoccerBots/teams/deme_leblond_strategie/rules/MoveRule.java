
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;

/**
 * Regle de deplacement dans une direction
 */
public class MoveRule extends AbstractRule {

  private Vec2 direction;

  /**
   * Construit la regle avec la direction dans laquelle l'agent doit se deplacer
   * @param abstract_robot l'agent
   * @param direction la direction dans laquelle l'agent doit se deplacer
   */
  public MoveRule(SocSmall abstract_robot, Vec2 direction) {
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
   * Effectue l'action de la regle, direge l'agent dans la direction de la classe
   * et met sa vitesse a 1
   */
  public void action() {
    this.abstract_robot.setSteerHeading(curr_time, this.direction.t);
		this.abstract_robot.setSpeed(curr_time, 1.0);
  }
}
