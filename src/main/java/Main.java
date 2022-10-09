public class Main {

    final static int BUILD_TIME = 300;
    final static int NUMBER_OF_CLIENTS = 3;

    public static void main(String[] args) {
        Seller seller = new Seller();

        Runnable runnable = () -> {
            synchronized (seller) {
                String buyerName = Thread.currentThread().getName();
                System.out.println(buyerName + " зашел в автосалон");
                if (seller.getAvailableCars() == 0) {
                    try {
                        System.out.println("Нет доступных машин");
                        seller.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                seller.carSell();
                System.out.println(buyerName + " купил авто");
            }
        };

        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            new Thread(runnable, "Покупатель " + i).start();
        }
        new Thread(() -> {
            while (seller.getCarsSold() < NUMBER_OF_CLIENTS) {
                try {
                    Thread.sleep(BUILD_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (seller) {
                    seller.carBuild();
                    seller.notify();
                }
            }
        }).start();
    }
}

