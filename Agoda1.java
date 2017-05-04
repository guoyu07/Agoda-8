import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
	int id;
	int acceleration;
	float speed;
	int topSpeed;
}

class RaceCar extends Car {
	float nitro;
	float handlingFactor;
	float position;
	boolean usedNitro;
	boolean isFinished;

	public String toString() {
		return "id:" + id + ", speed:" + speed + ", postion:" + position + ", acceleration:" + acceleration
				+ ", topSpeed:" + topSpeed + ", nitro:" + usedNitro + ", finished:" + isFinished;
	}
}

class RaceCarResult {
	RaceCar car;
	int finishedTime;
	float finalSpeed;

	public String toString() {
		return "id:" + car.id + ", finishedTime:" + finishedTime + ", finalSpeed" + finalSpeed;
	}
}

class RaceResult {
	List<RaceCarResult> carResults = new ArrayList<RaceCarResult>();
}

public class Agoda1 {

	private int TAILGATING = 10;
	private int DELTA_TIME = 2;
	private int distance;

	private List<RaceCar> cars;
	private List<RaceCar> finishedCars = new ArrayList<RaceCar>();

	public RaceResult raceResult = new RaceResult();

	public Agoda1(List<RaceCar> cars, int distance) {
		this.distance = distance;
		this.cars = cars;
	}

	private boolean isFinished() {
		return finishedCars.size() == cars.size();
	}

	private void printStatus() {
		for (RaceCar car : cars) {
			System.out.println(car);
		}
	}

	private void accesses(int t) {
		System.out.println("time:" + t);

		double minPos = Double.MAX_VALUE;
		int minIdx = -1;

		// refresh speed and position of each car
		for (int i = 0; i < cars.size(); ++i) {
			RaceCar car = cars.get(i);
			if (car.isFinished)
				continue;
			// refresh position
			car.position += car.speed * DELTA_TIME + 0.5 * car.acceleration * DELTA_TIME * DELTA_TIME;
			if (car.position < minPos) {
				minPos = car.position;
				minIdx = i;
			}
			// refresh speed
			if (car.speed < car.topSpeed) {
				car.speed = Math.min(car.speed + car.acceleration * DELTA_TIME, car.topSpeed);
			}
			// check if the car finish racing
			if (car.position >= distance) {
				car.isFinished = true;
				RaceCarResult result = new RaceCarResult();
				result.car = car;
				result.finalSpeed = car.speed;
				result.finishedTime = t;
				finishedCars.add(car);
				raceResult.carResults.add(result);

				System.out.println("car " + car.id + " finish racing");
				System.out.println(car);
			}

		}

		printStatus();

		// if some one is tailgating, decelerate
		for (int i = 0; i < cars.size(); ++i) {
			RaceCar cari = cars.get(i);
			if (cari.isFinished)
				continue;
			for (int j = 0; j != i && j < cars.size(); ++j) {
				RaceCar carj = cars.get(j);
				if (carj.isFinished)
					continue;
				if (carj.position > cari.position && (carj.position - cari.position) < TAILGATING) {
					cari.speed *= cari.handlingFactor;
					System.out.println("car " + cari.id + " is tailgating car " + carj.id + " at time:" + t);
					System.out.println(cari);
					System.out.println(carj);
				}
			}
		}

		// the last position car may use nitro
		if (minIdx != -1) {
			RaceCar car = cars.get(minIdx);
			if (!car.usedNitro) {
				car.speed = Math.min(car.topSpeed, car.speed * car.nitro);
				car.usedNitro = true;
				System.out.println("car " + car.id + " use nitro");
				System.out.println(car);
			}
		}
	}

	public RaceResult race() {
		int time = 0;
		while (!isFinished()) {
			time += DELTA_TIME;
			accesses(time);

		}
		return raceResult;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int distance = sc.nextInt();
		sc.close();
		List<RaceCar> cars = new ArrayList<RaceCar>();
		for (int i = 1; i <= n; ++i) {
			RaceCar car = new RaceCar();
			car.id = i;
			car.topSpeed = 150 + 10 * i;
			car.acceleration = 2 * i;
			car.nitro = 2;
			car.handlingFactor = 0.8f;
			car.position = 200 * (n - i);
			car.speed = 0;
			cars.add(car);
		}
		Agoda1 inst = new Agoda1(cars, distance);
		RaceResult raceResult = inst.race();

		System.out.println("RACING RESULT");
		for (RaceCarResult carResult : raceResult.carResults) {
			System.out.println(carResult.car + ", finishedTime:" + carResult.finishedTime + ", finalSpeed:"
					+ carResult.finalSpeed);
		}

	}
}