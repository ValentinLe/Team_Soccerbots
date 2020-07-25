
package deme_leblond_strategie.rules;

/**
 * Regle representant une chaine de regle de subsemption
 */
public class ChainRule implements Rule {

  protected Rule rule;
  protected ChainRule next;

  /**
   * Une maillon de chaine possede un suivant et une regle a appliquer
   * @param rule la regle a appliquer
   * @param next le maillon suivant
   */
  public ChainRule(Rule rule, ChainRule next) {
    this.rule = rule;
    this.next = next;
  }

  /**
   * Une maillon de chaine possede un suivant et une regle a appliquer
   * @param rule la regle a appliquer
   */
  public ChainRule(Rule rule) {
    this(rule, null);
  }

  /**
   * Une maillon de chaine possede un suivant et une regle a appliquer
   */
  public ChainRule() {
    this(null, null);
  }

  /**
   * Setter sur le maillon suivant de la chaine
   * @param next le maillont suivant de la chaine
   */
  public void setNext(ChainRule next) {
    this.next = next;
  }

  /**
   * ajoute le maillon a la fin de la chaine
   * @param end le maillon a ajouter en fin de chaine
   */
  public void addToEnd(ChainRule end) {
    if (this.rule == null) {
      // si la regle de la classe n'est pas setter on met la regle du maillon
      this.rule = end.rule;
    } else {
      // set le maillon suivant au dernier maillon de la chaine
      ChainRule current = this;
      while (current.hasNext()) {
        current = current.next;
      }
      current.setNext(end);
    }
  }

  /**
   * ajoute la regle comme maillon a la fin de la chaine
   * @param rule la regle a ajouter comme maillon en fin de chaine
   */
  public void addToEnd(Rule rule) {
    this.addToEnd(new ChainRule(rule));
  }

  /**
   * test si le maillon possede un suivant
   * @return true si le maillon possede un suivant, false sinon
   */
  public boolean hasNext() {
    return this.next != null;
  }

  /**
   * test si la regle est activable, une chaine est activable si au moins une regle
   * de ses maillons est activable
   */
  public boolean isActivable() {
    ChainRule current = this;
    while (current != null) {
      if (current.rule.isActivable()) {
        return true;
      }
      current = current.next;
    }
    return false;
  }

  /**
   * Active la regle chainee, si la regle du maillon est activable, on l'active
   * sinon on dit au maillon suivant de faire de meme
   */
  public void action() {
    if (rule != null) {
      if (rule.isActivable()) {
        rule.action();
      } else {
        if (next != null) {
          next.action();
        }
      }
    }
  }

}
