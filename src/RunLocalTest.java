import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * A framework to run all public test cases for the project.
 *
 * @author Yihang Li
 * @version 4/6/2025
 */
public class RunLocalTest {
    public static void main(String[] args) {
        Class<?>[] classesToTest = {
                RatingDatabaseTest.class,
                MessageInterfaceTest.class,
                UserDatabaseTest.class,
                ItemDatabaseTest.class
        };

        boolean allTestsPassed = true;

        for (Class<?> testClass : classesToTest) {
            System.out.println("Running tests for: " + testClass.getSimpleName());
            Result result = JUnitCore.runClasses(testClass);
            if (!result.wasSuccessful()) {
                allTestsPassed = false;
                for (Failure failure : result.getFailures()) {
                    System.out.println(failure.toString());
                }
            } else {
                System.out.println(testClass.getSimpleName() + " passed all tests.");
            }
        }

        if (allTestsPassed) {
            System.out.println("Excellent - All tests ran successfully");
        } else {
            System.out.println("Some tests failed. Check the output above.");
        }
    }
}