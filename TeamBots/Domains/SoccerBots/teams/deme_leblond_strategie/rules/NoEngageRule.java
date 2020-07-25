
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.config.*;
import java.util.Random;

/**
 * Regle sur l'engagement, l'agent n'engage pas si un adversaire est plus proche
 * de la balle que lui pour eviter le bloquage infini sur l'engagement
 */
public class NoEngageRule extends AbstractRule {

  public NoEngageRule(SocSmall abstract_robot) {
    super(abstract_robot);
  }

  /**
   * test si la regle est activable, elle est activable si l'agent ne peut pas
   * engager et que qu'il y a un adversaire proche de lui
   * @return true si la regle est activable, false sinon
   */
  public boolean isActivable() {
    SensorTest sensorTest = new SensorTest(abstract_robot);
    Random r = new Random();
    // on rajoute un cas ou l'attaquant ira chercher le ballon quand meme avec
    // une faible probabilite pour eviter le bloquage ou les deux attaquants des equipe
    // serait tous les deux dans le meme cas et laisse l'autre engage
    return (sensorTest.cantEngage() && sensorTest.haveCloseOpponent()) ||
           (sensorTest.isBallAtCenter() && r.nextDouble() < Parameters.PROBA_ENGAGE);
  }

  /**
   * Effectue l'action de la regle, deplace l'agent dans la direction opposee des
   * adversaires proches (et des alies ici car on utilise la regle d'evitement de joueurs)
   */
  public void action() {
    AvoidOpponentRule avoidOpponent = new AvoidOpponentRule(abstract_robot);
    avoidOpponent.action();
  }
}
