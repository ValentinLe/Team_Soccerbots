

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import java.util.Random;

public class Arsenal extends ControlSystemSS {

	private final double MIN_DIST_CLOSEST_PLAYER = 0.1;
	private final double MIN_DIST_BALL_CENTER = 0.05;

	private final double PROP_ANGLE_SHOOT = 0.035;
	private final double ORTOGONAL_DELTA = 0.01;

	private final double DIST_DEFEND = 0.6;
	private final double DIST_GUARDIEN_HORIZONTAL_TO_GOAL = 0.1;
	private final double DIST_GUARDIEN_VERTICAL_TO_GOAL = 0.4;

	private final double PHI_COHESION = 0.8;
	private final double PHI_SEPARATION = 1;
	private final double PHI_ALIGNEMENT = 1;
	private final double DIST_NEAR_TEAMATE = 0.5;

	private long curr_time;

	public void Configure() {}


	public int TakeStep() {
		this.curr_time = abstract_robot.getTime();
		int mynum = abstract_robot.getPlayerNumber(curr_time);
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		Vec2 goal = abstract_robot.getOurGoal(curr_time);
		Vec2 ball = abstract_robot.getBall(curr_time);

		if (cantEngage()) {
			escapeFromOpponent();
		} else if (iAmNearestTo(ball)) {
			attack();
		} else if (iAmNearestTo(goal)) {
			defend();
		} else if (haveCloseTeamate()) {
			escapeFromTeamate();
		} else if (haveCloseOpponent()) {
			escapeFromOpponent();
		} else {
			moveSwarm();
		}

		return(CSSTAT_OK);
	}


	/* COMPORTEMENTS AGENTS */

	public void attack() {
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
		if (canShoot(PROP_ANGLE_SHOOT)) {
			abstract_robot.kick(curr_time);
		} else {
			move(getShootSpotTo(opponentsGoal));
		}
	}

	public void defend() {
		Vec2 goal = abstract_robot.getOurGoal(curr_time);
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 posDefend = getDefendSpot();

		if (getDistance(ball, goal) < DIST_DEFEND) {
			attack();
		} else if (posDefend.r > abstract_robot.RADIUS/2) {
			move(posDefend);
		} else {
			abstract_robot.setSpeed(curr_time, 0);
			abstract_robot.setSteerHeading(curr_time, ball.t);
		}
	}

	public void moveDistBary() {
		Vec2 barycentre = getBarycentreMyTeam();
		Vec2 myPosBary = new Vec2(barycentre);
		if (myPosBary.r < 0.4) {
			myPosBary.setr(-1 * myPosBary.r);
		}
		move(myPosBary);
	}

	public void moveSwarm() {
		Vec2 barycentre = getBarycentreMyTeam();
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		Vec2 destination = new Vec2(0, 0);
		double meanAngle = getMeanAngle();
		Vec2 cohesion = new Vec2(barycentre);
		cohesion.setr(cohesion.r * PHI_COHESION);
		destination.add(cohesion);
		Vec2 separation = getSeparation();
		separation.setr(separation.r * PHI_SEPARATION * -1);
		destination.add(separation);
		destination.sett(destination.t * PHI_ALIGNEMENT);
		move(destination);
	}

	public void randomMove() {
		Random r = new Random();
		double x = 2*r.nextDouble()-1;
		double y = 2*r.nextDouble()-1;
		Vec2 v = new Vec2(x, y);
		move(v);
	}

	public void escapeFromTeamate() {
		move(awayFrom(getClosestPlayer(abstract_robot.getTeammates(curr_time))));
	}

	public void escapeFromOpponent() {
		move(awayFrom(getClosestPlayer(abstract_robot.getOpponents(curr_time))));
	}

	public void move(Vec2 dir) {
		abstract_robot.setSteerHeading(curr_time, dir.t);
		abstract_robot.setSpeed(curr_time, 1.0);
	}


	/* RECUPERATIONS SENSORIELS */

	public Vec2 getClosestPlayer(Vec2[] players) {
		Vec2 closestPlayer = new Vec2(Integer.MAX_VALUE,0);
		for (int i = 0; i < players.length; i++) {
			if (players[i].r < closestPlayer.r) {
				closestPlayer = players[i];
			}
		}
		return closestPlayer;
	}

	public Vec2 getShootSpotTo(Vec2 target) {
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 targetToBall = new Vec2(ball);
		targetToBall.sub(target);
		targetToBall.setr(targetToBall.r + abstract_robot.KICKER_SPOT_RADIUS);
		Vec2 shootSpot = new Vec2(target);
		shootSpot.add(targetToBall);
		return shootSpot;
	}

