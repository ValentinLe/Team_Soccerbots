
package deme_leblond_strategie.sensors;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.config.*;

/**
 * Classe pour faire des tests sensoriels que capte l'agent
 */
public class SensorTest {

  protected SocSmall abstract_robot;
  protected long curr_time;

  /**
   * Construit l'objet en prenant le current time du jeu ainsi que l'agent
   */
  public SensorTest(SocSmall abstract_robot) {
    this.abstract_robot = abstract_robot;
    this.curr_time = abstract_robot.getTime();
  }

  /**
   * test si il y a un alies proche de l'agent (selon la constante de distance
   * entre joueurs)
   * @return true si un alies est proche false sinon
   */
  public boolean haveCloseTeamate() {
		return haveClosePlayer(
          abstract_robot.getTeammates(curr_time),
          Parameters.MIN_DIST_CLOSEST_TEAMMATE
    );
	}

  /**
   * test si il y a un joueur adverse proche de l'agent
   * @return true si un adversaire est proche de l'agent false sinon
   */
	public boolean haveCloseOpponent() {
		return haveClosePlayer(
          abstract_robot.getOpponents(curr_time),
          Parameters.MIN_DIST_CLOSEST_OPPONENT
    );
	}

  /**
   * test si il y a au moins un joueur d'un ensemble de joueurs proche de l'agent
   * @param players l'ensemble des joueurs pour le test
   * @return true si un des joueurs est proche de l'agent
   */
	public boolean haveClosePlayer(Vec2[] players, double minDist) {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    // au moins un joueur est proche si le joueur le plus proche de l'agent est
    // a une distance inferieure a la constante sur la distance min que doit avoir
    // un joueur
		Vec2 closestPlayer = sensorGet.getClosestPlayer(players);
		return closestPlayer.r < minDist;
	}

  /**
   * test si l'agent est le plus proche d'une position par rapport a ses alies
   * @return true si l'agent est plus proche de la position false sinon
   */
	public boolean iAmNearestTo(Vec2 position) {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		return iAmNearestTo(teammates, position);
	}

  /**
   * test si l'agent est le plus proche d'une position par rapport a un ensemble
   * de joueurs
   * @param players l'ensemble de joueurs a comparer
   * @return true si l'agent est plus proche de la position false sinon
   */
	public boolean iAmNearestTo(Vec2[] players, Vec2 position) {
    return iAmNthNearestTo(players, position, 1);
	}

  /**
   * test si l'agent reunit toutes les conditions pour tirer, il tirera si il
   * est oriente vers le but ou si il est contre un mur orthoganal avec le but.
   * L'agent tir uniquement quand il n'est pas dans son camp
   * @return true si l'agent est conforme pour tirer false sinon
   */
	public boolean canShoot(double angleProportion) {
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
		boolean canKick = abstract_robot.canKick(curr_time);
		boolean goodOriented = isSameDirectionToTarget(ball, opponentsGoal, angleProportion);
    // on ajoute une restriction sur l'angle pour eviter que l'agent tire quand il est au milieu
    // du terrain pas du tout oriente vers le camp adverse
    boolean isGoodOrthogonalOriented = isOrientedToOpponentGoal();
    // si le vecteur de la balle et celui du but adverse sont orthogonaux
    boolean orthogonalPos = isOrthoganal(ball, opponentsGoal) && isGoodOrthogonalOriented;
		return canKick && (goodOriented || orthogonalPos) && !iAmInMyField();
	}

  /**
   * Test si l'agent avec la balle est oriente vers le camp adverse
   * @return true si l'agent avec la balle est oriente vers le camp adverse, false sinon
   */
  public boolean isOrientedToOpponentGoal() {
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
    Vec2 goal = abstract_robot.getOurGoal(curr_time);
    return ball.x * opponentsGoal.x > 0;
  }

  /**
   * test si une position est oriente dans la meme direction que la cible selon
   * une proportion d'angle si la proportion egale a 1, l'angle de validation sera
   * un demi-cercle, 2 un cercle, 0.5 un angle droit, etc.
   * @param position la position a tester
   * @param target la cible a laquelle tester la position
   * @param angleProportion la mutiplicite de PI de l'angle maximum a avoir entre
   * la position et la cible
   * @return true si la position est dans la meme direction que la cible selon l'angle
   * false sinon
   */
	public boolean isSameDirectionToTarget(Vec2 position, Vec2 target, double angleProportion) {
		double angleMaximum = Math.PI * angleProportion;
    // test si l'ecart entre la position et la cible est inferieur a l'angle maximum
    // souhaite
		return Math.abs(position.t - target.t) < angleMaximum;
	}

  /**
   * test si l'agent est a la position donnee
   * @param position la position sur laquelle tester
   * @return true si l'agent est sur la position, false sinon
   */
  public boolean iAmAtPosition(Vec2 position) {
    // l'agent est sur la position si la distance avec celle-ci est inferieur a
    // son rayon
    return position.r > SocSmall.RADIUS/2;
  }

