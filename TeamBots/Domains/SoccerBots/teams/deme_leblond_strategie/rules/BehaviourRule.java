
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;

/**
 * Classe abstraite representant un comportement
 */
public abstract class BehaviourRule extends AbstractRule {

  protected ChainRule behaviour;

  /**
   * Un comportement comporte une chaine de regle de subsembption
   */
  public BehaviourRule(SocSmall abstract_robot, ChainRule behaviour) {
    super(abstract_robot);
    this.behaviour = behaviour;
  }

  /**
   * test si la regle est activable
   * @return true si la regle peut etre activee, false sinon
   */
  public boolean isActivable() {
    // un comportement est activable si au moins une des regles de la chaine est
    // activable
    return behaviour.isActivable();
  }

  /**
   * l'action d'un comportement est l'action de sa chaine de regle
   */
  public void action() {
    behaviour.action();
  }
}
