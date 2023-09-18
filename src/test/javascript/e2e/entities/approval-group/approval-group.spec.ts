import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ApprovalGroupComponentsPage from './approval-group.page-object';
import ApprovalGroupUpdatePage from './approval-group-update.page-object';
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

describe('ApprovalGroup e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let approvalGroupComponentsPage: ApprovalGroupComponentsPage;
  let approvalGroupUpdatePage: ApprovalGroupUpdatePage;

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
    approvalGroupComponentsPage = new ApprovalGroupComponentsPage();
    approvalGroupComponentsPage = await approvalGroupComponentsPage.goToPage(navBarPage);
  });

  it('should load ApprovalGroups', async () => {
    expect(await approvalGroupComponentsPage.title.getText()).to.match(/Approval Groups/);
    expect(await approvalGroupComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete ApprovalGroups', async () => {
    const beforeRecordsCount = (await isVisible(approvalGroupComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(approvalGroupComponentsPage.table);
    approvalGroupUpdatePage = await approvalGroupComponentsPage.goToCreateApprovalGroup();
    await approvalGroupUpdatePage.enterData();

    expect(await approvalGroupComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(approvalGroupComponentsPage.table);
    await waitUntilCount(approvalGroupComponentsPage.records, beforeRecordsCount + 1);
    expect(await approvalGroupComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await approvalGroupComponentsPage.deleteApprovalGroup();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(approvalGroupComponentsPage.records, beforeRecordsCount);
      expect(await approvalGroupComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(approvalGroupComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
