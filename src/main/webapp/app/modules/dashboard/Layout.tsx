/* eslint-disable complexity */
import React, { useState, useEffect } from 'react';
import './layout.scss';
import { SidebarNav } from './components/SidebarNavs';
import { Link, NavLink, RouteComponentProps } from 'react-router-dom';
import { connect } from 'react-redux';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import Header from 'app/modules/dashboard/header/Header';
import { IoMdArrowDropdown, IoMdArrowDropright } from 'react-icons/io';
import ArrowSide from '../../../content/images/arrow-side.png';
// export interface ILayouts extends StateProps, React.ReactElement {}
const width = 270;
const Layout: any = props => {
  const [activeLink, setActiveLink] = useState(0);
  const [xPosition, setX] = React.useState(-width);
  const [size, setSize] = useState('');
  const [active1, setActive1] = useState(false);
  const [active2, setActive2] = useState(false);
  const [activeRequests, setActiveRequests] = useState(false);

  const toggleActive = () => setActive1(!active1);
  const toggleActive2 = () => setActive2(!active2);
  const toggleRequests = () => setActiveRequests(!activeRequests);

  const toggleMenu = () => {
    if (window.innerWidth > 995) return;
    if (xPosition < 0) {
      setX(0);
    } else {
      setX(-width);
    }
  };

  // school admins
  const isMcPhersonAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON]);
  const isMcPhersonFinancial = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.FUOYE_ADMIN]);
  const isFuoyeFinancial = hasAnyAuthority(props.account.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSch = isMcPhersonAdmin || isFuoyeAdmin;
  const isSchAdmin = isMcPhersonAdmin || isMcPhersonFinancial || isFuoyeAdmin || isFuoyeFinancial
  const isSchFinancial = isMcPhersonFinancial || isFuoyeFinancial

  // systemspecs admin
  const isAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isAdminOperations = hasAnyAuthority(props.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS, AUTHORITIES.SUPPORT]);
  const isPolaris = hasAnyAuthority(props.account.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  
  // financial partners admin
  const isPaymasta = hasAnyAuthority(props.account.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(props.account.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(props.account.authorities, [AUTHORITIES.WYNK_ADMIN]);
  const isPartnerFinancial = isPaymasta || isWragby || isWynk;

  // General financial
  const isFinancial = isAdmin || isSchFinancial || isPartnerFinancial || isPolaris
  
  useEffect(() => {
    window.innerWidth > 995 ? setX(0) : setX(-width);
  }, []);

  return (
    <>
      <Header toggleMenu={toggleMenu} isAuthenticated={props.isAuthenticated} />
      <section className="layoutContainer">
        <div
          style={{
            transform: `translatex(${xPosition}px)`,
            width,
            overflow: 'auto',
            paddingLeft: '10px',
            paddingTop: '32px'
          }}
          className="side-bar"
        >
          <div 
            className={`side-content ${active1 || active2 ? 'side-content-show' : ''}`}
            style={{ overflowY: 'auto' }}
          >
            <div className="inner-side-content-box">
              {isAdmin && (
                <NavLink
                  isActive={(match, location) => {
                    if (match) return false;
                    setActiveLink(0);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/dash"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 0} nav="Overview" top />
                  </div>
                </NavLink>
              )}
              {(isAdmin || isSch || isPartnerFinancial) &&
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(1);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/customers"
                >
                  <div className="sidebar__link">
                    <SidebarNav 
                      active={activeLink === 1} 
                      nav={(isSchAdmin) ? "Students" : "Customers"} 
                    />
                  </div>
                </NavLink>
              }
              {isFinancial &&
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(2);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/dwallets"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 2} nav="Wallets" />
                  </div>
                </NavLink>
              }
              {isFinancial &&
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(3);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/alltransactions"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 3} nav="Transactions" />
                  </div>
                </NavLink>
              }
              {isFinancial && (
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(4);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/reports"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 4} nav="Reports" />
                  </div>
                </NavLink>
              )}
              {isAdmin && (
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(20);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/external-reports"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 20} nav="Reports (external)" />
                  </div>
                </NavLink>
              )}
              {(isSchFinancial || isPartnerFinancial) && (
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(41);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/transferfund"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 41} nav="Send Money" />
                  </div>
                </NavLink>
              )}
              {isAdminOperations && (
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(43);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/ibile-receipts"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 43} nav="Ibile Receipts" />
                  </div>
                </NavLink>
              )}
              {(isAdmin || isAdminOperations || isSch) && 
                <div className={activeRequests ? "active-sub-info" : 'inactive-sub-info'}>
                  <div onClick={toggleRequests} className="inactive_nav">
                    <div style={{
                        width: '100%',
                        display: 'flex',
                        alignItems: 'flex-start'
                    }}>
                          <div style={{ width: activeRequests ? '100%' : '60%' }} className="active__nav">
                            <SidebarNav nav="Posted Requests" active={activeRequests}/>
                          </div>
                          <div
                            style={{ 
                              transform: activeRequests ? 'rotate(90deg)' : 'rotate(0deg)',
                            }}
                          >
                            <img src={ArrowSide} alt='arrow' />
                          </div>
                    </div>
                  </div>
                  {activeRequests && (
                      <div>
                        <NavLink
                          isActive={(match, location) => {
                            if (!match) return false;
                            setActiveLink(27);
                            return true;
                          }}
                          activeClassName="sideNavActive"
                          to="/posted-feeds"
                        >
                          <SidebarNav active={activeLink === 27} nav="Posts" />
                        </NavLink>
                        <NavLink
                          isActive={(match, location) => {
                            if (!match) return false;
                            setActiveLink(28);
                            return true;
                          }}
                          activeClassName="sideNavActive"
                          to="/posted-adverts"
                        >
                          <SidebarNav active={activeLink === 28} nav="Adverts" />
                        </NavLink>
                      </div>
                    )
                  }
                </div>
              }
              {(isAdmin || isAdminOperations || isSch || isPartnerFinancial) && <NavLink
                isActive={(match, location) => {
                  if (!match) return false;
                  setActiveLink(21);
                  return true;
                }}
                activeClassName="sideNavActive"
                to="/messaging"
              >
                <div className="sidebar__link">
                  <SidebarNav active={activeLink === 21} nav="Messaging" />
                </div>
              </NavLink>}
              {isAdmin && (
                <>
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(40);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/end-reports"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 40} nav="End of Day Report" />
                  </div>
                </NavLink>
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(5);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/health"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 5} nav="Service Uptime" />
                  </div>
                </NavLink>
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(6);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/verifyRRR"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 6} nav="Verify RRR" />
                  </div>
                </NavLink>
                </>
               )}
              {isAdmin && (
                <div className={active1 ? "active-sub-info" : 'inactive-sub-info'}>
                  <div onClick={toggleActive} className="inactive_nav">
                    <div style={{
                        width: '100%',
                        display: 'flex',
                        alignItems: 'flex-start'
                    }}>
                          <div style={{ width: active1 ? '100%' : '60%' }} className="active__nav">
                            <SidebarNav nav="Administration" active={active1}/>
                          </div>
                          <div
                            style={{ 
                              transform: active1 ? 'rotate(90deg)' : 'rotate(0deg)',
                            }}
                          >
                            <img src={ArrowSide} alt='arrow' />
                          </div>
                    </div>
                  </div>
              
               {active1 && (
                <div>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(10);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/create-request"
                  >
                    <SidebarNav active={activeLink === 10} nav="Create Request" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(12);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/request-status"
                  >
                    <SidebarNav active={activeLink === 12} nav="Request Status" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(11);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/requests"
                  >
                    <SidebarNav active={activeLink === 11} nav="Approve Requests" />
                  </NavLink>
                  {isAdminOperations && (
                    <NavLink
                      isActive={(match, location) => {
                        if (!match) return false;
                        setActiveLink(8);
                        return true;
                      }}
                      activeClassName="sideNavActive"
                      to="/schemes"
                    >
                      <SidebarNav active={activeLink === 8} nav="Scheme Management" />
                    </NavLink>
                  )}
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(9);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/transit"
                  >
                    <SidebarNav active={activeLink === 9} nav="Transit statement" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(90);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/app-management"
                  >
                    <SidebarNav active={activeLink === 90} nav="App Management" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(24);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/super-agents"
                  >
                    <SidebarNav active={activeLink === 24} nav="Super Agents" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(14);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/approval-groups"
                  >
                    <SidebarNav active={activeLink === 14} nav="Approval Groups" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(13);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/approval-workflow"
                  >
                    <SidebarNav active={activeLink === 13} nav="Approval Workflow" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(17);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/show-nuban"
                  >
                    <SidebarNav active={activeLink === 17} nav="Nuban Accounts" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(18);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/kyc-request"
                  >
                    <SidebarNav active={activeLink === 18} nav="KYC Request" />
                  </NavLink>
                  <NavLink
                    isActive={(match, location) => {
                      if (!match) return false;
                      setActiveLink(19);
                      return true;
                    }}
                    activeClassName="sideNavActive"
                    to="/kyc-docs"
                  >
                    <SidebarNav active={activeLink === 19} nav="KYC Documents" />
                  </NavLink>
                </div>
                )}
              </div>
              )}
              {isAdmin && (
                <div className={active2 ? "active-sub-info" : 'inactive-sub-info'}>
                  <div onClick={toggleActive2} className="inactive_nav">
                    <div style={{
                        width: '100%',
                        display: 'flex',
                        alignItems: 'flex-start'
                    }}>
                          <div style={{ width: active2 ? '100%' : '70%' }} className="active__nav">
                            <SidebarNav nav="Card Management" active={active2}/>
                          </div>
                          <div
                            style={{ 
                              transform: active2 ? 'rotate(90deg)' : 'rotate(0deg)',
                            }}
                          >
                            <img src={ArrowSide} alt='arrow' />
                          </div>
                    </div>
                  </div>
                  {active2 && (
                    <div className="aside-sub-info">
                      <NavLink
                        isActive={(match, location) => {
                          if (!match) return false;
                          setActiveLink(25);
                          return true;
                        }}
                        activeClassName="sideNavActive"
                        to="/card-summary"
                      >
                        <SidebarNav active={activeLink === 25} nav="Card Summary" />
                      </NavLink>
                      <NavLink
                        isActive={(match, location) => {
                          if (!match) return false;
                          setActiveLink(26);
                          return true;
                        }}
                        activeClassName="sideNavActive"
                        to="/card-request"
                      >
                        <SidebarNav active={activeLink === 26} nav="Card Request" />
                      </NavLink>
                      <NavLink
                        isActive={(match, location) => {
                          if (!match) return false;
                          setActiveLink(30);
                          return true;
                        }}
                        activeClassName="sideNavActive"
                        to="/card-report"
                      >
                        <SidebarNav active={activeLink === 30} nav="Card Report" />
                      </NavLink>
                    </div>
                  )}
              </div>
            )}
            {(isSchFinancial) && (
                <NavLink
                  isActive={(match, location) => {
                    if (!match) return false;
                    setActiveLink(42);
                    return true;
                  }}
                  activeClassName="sideNavActive"
                  to="/change-password"
                >
                  <div className="sidebar__link">
                    <SidebarNav active={activeLink === 42} nav="Change Password" />
                  </div>
                </NavLink>
              )}
            </div>
          </div>
        </div>
        <div
          style={{
            transform: `translatex(${xPosition + width}px)`,
            overflow: 'hidden',
            marginRight: window.innerWidth > 995 ? `${width + 36}px` : 0,
            marginLeft: window.innerWidth > 995 ? '25px' : 0,
            marginTop: '24px',
          }}
        >
          {props.children}
          {window.innerWidth < 995 && xPosition === 0 && <div onClick={toggleMenu} className="blindFold"></div>}
        </div>
      </section>
    </>
  );
};
const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});
type StateProps = ReturnType<typeof mapStateToProps>;
export default connect(mapStateToProps)(Layout);
