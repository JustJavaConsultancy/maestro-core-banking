import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class VehicleInsuranceRequestUpdatePage {
  pageTitle: ElementFinder = element(by.id('apiGatewayApp.vehicleInsuranceRequest.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  operationSelect: ElementFinder = element(by.css('select#vehicle-insurance-request-operation'));
  policyNoInput: ElementFinder = element(by.css('input#vehicle-insurance-request-policyNo'));
  certificateNoInput: ElementFinder = element(by.css('input#vehicle-insurance-request-certificateNo'));
  occupationInput: ElementFinder = element(by.css('input#vehicle-insurance-request-occupation'));
  sectorInput: ElementFinder = element(by.css('input#vehicle-insurance-request-sector'));
  idTypeInput: ElementFinder = element(by.css('input#vehicle-insurance-request-idType'));
  idNumberInput: ElementFinder = element(by.css('input#vehicle-insurance-request-idNumber'));
  vehicleTypeInput: ElementFinder = element(by.css('input#vehicle-insurance-request-vehicleType'));
  registrationNoInput: ElementFinder = element(by.css('input#vehicle-insurance-request-registrationNo'));
  vehMakeInput: ElementFinder = element(by.css('input#vehicle-insurance-request-vehMake'));
  vehModelInput: ElementFinder = element(by.css('input#vehicle-insurance-request-vehModel'));
  vehYearInput: ElementFinder = element(by.css('input#vehicle-insurance-request-vehYear'));
  registrationStartInput: ElementFinder = element(by.css('input#vehicle-insurance-request-registrationStart'));
  expiryDateInput: ElementFinder = element(by.css('input#vehicle-insurance-request-expiryDate'));
  mileageInput: ElementFinder = element(by.css('input#vehicle-insurance-request-mileage'));
  vehColorInput: ElementFinder = element(by.css('input#vehicle-insurance-request-vehColor'));
  chassisNoInput: ElementFinder = element(by.css('input#vehicle-insurance-request-chassisNo'));
  engineNoInput: ElementFinder = element(by.css('input#vehicle-insurance-request-engineNo'));
  engineCapacityInput: ElementFinder = element(by.css('input#vehicle-insurance-request-engineCapacity'));
  seatCapacityInput: ElementFinder = element(by.css('input#vehicle-insurance-request-seatCapacity'));
  balanceInput: ElementFinder = element(by.css('input#vehicle-insurance-request-balance'));
  profileSelect: ElementFinder = element(by.css('select#vehicle-insurance-request-profile'));
  insuranceTypeSelect: ElementFinder = element(by.css('select#vehicle-insurance-request-insuranceType'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setOperationSelect(operation) {
    await this.operationSelect.sendKeys(operation);
  }

  async getOperationSelect() {
    return this.operationSelect.element(by.css('option:checked')).getText();
  }

  async operationSelectLastOption() {
    await this.operationSelect.all(by.tagName('option')).last().click();
  }
  async setPolicyNoInput(policyNo) {
    await this.policyNoInput.sendKeys(policyNo);
  }

  async getPolicyNoInput() {
    return this.policyNoInput.getAttribute('value');
  }

  async setCertificateNoInput(certificateNo) {
    await this.certificateNoInput.sendKeys(certificateNo);
  }

  async getCertificateNoInput() {
    return this.certificateNoInput.getAttribute('value');
  }

  async setOccupationInput(occupation) {
    await this.occupationInput.sendKeys(occupation);
  }

  async getOccupationInput() {
    return this.occupationInput.getAttribute('value');
  }

  async setSectorInput(sector) {
    await this.sectorInput.sendKeys(sector);
  }

  async getSectorInput() {
    return this.sectorInput.getAttribute('value');
  }

  async setIdTypeInput(idType) {
    await this.idTypeInput.sendKeys(idType);
  }

  async getIdTypeInput() {
    return this.idTypeInput.getAttribute('value');
  }

  async setIdNumberInput(idNumber) {
    await this.idNumberInput.sendKeys(idNumber);
  }

  async getIdNumberInput() {
    return this.idNumberInput.getAttribute('value');
  }

  async setVehicleTypeInput(vehicleType) {
    await this.vehicleTypeInput.sendKeys(vehicleType);
  }

  async getVehicleTypeInput() {
    return this.vehicleTypeInput.getAttribute('value');
  }

  async setRegistrationNoInput(registrationNo) {
    await this.registrationNoInput.sendKeys(registrationNo);
  }

  async getRegistrationNoInput() {
    return this.registrationNoInput.getAttribute('value');
  }

  async setVehMakeInput(vehMake) {
    await this.vehMakeInput.sendKeys(vehMake);
  }

  async getVehMakeInput() {
    return this.vehMakeInput.getAttribute('value');
  }

  async setVehModelInput(vehModel) {
    await this.vehModelInput.sendKeys(vehModel);
  }

  async getVehModelInput() {
    return this.vehModelInput.getAttribute('value');
  }

  async setVehYearInput(vehYear) {
    await this.vehYearInput.sendKeys(vehYear);
  }

  async getVehYearInput() {
    return this.vehYearInput.getAttribute('value');
  }

  async setRegistrationStartInput(registrationStart) {
    await this.registrationStartInput.sendKeys(registrationStart);
  }

  async getRegistrationStartInput() {
    return this.registrationStartInput.getAttribute('value');
  }

  async setExpiryDateInput(expiryDate) {
    await this.expiryDateInput.sendKeys(expiryDate);
  }

  async getExpiryDateInput() {
    return this.expiryDateInput.getAttribute('value');
  }

  async setMileageInput(mileage) {
    await this.mileageInput.sendKeys(mileage);
  }

  async getMileageInput() {
    return this.mileageInput.getAttribute('value');
  }

  async setVehColorInput(vehColor) {
    await this.vehColorInput.sendKeys(vehColor);
  }

  async getVehColorInput() {
    return this.vehColorInput.getAttribute('value');
  }

  async setChassisNoInput(chassisNo) {
    await this.chassisNoInput.sendKeys(chassisNo);
  }

  async getChassisNoInput() {
    return this.chassisNoInput.getAttribute('value');
  }

  async setEngineNoInput(engineNo) {
    await this.engineNoInput.sendKeys(engineNo);
  }

  async getEngineNoInput() {
    return this.engineNoInput.getAttribute('value');
  }

  async setEngineCapacityInput(engineCapacity) {
    await this.engineCapacityInput.sendKeys(engineCapacity);
  }

  async getEngineCapacityInput() {
    return this.engineCapacityInput.getAttribute('value');
  }

  async setSeatCapacityInput(seatCapacity) {
    await this.seatCapacityInput.sendKeys(seatCapacity);
  }

  async getSeatCapacityInput() {
    return this.seatCapacityInput.getAttribute('value');
  }

  async setBalanceInput(balance) {
    await this.balanceInput.sendKeys(balance);
  }

  async getBalanceInput() {
    return this.balanceInput.getAttribute('value');
  }

  async profileSelectLastOption() {
    await this.profileSelect.all(by.tagName('option')).last().click();
  }

  async profileSelectOption(option) {
    await this.profileSelect.sendKeys(option);
  }

  getProfileSelect() {
    return this.profileSelect;
  }

  async getProfileSelectedOption() {
    return this.profileSelect.element(by.css('option:checked')).getText();
  }

  async insuranceTypeSelectLastOption() {
    await this.insuranceTypeSelect.all(by.tagName('option')).last().click();
  }

  async insuranceTypeSelectOption(option) {
    await this.insuranceTypeSelect.sendKeys(option);
  }

  getInsuranceTypeSelect() {
    return this.insuranceTypeSelect;
  }

  async getInsuranceTypeSelectedOption() {
    return this.insuranceTypeSelect.element(by.css('option:checked')).getText();
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
    await this.operationSelectLastOption();
    await waitUntilDisplayed(this.saveButton);
    await this.setPolicyNoInput('policyNo');
    expect(await this.getPolicyNoInput()).to.match(/policyNo/);
    await waitUntilDisplayed(this.saveButton);
    await this.setCertificateNoInput('certificateNo');
    expect(await this.getCertificateNoInput()).to.match(/certificateNo/);
    await waitUntilDisplayed(this.saveButton);
    await this.setOccupationInput('occupation');
    expect(await this.getOccupationInput()).to.match(/occupation/);
    await waitUntilDisplayed(this.saveButton);
    await this.setSectorInput('sector');
    expect(await this.getSectorInput()).to.match(/sector/);
    await waitUntilDisplayed(this.saveButton);
    await this.setIdTypeInput('idType');
    expect(await this.getIdTypeInput()).to.match(/idType/);
    await waitUntilDisplayed(this.saveButton);
    await this.setIdNumberInput('idNumber');
    expect(await this.getIdNumberInput()).to.match(/idNumber/);
    await waitUntilDisplayed(this.saveButton);
    await this.setVehicleTypeInput('vehicleType');
    expect(await this.getVehicleTypeInput()).to.match(/vehicleType/);
    await waitUntilDisplayed(this.saveButton);
    await this.setRegistrationNoInput('registrationNo');
    expect(await this.getRegistrationNoInput()).to.match(/registrationNo/);
    await waitUntilDisplayed(this.saveButton);
    await this.setVehMakeInput('vehMake');
    expect(await this.getVehMakeInput()).to.match(/vehMake/);
    await waitUntilDisplayed(this.saveButton);
    await this.setVehModelInput('vehModel');
    expect(await this.getVehModelInput()).to.match(/vehModel/);
    await waitUntilDisplayed(this.saveButton);
    await this.setVehYearInput('5');
    expect(await this.getVehYearInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setRegistrationStartInput('registrationStart');
    expect(await this.getRegistrationStartInput()).to.match(/registrationStart/);
    await waitUntilDisplayed(this.saveButton);
    await this.setExpiryDateInput('expiryDate');
    expect(await this.getExpiryDateInput()).to.match(/expiryDate/);
    await waitUntilDisplayed(this.saveButton);
    await this.setMileageInput('mileage');
    expect(await this.getMileageInput()).to.match(/mileage/);
    await waitUntilDisplayed(this.saveButton);
    await this.setVehColorInput('vehColor');
    expect(await this.getVehColorInput()).to.match(/vehColor/);
    await waitUntilDisplayed(this.saveButton);
    await this.setChassisNoInput('chassisNo');
    expect(await this.getChassisNoInput()).to.match(/chassisNo/);
    await waitUntilDisplayed(this.saveButton);
    await this.setEngineNoInput('engineNo');
    expect(await this.getEngineNoInput()).to.match(/engineNo/);
    await waitUntilDisplayed(this.saveButton);
    await this.setEngineCapacityInput('engineCapacity');
    expect(await this.getEngineCapacityInput()).to.match(/engineCapacity/);
    await waitUntilDisplayed(this.saveButton);
    await this.setSeatCapacityInput('seatCapacity');
    expect(await this.getSeatCapacityInput()).to.match(/seatCapacity/);
    await waitUntilDisplayed(this.saveButton);
    await this.setBalanceInput('5');
    expect(await this.getBalanceInput()).to.eq('5');
    await this.profileSelectLastOption();
    await this.insuranceTypeSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
