package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITEquipment;
import eu.intent.sdk.model.ITEquipmentList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ITEquipmentTest {
    @Test
    public void jsonToEquipmentList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.equipments);
        ITEquipmentList equipmentList = ITRetrofitUtils.getGson().fromJson(json, ITEquipmentList.class);
        assertEquals(3, equipmentList.totalCount);
        assertEquals(3, equipmentList.equipments.size());
        assertEquals(0.8337478999999348, equipmentList.equipments.get(0).address.location.lng);
        assertEquals(49.5809623, equipmentList.equipments.get(0).address.location.lat);
        assertEquals("ELM Leblanc", equipmentList.equipments.get(2).brand);
        assertEquals("VMC-099-00012", equipmentList.equipments.get(0).externalRef);
        assertEquals("89dbf4a0-69dd-4091-b777-dc0ff19eb8f8", equipmentList.equipments.get(0).id);
        assertEquals("Chauffe-eau Porte 20", equipmentList.equipments.get(2).label);
        assertEquals("Test", equipmentList.equipments.get(0).model);
        assertEquals(ITEquipment.Scope.COMMON, equipmentList.equipments.get(0).scope);
        assertEquals(ITEquipment.Scope.COMMON, equipmentList.equipments.get(1).scope);
        assertEquals(ITEquipment.Scope.PRIVATE, equipmentList.equipments.get(2).scope);
        assertEquals("0Z902729837", equipmentList.equipments.get(2).serialNumber);
        assertEquals(1325631600000L, equipmentList.equipments.get(2).serviceDate);
        assertEquals(ITEquipment.Type.CMV, equipmentList.equipments.get(0).type);
        assertEquals(ITEquipment.Type.COMMUNITY_BOILER, equipmentList.equipments.get(1).type);
        assertEquals(ITEquipment.Type.INDIVIDUAL_BOILER, equipmentList.equipments.get(2).type);
        assertEquals(0, equipmentList.equipments.get(2).uninstallDate);
    }

    @Test
    public void testIncompleteEquipment() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.equipment_unknown);
        ITEquipment equipment = ITRetrofitUtils.getGson().fromJson(json, ITEquipment.class);
        assertNotNull(equipment.address);
        assertFalse(equipment.address.location.isValid());
        assertEquals(ITEquipment.Scope.UNKNOWN, equipment.scope);
        assertEquals(ITEquipment.Type.UNKNOWN, equipment.type);
    }
}
