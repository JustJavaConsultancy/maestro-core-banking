import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class ApprovalWorkflowUpdatePage {
  pageTitle: ElementFinder = element(by.id('apiGatewayApp.approvalWorkflow.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#approval-workflow-name'));
  initiatorSelect: ElementFinder = element(by.css('select#approval-workflow-initiator'));
  approverSelect: ElementFinder = element(by.css('select#approval-workflow-approver'));
  transactionTypeSelect: ElementFinder = element(by.css('select#approval-workflow-transactionType'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async initiatorSelectLastOption() {
    await this.initiatorSelect.all(by.tagName('option')).last().click();
  }

  async initiatorSelectOption(option) {
    await this.initiatorSelect.sendKeys(option);
  }

  getInitiatorSelect() {
    return this.initiatorSelect;
  }

  async getInitiatorSelectedOption() {
    return this.initiatorSelect.element(by.css('option:checked')).getText();
  }

  async approverSelectLastOption() {
    await this.approverSelect.all(by.tagName('option')).last().click();
  }

  async approverSelectOption(option) {
    await this.approverSelect.sendKeys(option);
  }

  getApproverSelect() {
    return this.approverSelect;
  }

  async getApproverSelectedOption() {
    return this.approverSelect.element(by.css('option:checked')).getText();
  }

  async transactionTypeSelectLastOption() {
    await this.transactionTypeSelect.all(by.tagName('option')).last().click();
  }

  async transactionTypeSelectOption(option) {
    await this.transactionTypeSelect.sendKeys(option);
  }

  getTransactionTypeSelect() {
    return this.transactionTypeSelect;
  }

  async getTransactionTypeSelectedOption() {
    return this.transactionTypeSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setNameInput('name');
    expect(await this.getNameInput()).to.match(/name/);
    await this.initiatorSelectLastOption();
    await this.approverSelectLastOption();
    await this.transactionTypeSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
