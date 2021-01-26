package hepsiburadaQA;

import com.google.common.collect.ImmutableList;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;

abstract public class BaseTest {

    private static final Duration SCROLL_DUR = Duration.ofMillis(1000);
    private static final double SCROLL_RATIO = 0.3;
    private static final int ANDROID_SCROLL_DIVISOR = 3;
    protected AppiumDriver<MobileElement> driver;
    private Dimension windowSize;

    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    @After
    public void tearDown() {
        try {
            getDriver().quit();
        } catch (Exception exception) {
        }
    }

    private Dimension getWindowSize() {
        if (windowSize == null) {
            windowSize = getDriver().manage().window().getSize();
        }
        return windowSize;
    }

    protected void swipe(Point start, Point end, Duration duration) {
        AppiumDriver<MobileElement> d = getDriver();
        boolean isAndroid = d instanceof AndroidDriver<?>;

        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        if (isAndroid) {
            duration = duration.dividedBy(ANDROID_SCROLL_DIVISOR);
        } else {
            swipe.addAction(new Pause(input, duration));
            duration = Duration.ZERO;
        }
        swipe.addAction(input.createPointerMove(duration, Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
        d.perform(ImmutableList.of(swipe));
    }

    protected void swipe(double startX, double startY, double endX, double endY, Duration duration) {
        Dimension size = getWindowSize();
        Point start = new Point((int) (size.width * startX), (int) (size.height * startY));
        Point end = new Point((int) (size.width * endX), (int) (size.height * endY));
        swipe(start, end, duration);
    }

    protected void scroll(ScrollDirection direction, double distance) {
        if (distance < 0 || distance > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }
        Dimension size = getWindowSize();
        Point midPoint = new Point((int) (size.width * 0.5), (int) (size.height * 0.5));
        int top = midPoint.y - (int) ((size.height * distance) * 0.5);
        int bottom = midPoint.y + (int) ((size.height * distance) * 0.5);
        int left = midPoint.x - (int) ((size.width * distance) * 0.5);
        int right = midPoint.x + (int) ((size.width * distance) * 0.5);
        if (direction == ScrollDirection.UP) {
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);
        } else if (direction == ScrollDirection.DOWN) {
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);
        } else if (direction == ScrollDirection.LEFT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);
        } else { // Right
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);
        }
    }

    protected void scroll(ScrollDirection direction) {
        scroll(direction, SCROLL_RATIO);
    }

    protected void scroll() {
        scroll(ScrollDirection.DOWN, SCROLL_RATIO);
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
