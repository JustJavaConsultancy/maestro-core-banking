import { by, element, ElementArrayFinder, ElementFinder } from 'protractor';

import { click, isVisible, waitUntilAnyDisplayed, waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import SuperAgentUpdatePage from './super-agent-update.page-object';

const expect = chai.expect;

export class SuperAgentDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('apiGatewayApp.superAgent.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-superAgent'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class SuperAgentComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('super-agent-heading'));
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
    await navBarPage.getEntityPage('super-agent');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateSuperAgent() {
    await this.createButton.click();
    return new SuperAgentUpdatePage();
  }

  async deleteSuperAgent() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const superAgentDeleteDialog = new SuperAgentDeleteDialog();
    await waitUntilDisplayed(superAgentDeleteDialog.deleteModal);
    expect(await superAgentDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/apiGatewayApp.superAgent.delete.question/);
    await superAgentDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(superAgentDeleteDialog.deleteModal);

    expect(await isVisible(superAgentDeleteDialog.deleteModal)).to.be.false;
  }
}
