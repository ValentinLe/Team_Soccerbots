
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;

/**
 * Classe abstraite d'une Regle
 */
public abstract class AbstractRule implements Rule {

  protected SocSmall abstract_robot;
  protected long curr_time;

  /**
   * Contruit la regle en prenant l'agent et le current time du jeu en attribut
   */
  public AbstractRule(SocSmall abstract_robot) {
    this.abstract_robot = abstract_robot;
    this.curr_time = abstract_robot.getTime();
  }
}
