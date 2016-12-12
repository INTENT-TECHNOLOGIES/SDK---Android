package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITDataResult;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITDataTest {
    @Test
    public void jsonToDataResultList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.data_results);
        Type listType = new TypeToken<List<ITDataResult>>() {
        }.getType();
        List<ITDataResult> dataResults = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(1, dataResults.size());
        assertEquals(ITData.Type.SNAPSHOT, dataResults.get(0).type);
        assertEquals(3, dataResults.get(0).data.size());
        assertEquals(1458061814574L, dataResults.get(0).data.get(0).timestamp);
        assertEquals(1458661878227L, dataResults.get(0).data.get(1).timestamp);
        assertEquals(1460049538627L, dataResults.get(0).data.get(2).timestamp);
        assertEquals(789.0, dataResults.get(0).data.get(0).value);
        assertEquals(456.7, dataResults.get(0).data.get(1).value);
        assertEquals(Double.NaN, dataResults.get(0).data.get(2).value);
        assertEquals(Double.NaN, dataResults.get(0).data.get(0).valueMin);
        assertEquals(Double.NaN, dataResults.get(0).data.get(1).valueMin);
        assertEquals(12.3, dataResults.get(0).data.get(2).valueMin);
        assertEquals(Double.NaN, dataResults.get(0).data.get(0).valueMax);
        assertEquals(Double.NaN, dataResults.get(0).data.get(1).valueMax);
        assertEquals(23.4, dataResults.get(0).data.get(2).valueMax);
        assertEquals(ITData.TrustLevel.TRUSTED_MANUAL, dataResults.get(0).data.get(0).trustLevel);
        assertEquals(ITData.TrustLevel.TRUSTED_MANUAL, dataResults.get(0).data.get(1).trustLevel);
        assertEquals(ITData.TrustLevel.TRUSTED_SENSOR, dataResults.get(0).data.get(2).trustLevel);
    }

    @Test
    public void jsonToTicketDataResultList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.ticket_data_results);
        Type listType = new TypeToken<List<ITDataResult>>() {
        }.getType();
        List<ITDataResult> dataResults = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(2, dataResults.size());
        assertEquals(ITData.Type.TICKET, dataResults.get(0).type);
        assertEquals(ITData.Type.TICKET, dataResults.get(1).type);
        assertEquals(14, dataResults.get(0).data.size());
        assertEquals("Electricity", dataResults.get(0).data.get(0).ticketCategory);
        assertEquals("07527471", dataResults.get(0).data.get(0).ticketCode);
        assertEquals("contrôle cellule hs prévoir retour pour remplacement réf flash 04504", dataResults.get(0).data.get(0).ticketComment);
        assertEquals("Operation", dataResults.get(0).data.get(0).ticketNature);
        assertEquals("TemporaryRepair", dataResults.get(0).data.get(0).ticketStatus);
        assertEquals("Nathan", dataResults.get(0).data.get(0).ticketTechnicianName);
        assertEquals(1479894000000L, dataResults.get(0).data.get(0).ticketCallDate);
        assertEquals(1480078200000L, dataResults.get(0).data.get(0).ticketStartDate);
        assertEquals(1480078800000L, dataResults.get(0).data.get(0).ticketEndDate);
        assertEquals(1480078800000L, dataResults.get(0).data.get(0).ticketPlannedDate);
        assertEquals(true, dataResults.get(0).data.get(0).ticketCustomerBasedDate);
    }
}
