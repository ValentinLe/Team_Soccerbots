
package deme_leblond_strategie;

/*
 * GoToBall.java
 */

import EDU.gatech.cc.is.util.Vec2;
import EDU.gatech.cc.is.abstractrobot.*;
import java.util.Random;
import deme_leblond_strategie.sensors.*;
import deme_leblond_strategie.rules.*;
import deme_leblond_strategie.config.*;

//Clay not used

/**
 * This is about the simplest possible soccer strategy, just go to the ball.
 * (c)1997 Georgia Tech Research Corporation
 *
 * @author Tucker Balch
 * @version $Revision: 1.1 $
 */


public class Demeleblond extends ControlSystemSS {

	/**
	Configure the Avoid control system.  This method is
	called once at initialization time.  You can use it
	to do whatever you like.
	*/
	public void Configure() {}

	/**
	Called every timestep to allow the control system to
	run.
	*/
	public int TakeStep() {
		double curr_time = abstract_robot.getTime();
		if (curr_time != 0) {
			RuleFactory factory = new RuleFactory(abstract_robot);
			Rule action = factory.getStrategy();
			action.action();
		}

		// tell the parent we're OK
		return(CSSTAT_OK);
	}

}
