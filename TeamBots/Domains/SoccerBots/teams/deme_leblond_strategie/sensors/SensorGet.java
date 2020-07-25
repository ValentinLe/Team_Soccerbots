
package deme_leblond_strategie.sensors;

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import deme_leblond_strategie.config.*;

/**
 * Classe pour recuperer des positions, distances, ou autres captes par l'agent
 */
public class SensorGet {

  protected SocSmall abstract_robot;
  protected long curr_time;

  /**
   * Construit l'objet en prenant le current time du jeu ainsi que l'agent
   */
  public SensorGet(SocSmall abstract_robot) {
    this.abstract_robot = abstract_robot;
    this.curr_time = abstract_robot.getTime();
  }

  /**
   * Donne le vecteur menant au joueur le plus proche parmis un ensemble de joeurs
   * @param players l'ensemble des joueurs
   * @return le vecteur menant au joueur le plus proche
   */
	public Vec2 getClosestPlayer(Vec2[] players) {
    return getClosestPlayerTo(players, new Vec2(0, 0));
	}

  /**
   * Recupere le joueur le plus proche d'une position parmis un ensemble de joueurs
   * @param players l'ensemble de joueurs
   * @return le vecteur menant au joueur le plus proche de la position
   */
  public Vec2 getClosestPlayerTo(Vec2[] players, Vec2 position) {
    Vec2 closestPlayer = null;
    double distClosest = Double.MAX_VALUE;
		for (int i = 0; i < players.length; i++) {
      double dist = getDistance(players[i], position);
			if (dist < distClosest) {
				closestPlayer = players[i];
        distClosest = dist;
			}
		}
		return closestPlayer;
  }

  /**
   * Donne le vecteur pour aller au spot de tir afin que l'agent puisse tirer
   * sur la cible souhaitee
   * @param target la direction par rapport a l'agent dont on veut tirer
   * @return le vecteur de la position de shoot sur la cible
   */
	public Vec2 getShootSpotOf(Vec2 target) {
		Vec2 ball = abstract_robot.getBall(curr_time);
    // on recupere le vecteur target-ball
		Vec2 targetToBall = new Vec2(ball);
		targetToBall.sub(target);
    // on ajoute au rayon le radius pour le spot de shoot
		targetToBall.setr(targetToBall.r + SocSmall.KICKER_SPOT_RADIUS);
    // ensuite le vecteur du spot de shoot c'est target + taget-ball avec decalage
    // du radius
		Vec2 shootSpot = new Vec2(target);
		shootSpot.add(targetToBall);
		return shootSpot;
	}

  /**
   * Donne la position du gardien selon la position de la balle
   * @return le vecteur pour aller a la position du gardien
   */
	public Vec2 getDefendSpot() {
		Vec2 goal = abstract_robot.getOurGoal(curr_time);
		Vec2 ball = abstract_robot.getBall(curr_time);
		double y;
    // on recupere le decalage en y que doit avoir le gardien selon la position
    // de la balle
		if (ball.y <= 0) {
      // si la balle est trop basse on s'arrete a la distance vertical maximum
			y = Math.max(ball.y, -Parameters.DIST_GUARDIEN_VERTICAL_TO_GOAL);
		} else {
      // si la balle est trop haute on s'arrete a la distance vertical maximum
			y = Math.min(ball.y, Parameters.DIST_GUARDIEN_VERTICAL_TO_GOAL);
		}
		Vec2 decal;
    // on construit le vecteur de decalage par rapport au vecteur du but que devra
    // avoir le gardien
		if (goal.x <= 0) {
      // si on est dans le cote gauche, on va se decaler en X de +distance au but
			decal = new Vec2(Parameters.DIST_GUARDIEN_HORIZONTAL_TO_GOAL, y);
		} else {
      // si on est dans le cote doit, on va se decaler en X de -distance au but
			decal = new Vec2(-Parameters.DIST_GUARDIEN_HORIZONTAL_TO_GOAL, y);
		}
    // on construit le vecteur pour aller au spot du gardien
		Vec2 defendSpot = new Vec2(goal);
		defendSpot.add(decal);
		return defendSpot;
	}

  /**
   * Renvoi un vecteur dans la direction opposee a celle passee en parametre
   * @param target le vecteur dont on veut l'opposee
   * @return le vecteur dans la direction opposee
   */
	public Vec2 awayFrom(Vec2 target) {
		Vec2 awayFromTarget = new Vec2(target);
		awayFromTarget.sett(awayFromTarget.t + Math.PI);
		return awayFromTarget;
	}

  /**
   * Donne la distance entre deux vecteurs par rapport a l'agent
   * @param v1 le premier vecteur
   * @param v2 le deuxieme vecteur
   * @return la distance entre les deux vecteurs
   */
	public double getDistance(Vec2 v1, Vec2 v2) {
		Vec2 temp = new Vec2(v1);
		temp.sub(v2);
		return temp.r;
	}

  /**
   * Calcul le barycentre des joueurs alies
   * @return le vecteur de l'agent au barycentre
   */
	public Vec2 getBarycentreMyTeam() {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		double x = 0;
		double y = 0;
    // on fait une moyenne des vecteurs
		for (int i = 0; i < teammates.length; i++) {
			x += teammates[i].x;
			y += teammates[i].y;
		}
		x = x / teammates.length;
		y = y / teammates.length;
		return new Vec2(x, y);
	}

  /**
   * Calcul le barycentre des joueurs alies proche de l'agent
   * @return le vecteur entre l'agent et le barycentre des alies proche
   */
  public Vec2 getBarycentreMyNearestTeam() {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		double x = 0;
		double y = 0;
		int cpt = 0;
    // on fait egalement une moyenne
		for (int i = 0; i < teammates.length; i++) {
			if (teammates[i].r < Parameters.DIST_NEAR_TEAMATE) {
				x += teammates[i].x;
				y += teammates[i].y;
				cpt += 1;
			}
		}
		x = x / cpt;
		y = y / cpt;
		return new Vec2(x, y);
	}

  /**
   * Recupere le joueur adverse qui est le plus proche de la balle
   * @return le vecteur par rapport a l'agent du joueur le plus proche de la balle
   */
  public Vec2 getOpponentNearestToBall() {
    Vec2 ball = abstract_robot.getBall(curr_time);
    Vec2[] opponents = abstract_robot.getOpponents(curr_time);
    Vec2 nearestOpponent = new Vec2(Integer.MAX_VALUE, Integer.MAX_VALUE);
    double distBall = Double.MAX_VALUE;
    for (int i = 0; i < opponents.length; i++) {
      double currentDist = getDistance(opponents[i], ball);
      if (distBall > currentDist) {
        nearestOpponent = opponents[i];
        distBall = currentDist;
      }
    }
    return nearestOpponent;
  }

  /**
   * Recupere la position devant la balle qui sera la position ou se placer pour
   * contrer l'attaquant adverse
   * @return la position devant la balle par rapport a l'attaquant adverse
   */
  public Vec2 getSpotCounter() {
    Vec2 ball = abstract_robot.getBall(curr_time);
    Vec2 opponentAttack = getOpponentNearestToBall();
    // calcul du vecteur de l'attaquant a la balle avec un decalage
    Vec2 opponentToBall = new Vec2(ball);
    opponentToBall.sub(opponentAttack);
    opponentToBall.setr(opponentToBall.r + Parameters.DECAL_COUNTER_ATTACK);
    // construction du vecteur agent-position de contre devant la balle
    Vec2 spotCounter = new Vec2(opponentAttack);
    spotCounter.add(opponentToBall);
    return spotCounter;
  }

}