  /**
   * test si l'agent est dans son camp
   * @return true si l'agent est dans son camp, false sinon
   */
	public boolean iAmInMyField() {
		Vec2 myGoal = abstract_robot.getOurGoal(curr_time);
		Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
    // l'agent est dans son camp si il est plus proche de ses buts que des buts
    // adverse
    return myGoal.r < opponentsGoal.r;
	}

  /**
   * test si la balle est au centre du terrain
   * @return true si la distance entre la balle et le centre est inferieur a la
   * constante appropriee false sinon
   */
	public boolean isBallAtCenter() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
		Vec2 ball = abstract_robot.getBall(curr_time);
    // le vecteur entre l'agent et le centre est -celui de la position du joueur
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		myPos.setr(-1 * myPos.r);
		return sensorGet.getDistance(ball, myPos) <= Parameters.MIN_DIST_BALL_CENTER;
	}

  /**
   * test si les deux vecteurs sont orthogonaux sous un certain seuil
   * @param v1 le premier vecteur
   * @param v2 le deuxieme vecteur
   * @return true si les deux vecteurs sont orthogonaux, false sinon
   */
	public boolean isOrthoganal(Vec2 v1, Vec2 v2) {
		double prodScal = v1.x * v2.x + v1.y * v2.y;
		return Math.abs(prodScal) < Parameters.ORTOGONAL_DELTA;
	}

  /**
   * test si l'agent ne peut pas engager, il ne peut pas engager si la balle est
   * au centre du terrain et si il y a un adversaire plus proche que lui de la balle
   * et il faut aussi que le joueur soit le plus proche de la balle par rapport a
   * son equipe
   * @return true si l'agent ne peut pas engager false sinon
   */
  public boolean cantEngage() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2 ball = abstract_robot.getBall(curr_time);
    Vec2[] opponents = abstract_robot.getOpponents(curr_time);
    return isBallAtCenter() && !iAmNearestTo(opponents, ball) && iAmNearestTo(ball);
  }

  /**
   * Test si l'agent est le nieme plus proche de son equipe d'une position cible
   * @param taget la position cible
   * @param n la nieme position de l'agent
   * @return true si l'agent est le nieme plus proche de la position, false sinon
   */
  public boolean iAmNthNearestTo(Vec2[] players, Vec2 target, int n) {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    int nb_nearest = 0;
    for (int i = 0; i < players.length; i++) {
      double dist = sensorGet.getDistance(target, players[i]);
      if (target.r > dist) {
        nb_nearest += 1;
      }
    }
    // l'agent est a la nieme position si il y a n-1 alies de plus proche que lui
    return nb_nearest == (n-1);
  }

  /**
   * Test si le gardien est en train de faire une sortie de but, cad si le joueur
   * le plus proche des buts n'est pas suffisemment proche des buts
   * @return true si le gardien est en train de faire une sortie de but
   */
  public boolean isGoalExit() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2[] teammates = abstract_robot.getTeammates(curr_time);
    Vec2 goal = abstract_robot.getOurGoal(curr_time);
    Vec2 nearestToGoal = sensorGet.getClosestPlayerTo(teammates, goal);
    double distXnearestToGoal = Math.abs(goal.x - nearestToGoal.x);
    return distXnearestToGoal > Parameters.DIST_GOAL_COUNTER_ATTACK;
  }

  /**
   * Test si l'alie le plus proche est entre la balle et l'agent
   * @return true si l'alie le plus proche est entre la balle et l'agent, false sinon
   */
  public boolean thereIsTeammateBeforeBall() {
    SensorGet sensorGet = new SensorGet(abstract_robot);
    Vec2[] teammates = abstract_robot.getTeammates(curr_time);
    Vec2 goal = abstract_robot.getOurGoal(curr_time);
    Vec2 ball = abstract_robot.getBall(curr_time);
    Vec2 gardien = sensorGet.getClosestPlayerTo(teammates, goal);

    // l'alie le plus proche est devant la balle si la balle est dans la meme direction
    // que l'alie et que l'alie est plus proche que la balle
    return isSameDirectionToTarget(gardien, ball, Parameters.PROP_ANGLE_GOALEXIT) &&
           ball.r > gardien.r;
  }

  /**
   * Test si l'agent est le 2eme plus proche et que le gardien fait une sortie de
   * but et qu'il n'y a pas de joueur entre lui et la balle
   * @return true si l'agent doit aller defendre, false sinon
   */
  public boolean mustDefend() {
    Vec2[] teammates = abstract_robot.getTeammates(curr_time);
    Vec2 goal = abstract_robot.getOurGoal(curr_time);
    Vec2 ball = abstract_robot.getBall(curr_time);

    return isGoalExit() &&
           iAmNthNearestTo(teammates, goal, 2);
  }
}
