package eu.intent.sdk

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        ITActivityTest::class,
        ITAddressTest::class,
        ITApiCallbackTest::class,
        ITAuthHelperTest::class,
        ITClassifiedAdTest::class,
        ITClientTest::class,
        ITContactTest::class,
        ITConversationTest::class,
        ITDataTest::class,
        ITDateUtilsTest::class,
        ITEnvironmentTest::class,
        ITEquipmentTest::class,
        ITJsonUtilsTest::class,
        ITLocationTest::class,
        ITMessageTest::class,
        ITNewsTest::class,
        ITOAuthManagerTest::class,
        ITOAuthTokenTest::class,
        ITOperationSearchCriteriaTest::class,
        ITParcelableUtilsTest::class,
        ITPartTest::class,
        ITSessionManagerTest::class,
        ITSiteTest::class,
        ITStateParamsThresholdsTest::class,
        ITStateTemplateTest::class,
        ITStateTest::class,
        ITStreamTest::class,
        ITSubscriptionTest::class,
        ITTagTest::class,
        ITTicketTest::class,
        ITUserTest::class)
class TestSuite