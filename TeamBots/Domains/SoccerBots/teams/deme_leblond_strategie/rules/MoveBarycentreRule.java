
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.config.*;

/**
 * Regle de deplacement de l'agent a une certaine distance du barycentre, premiere
 * version du deplacement en nuee
 */
public class MoveBarycentreRule extends AbstractRule {

  public MoveBarycentreRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, la regle n'a pas de condition de non activation
   * @return true
   */
  public boolean isActivable() {
    return true;
  }

  /**
   * Effectue l'action de la regle, se deplace vers le barycentre si l'agent est
   * trop loin, dans la direction opposee si il est trop proche
   */
  public void action() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2 barycentre = sensorGet.getBarycentreMyTeam();
    Vec2 destination = new Vec2(barycentre);
    if (barycentre.r < Parameters.DIST_TO_BARYCENTRE) {
      // si l'agent est trop proche du barycentre on prend la direction opposee
      destination = sensorGet.awayFrom(destination);
    }
    MoveRule moveRule = new MoveRule(abstract_robot, destination);
    moveRule.action();
  }
}
