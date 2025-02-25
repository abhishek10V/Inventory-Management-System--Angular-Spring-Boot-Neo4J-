package com.example.inventory.service;

import com.example.inventory.model.Device;
import com.example.inventory.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    private Device sampleDevice;

    @BeforeEach
    void setUp() {
        sampleDevice = new Device();
        sampleDevice.setId(1L);
        sampleDevice.setName("Device 1");
        sampleDevice.setDeviceType("Sensor");
    }

    @Test
    void testSaveDevice_Success() {
        when(deviceRepository.save(any(Device.class))).thenReturn(sampleDevice);

        Device savedDevice = deviceService.saveDevice(sampleDevice);

        assertNotNull(savedDevice);
        assertEquals(1L, savedDevice.getId());
        assertEquals("Device 1", savedDevice.getName());
        verify(deviceRepository, times(1)).save(sampleDevice);
    }

    @Test
    void testGetDevice_Success() {
        List<Device> devices = new ArrayList<>();
        devices.add(sampleDevice);

        when(deviceRepository.findAll()).thenReturn(devices);

        List<Device> result = deviceService.getDevice();

        assertEquals(1, result.size());
        assertEquals("Device 1", result.get(0).getName());
        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void testDeleteDevice_Success() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));
        doNothing().when(deviceRepository).deleteById(1L);

        Device deletedDevice = deviceService.deleteDevice(1L);

        assertNotNull(deletedDevice);
        assertEquals(1L, deletedDevice.getId());
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDevice_NotFound() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        Device deletedDevice = deviceService.deleteDevice(2L);

        assertNull(deletedDevice);
        verify(deviceRepository, times(1)).findById(2L);
        verify(deviceRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetDeviceById_Success() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));

        Device foundDevice = deviceService.getDeviceById(1L);

        assertNotNull(foundDevice);
        assertEquals("Device 1", foundDevice.getName());
        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDeviceById_NotFound() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        Device foundDevice = deviceService.getDeviceById(2L);

        assertNull(foundDevice);
        verify(deviceRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateDevice_Success() {
        Device updatedData = new Device();
        updatedData.setName("Updated Device");
        updatedData.setDeviceType("Updated Type");

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(sampleDevice);

        Device updatedDevice = deviceService.updateDevice(1L, updatedData);

        assertNotNull(updatedDevice);
        assertEquals("Updated Device", updatedDevice.getName());
        assertEquals("Updated Type", updatedDevice.getDeviceType());
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void testUpdateDevice_NotFound() {
        Device updatedData = new Device();
        updatedData.setName("New Name");

        when(deviceRepository.findById(2L)).thenReturn(Optional.empty());

        Device updatedDevice = deviceService.updateDevice(2L, updatedData);

        assertNull(updatedDevice);
        verify(deviceRepository, times(1)).findById(2L);
        verify(deviceRepository, never()).save(any(Device.class));
    }
}