	public Vec2 getDefendSpot() {
		Vec2 goal = abstract_robot.getOurGoal(curr_time);
		Vec2 ball = abstract_robot.getBall(curr_time);
		double y;
		if (ball.y <= 0) {
			y = Math.min(goal.y - ball.y, goal.y - DIST_GUARDIEN_VERTICAL_TO_GOAL);
		} else {
			y = Math.min(goal.y + ball.y, goal.y + DIST_GUARDIEN_VERTICAL_TO_GOAL);
		}
		Vec2 decal;
		if (goal.x <= 0) {
			decal = new Vec2(DIST_GUARDIEN_HORIZONTAL_TO_GOAL, y);
		} else {
			decal = new Vec2(-DIST_GUARDIEN_HORIZONTAL_TO_GOAL, y);
		}
		Vec2 defendSpot = new Vec2(goal);
		defendSpot.add(decal);
		return defendSpot;
	}

	/* TESTS SENSORIELS */

	public boolean haveCloseTeamate() {
		return haveClosePlayer(abstract_robot.getTeammates(curr_time));
	}

	public boolean haveCloseOpponent() {
		return haveClosePlayer(abstract_robot.getOpponents(curr_time));
	}

	public boolean haveClosePlayer(Vec2[] players) {
		Vec2 closestPlayer = getClosestPlayer(players);
		return closestPlayer.r < MIN_DIST_CLOSEST_PLAYER;
	}

	public boolean iAmNearestTo(Vec2 position) {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		return iAmNearestTo(teammates, position);
	}

	public boolean iAmNearestTo(Vec2[] players, Vec2 position) {
		for (int i = 0; i < players.length; i++) {
			double dist = getDistance(players[i], position);
			if (position.r >= dist) {
				return false;
			}
		}
		return true;
	}

	public boolean canShoot(double angleProportion) {
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
		boolean canKick = abstract_robot.canKick(curr_time);
		boolean goodOriented = isSameDirectionToTarget(ball, opponentsGoal, angleProportion) || isOrthoganal(ball, opponentsGoal);
		return canKick && goodOriented && !iAmInMyField();
	}

	public boolean isSameDirectionToTarget(Vec2 position, Vec2 target, double angleProportion) {
		double res = Math.PI * angleProportion;
		return Math.abs(position.t - target.t) < res;
	}

	public boolean iAmInMyField() {
		Vec2 myGoal = abstract_robot.getOurGoal(curr_time);
		Vec2 opponentsGoal = abstract_robot.getOpponentsGoal(curr_time);
		return myGoal.r < opponentsGoal.r;
	}

	public boolean cantEngage() {
		int mynum = abstract_robot.getPlayerNumber(curr_time);
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		Vec2[] oppenents = abstract_robot.getOpponents(curr_time);
		Vec2 opp = getClosestPlayer(oppenents);
		double dist = getDistance(ball, opp);
		return isBallAtCenter() && !iAmNearestTo(oppenents, ball) && iAmNearestTo(ball);
	}

	public boolean isBallAtCenter() {
		Vec2 ball = abstract_robot.getBall(curr_time);
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		myPos.setr(-1 * myPos.r);
		return getDistance(ball, myPos) <= MIN_DIST_BALL_CENTER;
	}

	/* CALCULS SENSORIELS */

	public Vec2 awayFrom(Vec2 target) {
		Vec2 awayFromTarget = new Vec2(target);
		awayFromTarget.sett(awayFromTarget.t + Math.PI);
		return awayFromTarget;
	}

	public double getDistance(Vec2 v1, Vec2 v2) {
		Vec2 temp = new Vec2(v1);
		temp.sub(v2);
		return temp.r;
	}

	public boolean isOrthoganal(Vec2 v1, Vec2 v2) {
		double prodScal = v1.x * v2.x + v1.y * v2.y;
		return Math.abs(prodScal) < ORTOGONAL_DELTA;
	}

	public Vec2 getBarycentreMyTeam() {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		double x = 0;
		double y = 0;
		for (int i = 0; i < teammates.length; i++) {
			x += teammates[i].x;
			y += teammates[i].y;
		}
		x = x / teammates.length;
		y = y / teammates.length;
		return new Vec2(x, y);
	}

	public double getMeanAngle() {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		double meanAngle = 0;
		for (int i = 0; i < teammates.length; i++) {
			meanAngle += teammates[i].t;
		}
		meanAngle = meanAngle / teammates.length;
		return meanAngle;
	}

	public Vec2 getCohesion() {
		Vec2 posCohesion = new Vec2(0, 0);
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		for (int i = 0; i < teammates.length; i++) {
			posCohesion.add(teammates[i]);
		}
		return posCohesion;
	}

	public Vec2 getSeparation() {
		Vec2[] teammates = abstract_robot.getTeammates(curr_time);
		Vec2 myPos = abstract_robot.getPosition(curr_time);
		double x = 0;
		double y = 0;
		int cpt = 0;
		for (int i = 0; i < teammates.length; i++) {
			if (teammates[i].r < DIST_NEAR_TEAMATE) {
				x += teammates[i].x;
				y += teammates[i].y;
				cpt += 1;
			}
		}
		x = x / cpt;
		y = y / cpt;
		return new Vec2(x, y);
	}

}
