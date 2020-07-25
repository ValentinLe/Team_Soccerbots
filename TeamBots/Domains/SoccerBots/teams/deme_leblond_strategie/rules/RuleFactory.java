
package deme_leblond_strategie.rules;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.sensors.*;

/**
 * Factory de regle
 */
public class RuleFactory {

  protected SocSmall abstract_robot;
  protected long curr_time;

  /**
   * Construit la factory avec l'agent et le temps courrant en attribut
   */
  public RuleFactory(SocSmall abstract_robot) {
    this.abstract_robot = abstract_robot;
    this.curr_time = abstract_robot.getTime();
  }

  /**
   * Construit la regle de la strategie principale d'un agent
   */
  public Rule getStrategy() {
    ChainRule strategy = new ChainRule();
    // si l'agent ne peut pas engager, il evite de bloquer l'adversaire sur l'engagement
    strategy.addToEnd(new NoEngageRule(abstract_robot));
    // si l'agent est le plus proche de la balle, il attaque
    strategy.addToEnd(this.getAttackRule());
    // si l'agent est le plus proche des buts, il defend
    strategy.addToEnd(this.getDefendRule());
    // si l'agent est proche d'autres joueurs, il les evites
    strategy.addToEnd(new AvoidTeammateRule(abstract_robot));
    strategy.addToEnd(new AvoidOpponentRule(abstract_robot));
    // sinon ils se deplace en nuee
    strategy.addToEnd(new MoveSwarmRule(abstract_robot));
    return strategy;
  }

  /**
   * Construit la regle de comportement d'attaque d'un agent
   * @return la regle de comportement d'attaque d'un agent
   */
  public Rule getAttackRule() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    CounterAttackRule counterAttack = new CounterAttackRule(abstract_robot);
    ShootRule shootRule = new ShootRule(abstract_robot);
    Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
    MoveRule moveRule = new MoveRule(abstract_robot, sensorGet.getShootSpotOf(opponentsGoal));
    ChainRule behaviour = new ChainRule();
    // l'attaquant va se placer devant l'attanquant adverse
    behaviour.addToEnd(counterAttack);
    // l'attaquant shoot dans la balle si il le peut
    behaviour.addToEnd(shootRule);
    // sinon il va au spot de tire en direction du but
    behaviour.addToEnd(moveRule);
    // retourne la regle d'attaque activable quand l'agent est le plus proche de
    // la balle
    return new AttackRule(abstract_robot, behaviour);
  }

  /**
   * Construit la regle de comportement de defense d'un agent
   * @return la regle de comportement de defense d'un agent
   */
  public Rule getDefendRule() {
    GoalExitRule goalExit = new GoalExitRule(abstract_robot);
    DefendPositionRule defendSpot = new DefendPositionRule(abstract_robot);
    Vec2 ball = abstract_robot.getBall(curr_time);
    DontMoveRule dontMove = new DontMoveRule(abstract_robot, ball);
    // creation de la chaine du comportement
    ChainRule behaviour = new ChainRule();
    // le denseur fait une sortie si la balle est trop proche des buts
    behaviour.addToEnd(goalExit);
    // il se deplace vers le spot de defense si il n'y est pas
    behaviour.addToEnd(defendSpot);
    // sinon il ne bouge pas et fixe la balle
    behaviour.addToEnd(dontMove);
    return new DefendRule(abstract_robot, behaviour);
  }
}
