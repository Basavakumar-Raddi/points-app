import { Component, OnInit, AfterViewInit } from '@angular/core';
import { GridOptions } from 'ag-grid-community';
import {PointService } from '../point.service';
import * as _ from 'lodash';

@Component({
  selector: 'app-view-component',
  templateUrl: './view-component.component.html',
  styleUrls: ['./view-component.component.css']
})
export class ViewComponentComponent implements OnInit, AfterViewInit {
  gridOptions: GridOptions;
  columnDef = [];
  rowData = [];
  errorMsg: string = '';
  successMsg: string = '';
  errorFlag: boolean = false;
  successFlag: boolean = false;
  showSuccessMsg: boolean = true;
  response: Object = null;
  minValue: number = 0;
  averageValue: number = 0;
  maxValue: number = 0;
  totalValue: number = 0;
  rowSelected: boolean = false;

  constructor(private pointService : PointService) { 
    this.gridOptions = {
      columnDefs: this.columnDef,
      rowData: this.rowData,
      rowHeight: 32,
      headerHeight: 38,
      enableSorting: true,
      enableFilter: true,
      rowSelection: 'single',
      suppressAnimationFrame: true,
      suppressMenuHide: true,
      onSelectionChanged: this.onSelectionChanged.bind(this)
    };
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.columnDef = [
      {headerName: 'MeasurementDay', field: 'measurementDay', suppressFilter: true},
      {headerName: 'MeasurementLocation', field: 'location', filter: "agTextColumnFilter"},
      {headerName: 'MeasurementValue', field: 'value', suppressFilter: true}
    ];

    this.pointService.getPoints().subscribe(response => {
      if (!response.success) {
        this.errorFlag = true;
        this.errorMsg = response.message;
      } else {
        this.response = response.response;
        this.rowData = [];
        for (var columnEntry in response.response.pointDTOS) {
          this.rowData.push(response.response.pointDTOS[columnEntry]);
        }
        this.minValue = response.response.minValue;
        this.averageValue = response.response.averageValue;
        this.maxValue = response.response.maxValue;
        this.totalValue = response.response.totalValue;
        this.successFlag = true;
        this.showSuccessMsg = false;
        this.successMsg = response.message;
        this.gridOptions.api.setColumnDefs(this.columnDef);
        this.gridOptions.api.setRowData(this.rowData);
        this.gridOptions.api.sizeColumnsToFit();
      }
    }, error => {
      this.errorFlag = true;
      this.errorMsg = "Data Fetch failed";
    });
  }

  deletePoint() {
    var id=this.gridOptions.api.getSelectedRows();
    console.log(id);
    if(id){
      this.pointService.deleteUser(id[0]['id']).subscribe(response => {
        if (!response.success) {
          this.errorFlag = true;
          this.errorMsg = response.message;
          alert(this.errorMsg);
        } else {
          this.successFlag = true;
          this.showSuccessMsg = false;
          this.successMsg = response.message;
          this.updateGridData(id[0]['id']);
          alert(this.successMsg );
        }
      }, error => {
        this.errorFlag = true;
        this.errorMsg = "delete failed";
      });
    }
  }

  updateGridData(id){
    _.remove(this.rowData, {id: id});
    this.gridOptions.api.setRowData(this.rowData);
    this.gridOptions.api.sizeColumnsToFit();
  }

  onSelectionChanged(){
    this.rowSelected = true
  }

}
