package eu.intent.sdktests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ITActivityTest.class,
        ITAddressTest.class,
        ITClassifiedAdTest.class,
        ITContactTest.class,
        ITConversationTest.class,
        ITDataTest.class,
        ITDeviceTest.class,
        ITEquipmentTest.class,
        ITGreenGestureTest.class,
        ITLocationTest.class,
        ITMessageTest.class,
        ITNewsTest.class,
        ITOperationTest.class,
        ITPartTest.class,
        ITSiteTest.class,
        ITStateParamsThresholdsTest.class,
        ITStateTemplateTest.class,
        ITStateTest.class,
        ITStreamTest.class,
        ITSubscriptionTest.class,
        ITTagTest.class,
        ITUserTest.class
})
public class AllTests {
}
