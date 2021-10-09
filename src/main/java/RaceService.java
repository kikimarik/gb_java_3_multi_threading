import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class RaceService {
    public static final int CARS_COUNT = 4;
    
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(CARS_COUNT);
        CountDownLatch countDownLatch2 = new CountDownLatch(CARS_COUNT);
        // Туннель может впустить только половину машин, но только целых :)
        int tunnelLimit = (int)Math.floor(CARS_COUNT / 2.0);
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(new Semaphore(tunnelLimit)), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), countDownLatch, countDownLatch2);
        }

        for (Car car : cars) {
            new Thread(car).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        try {
            countDownLatch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!! Победитель " + Car.winner.getName());
    }
}