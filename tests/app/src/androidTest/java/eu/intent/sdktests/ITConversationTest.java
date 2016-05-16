package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITAssetType;
import eu.intent.sdk.model.ITConversation;
import eu.intent.sdk.model.ITMessage;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class ITConversationTest {
    @Test
    public void jsonToConversation() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.conversation);
        ITConversation conversation = ITRetrofitUtils.getGson().fromJson(json, ITConversation.class);
        assertEquals("04a17890-1122-48e0-95f4-3cc696a3bc4d", conversation.assetId);
        assertEquals(ITAssetType.SITE, conversation.assetType);
        assertEquals("HEATING", conversation.activityCategory);
        assertEquals(1457987228299L, conversation.creationDate);
        assertEquals("Intent", conversation.creatorName);
        assertEquals(1458896339111L, conversation.lastUpdate);
        assertEquals("intent", conversation.creatorDomain);
        assertEquals(0L, conversation.lastRead);
        assertEquals(46, conversation.totalMessagesCount);
        assertEquals(4, conversation.messages.size());
        assertEquals("9004652e-59e5-4496-80a3-44a021a053a1", conversation.messages.get(0).id);
        assertEquals("f220ea4a-c1ad-4633-85c7-ad5e0ec62f22", conversation.messages.get(1).id);
        assertEquals("f177e29d-8839-489e-872d-81df302e9f20", conversation.messages.get(2).id);
        assertEquals("0f31b13e-978c-40a4-b304-084d718eb836", conversation.messages.get(3).id);
        assertEquals(ITMessage.MessageType.COMMENT, conversation.messages.get(0).type);
        assertEquals(ITMessage.MessageType.REPORTING, conversation.messages.get(1).type);
        assertEquals(ITMessage.MessageType.REPORTING, conversation.messages.get(2).type);
        assertEquals(ITMessage.MessageType.STATE, conversation.messages.get(3).type);
        assertEquals(null, conversation.messages.get(0).title);
        assertEquals("Signalement", conversation.messages.get(1).title);
        assertEquals("Trop chaud", conversation.messages.get(2).title);
        assertEquals("Trop grand écart de T°", conversation.messages.get(3).title);
        assertEquals("Test", conversation.messages.get(0).message);
        assertEquals("Signalement urgent", conversation.messages.get(1).message);
        assertEquals("Fait chaud!", conversation.messages.get(2).message);
        assertEquals("Écart de température trop important entre les logements d'un site (l'appartement le plus froid et le plus chaud ont un écart de température trop important)", conversation.messages.get(3).message);
        assertEquals("Administrateur Demo", conversation.messages.get(0).creatorName);
        assertEquals("Administrateur Demo", conversation.messages.get(1).creatorName);
        assertEquals("Administrateur Demo", conversation.messages.get(2).creatorName);
        assertEquals("Intent", conversation.messages.get(3).creatorName);
        assertEquals("demo1.intent", conversation.messages.get(0).creatorDomain);
        assertEquals("demo1.intent", conversation.messages.get(1).creatorDomain);
        assertEquals("demo1.intent", conversation.messages.get(2).creatorDomain);
        assertEquals("intent", conversation.messages.get(3).creatorDomain);
        assertEquals(1458896339107L, conversation.messages.get(0).creationDate);
        assertEquals(1458652668679L, conversation.messages.get(1).creationDate);
        assertEquals(1458602226043L, conversation.messages.get(2).creationDate);
        assertEquals(1457991858226L, conversation.messages.get(3).creationDate);
        assertEquals(1458896339107L, conversation.messages.get(0).lastUpdate);
        assertEquals(1458652668679L, conversation.messages.get(1).lastUpdate);
        assertEquals(1458602237279L, conversation.messages.get(2).lastUpdate);
        assertEquals(1457991858226L, conversation.messages.get(3).lastUpdate);
        assertEquals(false, conversation.messages.get(0).removed);
        assertEquals(false, conversation.messages.get(1).removed);
        assertEquals(false, conversation.messages.get(2).removed);
        assertEquals(false, conversation.messages.get(3).removed);
        assertNull(conversation.messages.get(0).reportingState);
        assertNull(conversation.messages.get(0).reportingStateHistory);
        assertNull(conversation.messages.get(0).state);
        assertEquals(ITMessage.ReportingState.OPEN, conversation.messages.get(1).reportingState);
        assertEquals(1, conversation.messages.get(1).reportingStateHistory.size());
        assertEquals(ITMessage.ReportingState.OPEN, conversation.messages.get(1).reportingStateHistory.get(0).state);
        assertEquals(1458652668679L, conversation.messages.get(1).reportingStateHistory.get(0).timestamp);
        assertEquals("urn:demo1.intent:9879a242-78d6-489e-a17e-4434fc05ef50", conversation.messages.get(1).reportingStateHistory.get(0).changerUserId);
        assertEquals("Administrateur Demo", conversation.messages.get(1).reportingStateHistory.get(0).changerName);
        assertNull(conversation.messages.get(1).state);
        assertEquals(ITMessage.ReportingState.CLOSED, conversation.messages.get(2).reportingState);
        assertEquals(2, conversation.messages.get(2).reportingStateHistory.size());
        assertEquals(ITMessage.ReportingState.OPEN, conversation.messages.get(2).reportingStateHistory.get(0).state);
        assertEquals(ITMessage.ReportingState.CLOSED, conversation.messages.get(2).reportingStateHistory.get(1).state);
        assertEquals(1458602226043L, conversation.messages.get(2).reportingStateHistory.get(0).timestamp);
        assertEquals(1458602235682L, conversation.messages.get(2).reportingStateHistory.get(1).timestamp);
        assertEquals("urn:demo1.intent:9879a242-78d6-489e-a17e-4434fc05ef50", conversation.messages.get(2).reportingStateHistory.get(0).changerUserId);
        assertEquals("urn:demo1.intent:9879a242-78d6-489e-a17e-4434fc05ef50", conversation.messages.get(2).reportingStateHistory.get(1).changerUserId);
        assertEquals("Administrateur Demo", conversation.messages.get(2).reportingStateHistory.get(0).changerName);
        assertEquals("Administrateur Demo", conversation.messages.get(2).reportingStateHistory.get(1).changerName);
        assertNull(conversation.messages.get(2).state);
        assertNull(conversation.messages.get(3).reportingState);
        assertNull(conversation.messages.get(3).reportingStateHistory);
        assertNotNull(conversation.messages.get(3).state);
    }
}
