
package deme_leblond_strategie.rules;

/**
 * Interface representant une regle
 */
public interface Rule {

  /**
   * test si la regle est activable
   * @return true si la regle peut etre activee, false sinon
   */
  public boolean isActivable();

  /**
   * Effectue l'action de la regle
   */
  public void action();

}
