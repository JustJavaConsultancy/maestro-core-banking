import { browser } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SuperAgentComponentsPage from './super-agent.page-object';
import SuperAgentUpdatePage from './super-agent-update.page-object';
import { waitUntilDisplayed } from '../../util/utils';

const expect = chai.expect;

describe('SuperAgent e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let superAgentComponentsPage: SuperAgentComponentsPage;
  let superAgentUpdatePage: SuperAgentUpdatePage;

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
    superAgentComponentsPage = new SuperAgentComponentsPage();
    superAgentComponentsPage = await superAgentComponentsPage.goToPage(navBarPage);
  });

  it('should load SuperAgents', async () => {
    expect(await superAgentComponentsPage.title.getText()).to.match(/Super Agents/);
    expect(await superAgentComponentsPage.createButton.isEnabled()).to.be.true;
  });

  /* it('should create and delete SuperAgents', async () => {
        const beforeRecordsCount = await isVisible(superAgentComponentsPage.noRecords) ? 0 : await getRecordsCount(superAgentComponentsPage.table);
        superAgentUpdatePage = await superAgentComponentsPage.goToCreateSuperAgent();
        await superAgentUpdatePage.enterData();

        expect(await superAgentComponentsPage.createButton.isEnabled()).to.be.true;
        await waitUntilDisplayed(superAgentComponentsPage.table);
        await waitUntilCount(superAgentComponentsPage.records, beforeRecordsCount + 1);
        expect(await superAgentComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

        await superAgentComponentsPage.deleteSuperAgent();
        if(beforeRecordsCount !== 0) {
          await waitUntilCount(superAgentComponentsPage.records, beforeRecordsCount);
          expect(await superAgentComponentsPage.records.count()).to.eq(beforeRecordsCount);
        } else {
          await waitUntilDisplayed(superAgentComponentsPage.noRecords);
        }
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
