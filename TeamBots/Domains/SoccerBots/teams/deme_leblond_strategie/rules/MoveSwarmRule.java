
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.config.*;

/**
 * Regle de deplacement en nuee
 */
public class MoveSwarmRule extends AbstractRule {

  public MoveSwarmRule(SocSmall abstract_robot) {
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
   * Effectue l'action de la regle, se deplace vers la position calculer par le
   * principe de la nuee
   */
  public void action() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2 barycentre = sensorGet.getBarycentreMyTeam();
    Vec2 destination = new Vec2(0, 0);

    // la cohesion c'est se deplacer vers le barycentre de l'equipe
    Vec2 cohesion = new Vec2(barycentre);
    cohesion.setr(cohesion.r * Parameters.PHI_COHESION);

    // la separation c'est se deplacer dans la direction opposee a celle du barycentre
    // des alies les plus proches
    Vec2 separation = sensorGet.getBarycentreMyNearestTeam();
    separation.setr(separation.r * Parameters.PHI_SEPARATION);
    separation = sensorGet.awayFrom(separation);

    destination.add(cohesion);
    destination.add(separation);
    MoveRule moveRule = new MoveRule(abstract_robot, destination);
    moveRule.action();
  }
}
