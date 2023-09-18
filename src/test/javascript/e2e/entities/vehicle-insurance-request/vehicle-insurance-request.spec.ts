import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import VehicleInsuranceRequestComponentsPage from './vehicle-insurance-request.page-object';
import VehicleInsuranceRequestUpdatePage from './vehicle-insurance-request-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('VehicleInsuranceRequest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vehicleInsuranceRequestComponentsPage: VehicleInsuranceRequestComponentsPage;
  let vehicleInsuranceRequestUpdatePage: VehicleInsuranceRequestUpdatePage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    vehicleInsuranceRequestComponentsPage = new VehicleInsuranceRequestComponentsPage();
    vehicleInsuranceRequestComponentsPage = await vehicleInsuranceRequestComponentsPage.goToPage(navBarPage);
  });

  it('should load VehicleInsuranceRequests', async () => {
    expect(await vehicleInsuranceRequestComponentsPage.title.getText()).to.match(/Vehicle Insurance Requests/);
    expect(await vehicleInsuranceRequestComponentsPage.createButton.isEnabled()).to.be.true;
  });

  /* it('should create and delete VehicleInsuranceRequests', async () => {
        const beforeRecordsCount = await isVisible(vehicleInsuranceRequestComponentsPage.noRecords) ? 0 : await getRecordsCount(vehicleInsuranceRequestComponentsPage.table);
        vehicleInsuranceRequestUpdatePage = await vehicleInsuranceRequestComponentsPage.goToCreateVehicleInsuranceRequest();
        await vehicleInsuranceRequestUpdatePage.enterData();

        expect(await vehicleInsuranceRequestComponentsPage.createButton.isEnabled()).to.be.true;
        await waitUntilDisplayed(vehicleInsuranceRequestComponentsPage.table);
        await waitUntilCount(vehicleInsuranceRequestComponentsPage.records, beforeRecordsCount + 1);
        expect(await vehicleInsuranceRequestComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
        
        await vehicleInsuranceRequestComponentsPage.deleteVehicleInsuranceRequest();
        if(beforeRecordsCount !== 0) { 
          await waitUntilCount(vehicleInsuranceRequestComponentsPage.records, beforeRecordsCount);
          expect(await vehicleInsuranceRequestComponentsPage.records.count()).to.eq(beforeRecordsCount);
        } else {
          await waitUntilDisplayed(vehicleInsuranceRequestComponentsPage.noRecords);
        }
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
