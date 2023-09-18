import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ApprovalWorkflowComponentsPage from './approval-workflow.page-object';
import ApprovalWorkflowUpdatePage from './approval-workflow-update.page-object';
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

describe('ApprovalWorkflow e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let approvalWorkflowComponentsPage: ApprovalWorkflowComponentsPage;
  let approvalWorkflowUpdatePage: ApprovalWorkflowUpdatePage;

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
    approvalWorkflowComponentsPage = new ApprovalWorkflowComponentsPage();
    approvalWorkflowComponentsPage = await approvalWorkflowComponentsPage.goToPage(navBarPage);
  });

  it('should load ApprovalWorkflows', async () => {
    expect(await approvalWorkflowComponentsPage.title.getText()).to.match(/Approval Workflows/);
    expect(await approvalWorkflowComponentsPage.createButton.isEnabled()).to.be.true;
  });

  /* it('should create and delete ApprovalWorkflows', async () => {
        const beforeRecordsCount = await isVisible(approvalWorkflowComponentsPage.noRecords) ? 0 : await getRecordsCount(approvalWorkflowComponentsPage.table);
        approvalWorkflowUpdatePage = await approvalWorkflowComponentsPage.goToCreateApprovalWorkflow();
        await approvalWorkflowUpdatePage.enterData();

        expect(await approvalWorkflowComponentsPage.createButton.isEnabled()).to.be.true;
        await waitUntilDisplayed(approvalWorkflowComponentsPage.table);
        await waitUntilCount(approvalWorkflowComponentsPage.records, beforeRecordsCount + 1);
        expect(await approvalWorkflowComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
        
        await approvalWorkflowComponentsPage.deleteApprovalWorkflow();
        if(beforeRecordsCount !== 0) { 
          await waitUntilCount(approvalWorkflowComponentsPage.records, beforeRecordsCount);
          expect(await approvalWorkflowComponentsPage.records.count()).to.eq(beforeRecordsCount);
        } else {
          await waitUntilDisplayed(approvalWorkflowComponentsPage.noRecords);
        }
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
