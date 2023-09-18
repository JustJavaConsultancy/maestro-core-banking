import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import VehicleInsuranceRequestUpdatePage from './vehicle-insurance-request-update.page-object';

const expect = chai.expect;
export class VehicleInsuranceRequestDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('apiGatewayApp.vehicleInsuranceRequest.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-vehicleInsuranceRequest'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class VehicleInsuranceRequestComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('vehicle-insurance-request-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('vehicle-insurance-request');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateVehicleInsuranceRequest() {
    await this.createButton.click();
    return new VehicleInsuranceRequestUpdatePage();
  }

  async deleteVehicleInsuranceRequest() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const vehicleInsuranceRequestDeleteDialog = new VehicleInsuranceRequestDeleteDialog();
    await waitUntilDisplayed(vehicleInsuranceRequestDeleteDialog.deleteModal);
    expect(await vehicleInsuranceRequestDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /apiGatewayApp.vehicleInsuranceRequest.delete.question/
    );
    await vehicleInsuranceRequestDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(vehicleInsuranceRequestDeleteDialog.deleteModal);

    expect(await isVisible(vehicleInsuranceRequestDeleteDialog.deleteModal)).to.be.false;
  }
}
