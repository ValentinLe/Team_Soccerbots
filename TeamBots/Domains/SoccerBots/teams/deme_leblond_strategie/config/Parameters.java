
package deme_leblond_strategie.config;

public class Parameters {

  // distance minimum entre joueurs
  public final static double MIN_DIST_CLOSEST_TEAMMATE = 0.1;
  public final static double MIN_DIST_CLOSEST_OPPONENT = 0.1;
  // distance minimum entre la balle et le centre pour considerer que la balle
  // est au centre
  public final static double MIN_DIST_BALL_CENTER = 0.05;

  // proportion de l'angle de tir de l'attaquant
  public final static double PROP_ANGLE_SHOOT = 0.034;
  // ecart pour considerer que deux vecteurs sont orthogonaux
  public final static double ORTOGONAL_DELTA = 0.01;

  // distance maximum dans laquelle le gardien doit faire une sortie de but
  public final static double DIST_DEFEND = 0.6;
  // decalage horizontal par rapport au but que doit avoir le gardien
  public final static double DIST_GUARDIEN_HORIZONTAL_TO_GOAL = 0.1;
  // distance vertical maximum que doit avoir le gardien avec le but
  public final static double DIST_GUARDIEN_VERTICAL_TO_GOAL = 0.25;
  // distance minimum avec les buts ou l'agent devra se placer devant la balle
  public final static double DIST_GOAL_COUNTER_ATTACK = 0.2;
  // proportion de l'angle pour considerer qu'un jouer est dans le meme sens que
  // la balle
  public final static double PROP_ANGLE_GOALEXIT = 0.02;
  // decalage par rapport a la balle du placement du defenseur pour contrer l'attaquant
  // adverse
  public final static double DECAL_COUNTER_ATTACK = 0.19;

  // distance au barycentre pour la regle MoveBarycentreRule
  public final static double DIST_TO_BARYCENTRE = 0.4;

  // proportion sur la cohesion des joueurs
  public final static double PHI_COHESION = 0.8;
  // proportion sur la separation des joueurs
  public final static double PHI_SEPARATION = 1;
  // distance maximum dont on considere qu'un joueur est proche dans la nuee
  public final static double DIST_NEAR_TEAMATE = 0.5;

  // porbabilite d'engager (pour eviter les bloquage ou on se laisse engager mutuellement)
  public final static double PROBA_ENGAGE = 0.05;
}
