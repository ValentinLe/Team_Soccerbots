
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import java.util.*;

/**
 * Regle de deplacement aleatoire de l'agent
 */
public class RandomMoveRule extends AbstractRule {

  public RandomMoveRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, la regle n'a pas de condition de non-activation
   * @return true
   */
  public boolean isActivable() {
    return true;
  }

  /**
   * Effectue l'action de la regle, tire une direction aleatoirement et deplace
   * l'agent dans cette direction
   */
  public void action() {
    Random r = new Random();
    // on tire x et y entre -1 et 1
		double x = 2 * r.nextDouble() - 1;
		double y = 2 * r.nextDouble() - 1;
		Vec2 randomVect = new Vec2(x, y);
    MoveRule moveRule = new MoveRule(abstract_robot, randomVect);
    moveRule.action();
  }
}
