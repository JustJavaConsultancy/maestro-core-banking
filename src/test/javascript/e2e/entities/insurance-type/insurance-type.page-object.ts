import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import InsuranceTypeUpdatePage from './insurance-type-update.page-object';

const expect = chai.expect;
export class InsuranceTypeDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('apiGatewayApp.insuranceType.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-insuranceType'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class InsuranceTypeComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('insurance-type-heading'));
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
    await navBarPage.getEntityPage('insurance-type');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateInsuranceType() {
    await this.createButton.click();
    return new InsuranceTypeUpdatePage();
  }

  async deleteInsuranceType() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const insuranceTypeDeleteDialog = new InsuranceTypeDeleteDialog();
    await waitUntilDisplayed(insuranceTypeDeleteDialog.deleteModal);
    expect(await insuranceTypeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/apiGatewayApp.insuranceType.delete.question/);
    await insuranceTypeDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(insuranceTypeDeleteDialog.deleteModal);

    expect(await isVisible(insuranceTypeDeleteDialog.deleteModal)).to.be.false;
  }
}
