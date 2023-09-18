import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import ApprovalWorkflowUpdatePage from './approval-workflow-update.page-object';

const expect = chai.expect;
export class ApprovalWorkflowDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('apiGatewayApp.approvalWorkflow.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-approvalWorkflow'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ApprovalWorkflowComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('approval-workflow-heading'));
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
    await navBarPage.getEntityPage('approval-workflow');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateApprovalWorkflow() {
    await this.createButton.click();
    return new ApprovalWorkflowUpdatePage();
  }

  async deleteApprovalWorkflow() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const approvalWorkflowDeleteDialog = new ApprovalWorkflowDeleteDialog();
    await waitUntilDisplayed(approvalWorkflowDeleteDialog.deleteModal);
    expect(await approvalWorkflowDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /apiGatewayApp.approvalWorkflow.delete.question/
    );
    await approvalWorkflowDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(approvalWorkflowDeleteDialog.deleteModal);

    expect(await isVisible(approvalWorkflowDeleteDialog.deleteModal)).to.be.false;
  }
}
