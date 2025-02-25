import { NgFor } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InventoryService } from '../inventory.service';
import { ShelfPositionV0 } from './shelfPositionv0.model';
import { ShelfV0 } from './shelfv0.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [FormsModule, NgFor],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss',
})
export class InventoryComponent {
  private toastr = inject(ToastrService); // Inject ToastrService

  constructor(private inventoryService: InventoryService) {}

  showForm = false;
  formType: 'saveShelf' | 'getShelf' | 'saveShelfPosition' | 'getShelfPosition' | 'addShelfToShelfPosition' | 'addShelfPositionToDevice' = 'saveShelf';
  deviceId: number = NaN;
  sheflv0Id: number = NaN;
  shelfPositionv0Id: number = NaN;
  shelfv0: ShelfV0 = { id: NaN, name: '', shelfType: '', shelfPositionId: NaN };
  shelfPositionv0: ShelfPositionV0 = { id: NaN, name: '', deviceId: NaN };
  shelves: ShelfV0[] = [];
  shelfPositions: ShelfPositionV0[] = [];

  showToastSuccess(message: string) {
    this.toastr.success(message, 'Success');
  }

  showToastError(message: string) {
    this.toastr.error(message, 'Error');
  }

  hideform() {
    this.showForm = false;
    this.deviceId = NaN;
    this.sheflv0Id = NaN;
    this.shelfPositionv0Id = NaN;
    this.shelfv0 = { id: NaN, name: '', shelfType: '', shelfPositionId: NaN };
    this.shelfPositionv0 = { id: NaN, name: '', deviceId: NaN };
  }

  onSubmitSaveShelf() {
    this.inventoryService.saveShelf(this.shelfv0).subscribe(
      (response) => {
        console.log('Shelf saved: ', response);
        this.showToastSuccess('Shelf saved successfully!');
        this.fetchAllShelves();
        this.hideform();
      },
      (error) => {
        console.error('Error saving device: ', error);
        this.showToastError('Error saving shelf!');
      }
    );
  }

  onSubmitSaveShelfPosition() {
    this.inventoryService.saveShelfPosition(this.shelfPositionv0).subscribe(
      (response) => {
        console.log('Saved Shelf Position ', response);
        this.showToastSuccess('Shelf Position saved successfully!');
        this.fetchAllShelfPositions();
        this.hideform();
      },
      (error) => {
        console.error('Error saving Shelf Position ', error);
        this.showToastError('Error saving Shelf Position!');
      }
    );
  }

  onSubmitGetShelf() {
    this.inventoryService.getShelf(this.sheflv0Id).subscribe(
      (response) => {
        console.log('Shelf retrieved', response);
        this.showToastSuccess('Shelf retrieved successfully!');
        this.hideform();
      },
      (error) => {
        console.error('Error fetching shelf ', error);
        this.showToastError('Error fetching shelf!');
      }
    );
  }

  onSubmitGetShelfPosition() {
    this.inventoryService.getShelfPosition(this.shelfPositionv0Id).subscribe(
      (response) => {
        console.log('ShelfPosition retrieved: ', response);
        this.showToastSuccess('Shelf Position retrieved successfully!');
        this.hideform();
      },
      (error) => {
        console.error('Error fetching Shelf Position', error);
        this.showToastError('Error fetching Shelf Position!');
      }
    );
  }

  onSubmitAddShelfPositonToDevice() {
    this.inventoryService.addShelfPositionToDevice(this.deviceId, this.shelfPositionv0Id).subscribe(
      (response) => {
        const responseMessage: string = `Added ShelfPosition with shelfPositionId: ${this.shelfPositionv0Id} to deviceId: ${this.deviceId}`;
        console.log(responseMessage);
        this.showToastSuccess('Shelf Position added to device successfully!');
        this.fetchAllShelfPositions();
        this.hideform();
      },
      (error) => {
        console.error('Error Adding shelf position to device', error);
        this.showToastError('Error adding Shelf Position to device!');
      }
    );
  }

  onSubmitAddShelfToShelfPositon() {
    this.inventoryService.addShelfToShelfPosition(this.shelfPositionv0Id, this.sheflv0Id,).subscribe(
      (response) => {
        const responseMessage: string = `Added Shelf with shelfId: ${this.sheflv0Id} to Shelf Position with shelfPositionId: ${this.shelfPositionv0Id}`;
        console.log(responseMessage);
        this.showToastSuccess('Shelf added to Shelf Position successfully!');
        this.fetchAllShelves();
        this.hideform();
      },
      (error) => {
        console.error('Error adding shelf to shelf position', error);
        this.showToastError('Error adding shelf to Shelf Position!');
      }
    );
  }

  fetchAllShelves() {
    this.inventoryService.getAllShelves().subscribe(
      (response: ShelfV0[]) => {
        this.shelves = response;
        console.log('Shelves fetched successfully:', this.shelves);
        this.showToastSuccess('Shelves fetched successfully!');
      },
      (error) => {
        console.error('Error fetching shelves:', error);
        this.showToastError('Error fetching shelves!');
      }
    );
  }

  fetchAllShelfPositions() {
    this.inventoryService.getAllShelfPositions().subscribe(
      (response: ShelfPositionV0[]) => {
        this.shelfPositions = response;
        console.log('Shelf Positions fetched successfully:', this.shelfPositions);
        this.showToastSuccess('Shelf Positions fetched successfully!');
      },
      (error) => {
        console.error('Error fetching shelf positions:', error);
        this.showToastError('Error fetching Shelf Positions!');
      }
    );
  }
}
