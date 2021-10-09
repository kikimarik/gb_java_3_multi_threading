import java.util.concurrent.CountDownLatch;

public class Car implements Runnable {
    public static Car winner;
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    private CountDownLatch countDownLatch;
    private CountDownLatch countDownLatch2;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CountDownLatch countDownLatch, CountDownLatch countDownLatch2) {
        this.race = race;
        this.speed = speed;
        this.countDownLatch = countDownLatch;
        this.countDownLatch2 = countDownLatch2;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            countDownLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        countDownLatch2.countDown();
        if (countDownLatch2.getCount() == CARS_COUNT - 1) {
            Car.winner = this;
        }
    }
}