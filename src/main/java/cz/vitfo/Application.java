package cz.vitfo;

import cz.vitfo.dao.PaymentDao;
import cz.vitfo.dao.impl.PaymentDaoImpl;
import cz.vitfo.model.Payment;
import cz.vitfo.util.InputOutputUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public final class Application {
    private Logger logger = Logger.getLogger(Application.class.getName());

    private static final Scanner SCANNER = new Scanner(System.in).useDelimiter("\n");
    private static final int REFRESH_PERIOD = 60_000;
    private static final String HELP =
            "-------------------------------------\n" +
            "save - Saves all payments to the file\n" +
            "quit - Quits the application\n" +
            "show - Shows all payments (amount is > 0)\n" +
            "help - Shows help\n" +
            "input e.g. CZK 100 - saves input\n" +
            "-------------------------------------\n";

    private static Application application = null;

    private Application() {}

    public static synchronized Application getInstance() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    /**
     * Starts the application.
     *
     * @param args array of command line arguments
     */
    public void start(String[] args) {
        final PaymentDao paymentDao = new PaymentDaoImpl();

        boolean correctInit = init(args, paymentDao);

        if (correctInit) {
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    final List<Payment> all = paymentDao.findAll();
                    InputOutputUtil.printListToOutput(all);
                    System.out.println("$>");
                }
            }, 0, REFRESH_PERIOD);

            boolean isApplicationRunning = true;
            while (isApplicationRunning && SCANNER.hasNext()) {
                final String next = SCANNER.next().trim();
                switch (next) {
                    case "save":
                        InputOutputUtil.saveListToFile(paymentDao.findAll());
                        break;
                    case "quit":
                        logger.warning("Finishing application");
                        timer.cancel();
                        isApplicationRunning = false;
                        break;
                    case "show":
                        InputOutputUtil.printListToOutput(paymentDao.findAll());
                        break;
                    case "help":
                        System.out.println(HELP);
                        System.out.println("$>");
                        break;
                    default:
                        readInput(next, paymentDao);
                        break;
                }
            }
        } else {
            logger.severe("Application cannot be started due to wrong number of arguments.");
        }
    }

    /**
     * Checks the number of input arguments and loads input file with payments to the memory.
     *
     * @param args arguments
     * @param paymentDao payment dao
     * @return if the number of input arguments was correct
     */
    private boolean init(String[] args, PaymentDao paymentDao) {
        if (args.length != 0) {
            if (args.length == 1) {
                final List<Payment> payments = readInputFile(args[0]);
                paymentDao.save(payments);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private List<Payment> readInputFile(String fileName) {
        final File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return InputOutputUtil.readFileToList(fileName);
        } else {
            logger.severe("Specified file doesn't exist.");
            return new ArrayList<>();
        }
    }

    private void readInput(String input, PaymentDao paymentDao) {
        final Optional<Payment> payment = InputOutputUtil.readInput(input);
        if (payment.isPresent()) {
            paymentDao.save(payment.get());
        } else {
            System.out.println("Invalid input (currency amount e.g. CZK 100)");
        }
    }
}
