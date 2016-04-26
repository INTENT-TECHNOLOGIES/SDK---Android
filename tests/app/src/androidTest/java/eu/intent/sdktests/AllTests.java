package eu.intent.sdktests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ITActivityTest.class,
        ITClassifiedAdTest.class,
        ITContactTest.class,
        ITConversationTest.class,
        ITDataTest.class,
        ITDeviceTest.class,
        ITEquipmentTest.class,
        ITGreenGestureTest.class,
        ITMessageTest.class
})
public class AllTests {
}
