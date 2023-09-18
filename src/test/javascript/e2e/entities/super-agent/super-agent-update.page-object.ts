import { by, element, ElementFinder } from 'protractor';
import { isVisible, waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

export default class SuperAgentUpdatePage {
  pageTitle: ElementFinder = element(by.id('apiGatewayApp.superAgent.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  statusSelect: ElementFinder = element(by.css('select#super-agent-status'));
  agentSelect: ElementFinder = element(by.css('select#super-agent-agent'));
  schemeSelect: ElementFinder = element(by.css('select#super-agent-scheme'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setStatusSelect(status) {
    await this.statusSelect.sendKeys(status);
  }

  async getStatusSelect() {
    return this.statusSelect.element(by.css('option:checked')).getText();
  }

  async statusSelectLastOption() {
    await this.statusSelect.all(by.tagName('option')).last().click();
  }

  async agentSelectLastOption() {
    await this.agentSelect.all(by.tagName('option')).last().click();
  }

  async agentSelectOption(option) {
    await this.agentSelect.sendKeys(option);
  }

  getAgentSelect() {
    return this.agentSelect;
  }

  async getAgentSelectedOption() {
    return this.agentSelect.element(by.css('option:checked')).getText();
  }

  async schemeSelectLastOption() {
    await this.schemeSelect.all(by.tagName('option')).last().click();
  }

  async schemeSelectOption(option) {
    await this.schemeSelect.sendKeys(option);
  }

  getSchemeSelect() {
    return this.schemeSelect;
  }

  async getSchemeSelectedOption() {
    return this.schemeSelect.element(by.css('option:checked')).getText();
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
    await this.statusSelectLastOption();
    await this.agentSelectLastOption();
    await this.schemeSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
