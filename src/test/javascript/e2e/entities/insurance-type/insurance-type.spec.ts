import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import InsuranceTypeComponentsPage from './insurance-type.page-object';
import InsuranceTypeUpdatePage from './insurance-type-update.page-object';
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

describe('InsuranceType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let insuranceTypeComponentsPage: InsuranceTypeComponentsPage;
  let insuranceTypeUpdatePage: InsuranceTypeUpdatePage;

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
    insuranceTypeComponentsPage = new InsuranceTypeComponentsPage();
    insuranceTypeComponentsPage = await insuranceTypeComponentsPage.goToPage(navBarPage);
  });

  it('should load InsuranceTypes', async () => {
    expect(await insuranceTypeComponentsPage.title.getText()).to.match(/Insurance Types/);
    expect(await insuranceTypeComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete InsuranceTypes', async () => {
    const beforeRecordsCount = (await isVisible(insuranceTypeComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(insuranceTypeComponentsPage.table);
    insuranceTypeUpdatePage = await insuranceTypeComponentsPage.goToCreateInsuranceType();
    await insuranceTypeUpdatePage.enterData();

    expect(await insuranceTypeComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(insuranceTypeComponentsPage.table);
    await waitUntilCount(insuranceTypeComponentsPage.records, beforeRecordsCount + 1);
    expect(await insuranceTypeComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await insuranceTypeComponentsPage.deleteInsuranceType();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(insuranceTypeComponentsPage.records, beforeRecordsCount);
      expect(await insuranceTypeComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(insuranceTypeComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
