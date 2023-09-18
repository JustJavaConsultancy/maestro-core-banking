import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import ApprovalGroupUpdatePage from './approval-group-update.page-object';

const expect = chai.expect;
export class ApprovalGroupDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('apiGatewayApp.approvalGroup.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-approvalGroup'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ApprovalGroupComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('approval-group-heading'));
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
    await navBarPage.getEntityPage('approval-group');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateApprovalGroup() {
    await this.createButton.click();
    return new ApprovalGroupUpdatePage();
  }

  async deleteApprovalGroup() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const approvalGroupDeleteDialog = new ApprovalGroupDeleteDialog();
    await waitUntilDisplayed(approvalGroupDeleteDialog.deleteModal);
    expect(await approvalGroupDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/apiGatewayApp.approvalGroup.delete.question/);
    await approvalGroupDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(approvalGroupDeleteDialog.deleteModal);

    expect(await isVisible(approvalGroupDeleteDialog.deleteModal)).to.be.false;
  }
}
