package com.example.inventory.service;

import com.example.inventory.Exception.EntityNotFoundException;
import com.example.inventory.model.Device;
import com.example.inventory.model.ShelfPositionVO;
import com.example.inventory.model.ShelfVO;
import com.example.inventory.repository.DeviceRepository;
import com.example.inventory.repository.ShelfPositionRepository;
import com.example.inventory.repository.ShelfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private ShelfRepository shelfRepo;
    @Mock
    private ShelfPositionRepository shelfPosRepo;
    @Mock
    private DeviceRepository deviceRepo;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private ShelfVO sampleShelf;
    private ShelfPositionVO sampleShelfPos;
    private Device sampleDevice;

    @BeforeEach
    void setUp() {
        // Initialize Device
        sampleDevice = new Device();
        sampleDevice.setId(1L);
        sampleDevice.setName("Device 1");
        sampleDevice.setDeviceType("Router");

        // ✅ Initialize the shelfPos list to prevent NullPointerException
        sampleDevice.setShelfPos(new ArrayList<>());

        // Initialize Shelf Position
        sampleShelfPos = new ShelfPositionVO();
        sampleShelfPos.setId(1L);
        sampleShelfPos.setName("A1");
        sampleShelfPos.setDeviceId(1L);  // Associate it with a device

        // Initialize Shelf
        sampleShelf = new ShelfVO();
        sampleShelf.setId(1L);
        sampleShelf.setName("Sample Shelf");
        sampleShelf.setShelfType("Storage");
        sampleShelf.setShelfPositionId(1L); // Associate with a Shelf Position

        // Associate Shelf Position with Shelf
        sampleShelfPos.setShelf(sampleShelf);
    }

    // ✅ Test for saveShelf()
    @Test
    void testSaveShelf() {
        when(shelfRepo.save(sampleShelf)).thenReturn(sampleShelf);
        ShelfVO savedShelf = inventoryService.saveShelf(sampleShelf);
        assertNotNull(savedShelf);
        assertEquals(1L, savedShelf.getId());
        verify(shelfRepo, times(1)).save(sampleShelf);
    }

    // ✅ Test for saveShelfPosition()
    @Test
    void testSaveShelfPosition() {
        when(shelfPosRepo.save(sampleShelfPos)).thenReturn(sampleShelfPos);
        ShelfPositionVO savedShelfPos = inventoryService.saveShelfPosition(sampleShelfPos);
        assertNotNull(savedShelfPos);
        assertEquals("A1", savedShelfPos.getName());
        verify(shelfPosRepo, times(1)).save(sampleShelfPos);
    }

    // ✅ Test for getShelf()
    @Test
    void testGetShelf() {
        when(shelfRepo.findAll()).thenReturn(Arrays.asList(sampleShelf));
        List<ShelfVO> shelves = inventoryService.getShelf();
        assertEquals(1, shelves.size());
        verify(shelfRepo, times(1)).findAll();
    }

    // ✅ Test for getShelfPosition()
    @Test
    void testGetShelfPosition() {
        when(shelfPosRepo.findAll()).thenReturn(Arrays.asList(sampleShelfPos));
        List<ShelfPositionVO> shelfPositions = inventoryService.getShelfPosition();
        assertEquals(1, shelfPositions.size());
        verify(shelfPosRepo, times(1)).findAll();
    }

    // ✅ Test for getShelfById() - Success Case
    @Test
    void testGetShelfById_Success() {
        when(shelfRepo.findById(1L)).thenReturn(Optional.of(sampleShelf));
        ShelfVO foundShelf = inventoryService.getShelfById(1L);
        assertNotNull(foundShelf);
        assertEquals("Sample Shelf", foundShelf.getName());
        verify(shelfRepo, times(1)).findById(1L);
    }

    // ✅ Test for getShelfById() - Not Found Case
    @Test
    void testGetShelfById_NotFound() {
        when(shelfRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> inventoryService.getShelfById(1L));
    }

    // ✅ Test for getShelfPositionById() - Success Case
    @Test
    void testGetShelfPositionById_Success() {
        when(shelfPosRepo.findById(1L)).thenReturn(Optional.of(sampleShelfPos));
        ShelfPositionVO foundShelfPos = inventoryService.getShelfPositionById(1L);
        assertNotNull(foundShelfPos);
        assertEquals("A1", foundShelfPos.getName());
        verify(shelfPosRepo, times(1)).findById(1L);
    }

    // ✅ Test for getShelfPositionById() - Not Found Case
    @Test
    void testGetShelfPositionById_NotFound() {
        when(shelfPosRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> inventoryService.getShelfPositionById(1L));
    }

    // ✅ Test for addShelfPositionToDevice() - Success Case
    @Test
    void testAddShelfPositionToDevice_Success() {
        // Mock repositories to return our sample data
        when(deviceRepo.findById(1L)).thenReturn(Optional.of(sampleDevice));
        when(shelfPosRepo.findById(1L)).thenReturn(Optional.of(sampleShelfPos));

        // Call the method to be tested
        inventoryService.addShelfPositionToDevice(1L, 1L);

        // Verify the interactions
        verify(deviceRepo, times(1)).findById(1L);
        verify(shelfPosRepo, times(1)).findById(1L);
        verify(deviceRepo, times(1)).save(sampleDevice);

        // ✅ Assert that shelf position is correctly added to the device
        assertTrue(sampleDevice.getShelfPos().contains(sampleShelfPos), "Shelf Position should be added to the Device");
    }

    // ✅ Test for addShelfPositionToDevice() - Device Not Found
    @Test
    void testAddShelfPositionToDevice_DeviceNotFound() {
        when(deviceRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> inventoryService.addShelfPositionToDevice(1L, 1L));
    }

    // ✅ Test for addShelfToShelfPosition() - Success Case
    @Test
    void testAddShelfToShelfPosition_Success() {
        when(shelfRepo.findById(1L)).thenReturn(Optional.of(sampleShelf));
        when(shelfPosRepo.findById(1L)).thenReturn(Optional.of(sampleShelfPos));

        inventoryService.addShelfToShelfPosition(1L, 1L);

        verify(shelfRepo, times(1)).save(sampleShelf);
        verify(shelfPosRepo, times(1)).save(sampleShelfPos);
    }

    // ✅ Test for addShelfToShelfPosition() - Shelf Position Already Assigned
    @Test
    void testAddShelfToShelfPosition_AlreadyAssigned() {
        sampleShelfPos.setShelf(sampleShelf);
        when(shelfRepo.findById(1L)).thenReturn(Optional.of(sampleShelf));
        when(shelfPosRepo.findById(1L)).thenReturn(Optional.of(sampleShelfPos));

        assertThrows(IllegalStateException.class, () -> inventoryService.addShelfToShelfPosition(1L, 1L));
    }
}
