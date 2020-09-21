package barbershopevent;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Hello world!
 *
 */
public class Driver {

	public static void main(String[] args) {
		long[] serviceTimes = { 5000, 10000, 15000 };
		ArrayList<Customer> customers = new ArrayList<Customer>();
		/* 11 */ customers.add(new Customer(1000, serviceTimes[0]));
		/* 12 */ customers.add(new Customer(1000, serviceTimes[1]));
		/* 13 */ customers.add(new Customer(10000, serviceTimes[2]));
		/* 14 */ customers.add(new Customer(20000, serviceTimes[2]));
		/* 15 */ customers.add(new Customer(30000, serviceTimes[1]));
		/* 16 */ customers.add(new Customer(40000, serviceTimes[0]));
		/* 17 */ customers.add(new Customer(50000, serviceTimes[2]));
		/* 18 */ customers.add(new Customer(60000, serviceTimes[2]));
		/* 19 */ customers.add(new Customer(70000, serviceTimes[1]));

		for (int x = 0; x < customers.size(); x++) {
			customers.get(x).arrival();
			customers.get(x).start();
		}

	}
}
