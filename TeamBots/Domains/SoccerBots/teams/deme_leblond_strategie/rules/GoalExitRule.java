
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.config.*;

/**
 * Regle de sortie de but du gardien
 */
public class GoalExitRule extends AbstractRule {

  public GoalExitRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, elle l'est si la distance entre la balle et
   * le but de l'equipe de l'agent est trop faible
   */
  public boolean isActivable() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    SensorTest sensorTest = new SensorTest(abstract_robot);
    Vec2 goal = abstract_robot.getOurGoal(curr_time);
    Vec2 ball = abstract_robot.getBall(curr_time);
    boolean isBallNear = sensorGet.getDistance(ball, goal) < Parameters.DIST_DEFEND;
    return isBallNear && !sensorTest.thereIsTeammateBeforeBall();
  }

  /**
   * Effectue l'action de la regle, quand la balle est trop proche, le goal aura
   * le comportement d'un attaquant
   */
  public void action() {
    RuleFactory factory = new RuleFactory(abstract_robot);
    Rule attack = factory.getAttackRule();
    // on test pas si la regle est activable, on execute l'action de la regle directement
    attack.action();
  }
}
