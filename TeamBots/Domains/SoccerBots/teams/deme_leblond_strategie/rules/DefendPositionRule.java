
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;

/**
 * Regle de deplacement vers la position de defense du gardien
 */
public class DefendPositionRule extends AbstractRule {

  public DefendPositionRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, elle est activable si l'agent n'est pas sur
   * l'emplacement de defense
   * @return true si la regle est activable, false sinon
   */
  public boolean isActivable() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    SensorTest sensorTest = new SensorTest(abstract_robot);
    Vec2 posDefend = sensorGet.getDefendSpot();
    return sensorTest.iAmAtPosition(posDefend);
  }

  /**
   * Effectue l'action de la regle, deplace l'agent dans la direction de la position
   * de defense du gardien
   */
  public void action() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2 posDefend = sensorGet.getDefendSpot();
    MoveRule moveRule = new MoveRule(abstract_robot, posDefend);
    moveRule.action();
  }
}
