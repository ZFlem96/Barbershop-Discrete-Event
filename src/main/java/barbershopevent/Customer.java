package barbershopevent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Customer extends Thread {
	private long arrivalTime;
	private long serviceTime;
	private long waitingTimeStart = -1, elapsedWaitingTime = -1;
	private static int sessionsCompleted = 0;
	private static ArrayList<Long> collectedWaitingTimes = new ArrayList<Long>(),
			collectedServiceTime = new ArrayList<Long>(), collectedArrivalTime = new ArrayList<Long>();
	private static Calendar today;
	private static Semaphore barberSemaphore = new Semaphore(1, true);

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public long getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(long serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Customer(long arrivalTime, long serviceTime) {
		this.arrivalTime = arrivalTime;
		this.serviceTime = serviceTime;
	}

	public void arrival() {
		try {
			Random rand = new Random();
			double randValue = rand.nextDouble();
			final long arrivalTime = (long) (-Math.log((1 - randValue)) * this.getArrivalTime());
			Thread.sleep(arrivalTime);
			Customer.today = Calendar.getInstance();
			Customer.today.set(Calendar.HOUR_OF_DAY, 0);
			System.out.println("Customer " + this.getId() + " has arrived. (Workday time: " + (today.getTime()) + " )");
			waitingTimeStart = System.nanoTime();
			collectedArrivalTime.add(arrivalTime);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void service() {
		Random rand = new Random();
		double randValue = rand.nextDouble();
		final long serviceTime = (long) (-Math.log((1 - randValue)) * this.getServiceTime());
		try {
			Thread.sleep(serviceTime);
			collectedServiceTime.add(serviceTime);
			Customer.today = Calendar.getInstance();
			Customer.today.set(Calendar.HOUR_OF_DAY, 0);
			System.out.println("Barber is finished with Customer " + this.getId() + "'s hair. (Work day time: "
					+ (today.getTime()) + " )");
			update();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void update() {
		if (collectedWaitingTimes.size() != 0 && collectedArrivalTime.size() != 0 && collectedServiceTime.size() != 0) {
			long waitingTimeSum = 0, arrivalTimeSum = 0, serviceTimeSum = 0;
			for (int x = 0; x < collectedWaitingTimes.size(); x++) {
				waitingTimeSum = waitingTimeSum + collectedWaitingTimes.get(x);
			}
			for (int x = 0; x < collectedArrivalTime.size(); x++) {
				arrivalTimeSum = arrivalTimeSum + collectedArrivalTime.get(x);
			}
			for (int x = 0; x < collectedServiceTime.size(); x++) {
				serviceTimeSum = serviceTimeSum + collectedServiceTime.get(x);
			}
			Customer.today = Calendar.getInstance();
			Customer.today.set(Calendar.HOUR_OF_DAY, 0);
			long averageWaitingTime = (long) waitingTimeSum / collectedWaitingTimes.size();
			long averageArrivalTime = (long) arrivalTimeSum / collectedArrivalTime.size();
			long averageServiceTime = (long) serviceTimeSum / collectedServiceTime.size();
			System.out.println("Current average arrival time: " + averageArrivalTime
					+ " millisecond(s)| Current average waiting time: " + averageWaitingTime
					+ " millisecond(s)| Current average service time: " + averageServiceTime
					+ " millisecond(s) (Work day time: " + today.getTime() + ")");

		}
	}

	public void run() {
		int x = sessionsCompleted;
		try {
			barberSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			elapsedWaitingTime = System.nanoTime() - waitingTimeStart;
			collectedWaitingTimes.add(elapsedWaitingTime);
			Customer.today = Calendar.getInstance();
			Customer.today.set(Calendar.HOUR_OF_DAY, 0);
			System.out.println("Barber is cutting Customer " + this.getId() + "'s hair. Customer  was waiting for "
					+ elapsedWaitingTime + " millisecond(s). (Work day time: " + (today.getTime()) + " )");
			service();

		} finally {
			barberSemaphore.release();
		}
		x++;
		sessionsCompleted = x;
	}

}
